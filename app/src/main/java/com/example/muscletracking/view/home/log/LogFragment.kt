package com.example.muscletracking.view.home.log

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.muscletracking.HomeActivity
import com.example.muscletracking.LogDetailActivity
import com.example.muscletracking.MenuSelectActivity
import com.example.muscletracking.R
import com.example.muscletracking.model.log.Log
import com.example.muscletracking.viewmodel.log.LogViewModel
import com.example.muscletracking.viewmodel.musclepart.MusclePartViewModel
import java.text.SimpleDateFormat
import java.util.*


class LogFragment : Fragment(), DatePickerFragment.OnselectedListener {

    private val logViewModel: LogViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity!!.application)
        ).get(
            LogViewModel::class.java
        )
    }

    // 画面要素の定義
    private lateinit var tvDate: TextView
    private lateinit var btDateSelect: Button
    private lateinit var btMenuSelect: Button

    // 日付
    private lateinit var dateForView: String
    private lateinit var dateForApi: String

    private lateinit var inputTrainingMenuContainer: TextView

    private var selectedMenuId: String? = null

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data.let { data: Intent? ->
                    val selectedMenu = data?.getStringExtra("selectedMenu")
                    selectedMenuId = data?.getStringExtra("selectedMenuId")
                    inputTrainingMenuContainer.text = selectedMenu
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log, container, false)

        // 画面要素の取得
        inputTrainingMenuContainer = view.findViewById<TextView>(R.id.tvTrainingMenu)
        val inputTrainingDateContainer = view.findViewById<TextView>(R.id.tvTrainingDate)
        val inputTrainingWeightContainer = view.findViewById<EditText>(R.id.etTrainingWeight)
        val inputTrainingCountContainer = view.findViewById<EditText>(R.id.etTrainingCount)
        val inputTrainingMemoContainer = view.findViewById<EditText>(R.id.etTrainingMemo)

        // トレーニングメニュー選択時の処理
        btMenuSelect = view.findViewById<Button>(R.id.btSelectMenu)
        btMenuSelect.setOnClickListener {
            startForResult.launch(Intent(activity, MenuSelectActivity::class.java))
        }

        // 日付初期設定（現在日付）
        tvDate = view.findViewById<TextView>(R.id.tvTrainingDate)
        val sdfForView = SimpleDateFormat("yyyy年MM月dd日")
        val sdfForApi = SimpleDateFormat("yyyyMMdd")
        dateForView = sdfForView.format(Date(System.currentTimeMillis()))
        dateForApi = sdfForApi.format(Date(System.currentTimeMillis()))
        tvDate.text = dateForView

        // トレーニング日付選択時の処理
        btDateSelect = view.findViewById<Button>(R.id.btSelectDate)
        btDateSelect.setOnClickListener {
            val dialog = DatePickerFragment()
            dialog.show(childFragmentManager, "date_picker")
        }

        // 登録ボタンListener登録
        val btAddLog = view.findViewById<Button>(R.id.logRegisterButton)
        btAddLog.setOnClickListener {
            (activity as HomeActivity).hideKeyboard(it)
            // 入力情報の取得
            val inputTrainingMenu = inputTrainingMenuContainer.text.toString()
            val inputTrainingWeight =
                inputTrainingWeightContainer.text.toString()
            val inputTrainingCount =
                inputTrainingCountContainer.text.toString()
            val inputTrainingDate = dateForApi
            val inputTrainingMemo = inputTrainingMemoContainer.text.toString()

            val canRegister = checkValidation(
                inputTrainingMenuContainer,
                inputTrainingWeightContainer,
                inputTrainingCountContainer
            )

            if (canRegister) {
                // ログ追加API実行
                logViewModel.addLog(
                    selectedMenuId!!,
                    inputTrainingMenu,
                    inputTrainingWeight,
                    inputTrainingCount,
                    inputTrainingDate,
                    inputTrainingMemo,
                    (activity as HomeActivity).mUser!!.userId
                )
            }

//            when {
//                inputTrainingMenu == getString(R.string.txt_select) -> {
//                    tvErrorMessage.setText(R.string.msg_no_input_training_menu)
//                    tvErrorMessage.visibility = TextView.VISIBLE
//                }
//                inputTrainingWeight.isEmpty() -> {
//                    tvErrorMessage.setText(R.string.msg_no_input_training_weight)
//                    tvErrorMessage.visibility = TextView.VISIBLE
//                }
//                inputTrainingCount.isEmpty() -> {
//                    tvErrorMessage.setText(R.string.msg_no_input_training_count)
//                    tvErrorMessage.visibility = TextView.VISIBLE
//                }
//                else -> {
//                    // ログ追加API実行
//                    logViewModel.addLog(
//                        selectedMenuId!!,
//                        inputTrainingMenu,
//                        inputTrainingWeight,
//                        inputTrainingCount,
//                        inputTrainingDate,
//                        inputTrainingMemo,
//                        (activity as HomeActivity).mUser!!.userId
//                    )
//                }
//            }
        }

        logViewModel.addedLog.observe(this, androidx.lifecycle.Observer {
            // ローカルDB更新
            val insertLog =
                Log(
                    it.logId,
                    it.menuId,
                    it.menuName,
                    it.trainingWeight,
                    it.trainingCount,
                    it.trainingDate,
                    it.trainingMemo
                )
            logViewModel.insertLogOfDB(insertLog)

            // 追加完了ダイアログViewの設定
            val dialogContext =
                LayoutInflater.from(activity).inflate(R.layout.item_added_training, null)
            dialogContext.findViewById<TextView>(R.id.tvAddedTrainingDate).text =
                dateForView
            dialogContext.findViewById<TextView>(R.id.tvAddedTrainingMenu).text =
                it.menuName
            dialogContext.findViewById<TextView>(R.id.tvAddedTrainingWeight).text =
                it.trainingWeight.toString()
            dialogContext.findViewById<TextView>(R.id.tvAddedTrainingCount).text =
                it.trainingCount.toString()

            val dialog = AlertDialog.Builder(activity)
            dialog.setView(dialogContext)
            dialog.setPositiveButton(
                "修正",
                DialogInterface.OnClickListener { _, _ ->
                    // ログ詳細画面へ遷移
                    val intent = Intent(activity, LogDetailActivity::class.java)
                    intent.putExtra("logId", it.logId.toString())
                    startActivity(intent)
                }
            )
            dialog.setNegativeButton("完了", null)
            dialog.show()
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleTextView = activity!!.findViewById<TextView>(R.id.tvToolBarTitle)
        titleTextView.text = getString(R.string.label_log)
    }

    override fun selectedDate(year: Int, month: Int, dayOfMonth: Int) {
        dateForView = "%04d年%02d月%02d日".format(year, month + 1, dayOfMonth)
        dateForApi = "%04d%02d%02d".format(year, month + 1, dayOfMonth)
        tvDate.text = dateForView
    }

    private fun checkValidation(
        inputMenu: TextView,
        inputWeight: EditText,
        inputCount: EditText
    ): Boolean {
        val menu = inputMenu.text.toString()
        val weight = inputWeight.text.toString()
        val count = inputCount.text.toString()

        // menu
        if (menu == getString(R.string.txt_select)) {
            Toast.makeText(activity, R.string.msg_no_input_training_menu, Toast.LENGTH_SHORT).show()
            return false
        }

        // menu
        if (menu == getString(R.string.txt_select)) {
            inputMenu.requestFocus()
            Toast.makeText(activity, R.string.msg_no_input_training_menu, Toast.LENGTH_SHORT).show()
            return false
        }

        // weight
        if (weight.isEmpty()) {
            inputWeight.requestFocus()
            Toast.makeText(activity, R.string.msg_no_input_training_weight, Toast.LENGTH_SHORT)
                .show()
            return false
        }

        // count
        if (count.isEmpty()) {
            inputCount.requestFocus()
            Toast.makeText(activity, R.string.msg_no_input_training_count, Toast.LENGTH_SHORT)
                .show()
            return false
        }

        return true
    }
}