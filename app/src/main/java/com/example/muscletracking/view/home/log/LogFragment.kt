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
import com.example.muscletracking.view.home.HomeActivity
import com.example.muscletracking.view.home.logdetail.LogDetailActivity
import com.example.muscletracking.R
import com.example.muscletracking.model.log.Log
import com.example.muscletracking.model.log.LogResponse
import com.example.muscletracking.viewmodel.log.LogViewModel
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
    private lateinit var btAddLog: Button
    private lateinit var inputTrainingMenuContainer: TextView
    private lateinit var inputTrainingWeightContainer: EditText
    private lateinit var inputTrainingCountContainer: EditText
    private lateinit var inputTrainingMemoContainer: EditText

    // 日付
    private lateinit var dateForView: String
    private lateinit var dateForApi: String

    // メニューID
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
        inputTrainingMenuContainer = view.findViewById(R.id.tvTrainingMenu)
        inputTrainingWeightContainer = view.findViewById(R.id.etTrainingWeight)
        inputTrainingCountContainer = view.findViewById(R.id.etTrainingCount)
        inputTrainingMemoContainer = view.findViewById(R.id.etTrainingMemo)

        // トレーニングメニュー選択ボタン押下時の処理
        btMenuSelect = view.findViewById(R.id.btSelectMenu)
        btMenuSelect.setOnClickListener {
            // メニュー選択画面へ遷移
            startForResult.launch(Intent(activity, MenuSelectActivity::class.java))
        }

        // 日付初期設定（現在日付）
        val sdfForView = SimpleDateFormat("yyyy年MM月dd日")
        val sdfForApi = SimpleDateFormat("yyyyMMdd")
        val today = Date(System.currentTimeMillis())

        dateForView = sdfForView.format(today)
        dateForApi = sdfForApi.format(today)

        tvDate = view.findViewById(R.id.tvTrainingDate)
        tvDate.text = dateForView

        // トレーニング日付選択ボタン押下時の処理
        btDateSelect = view.findViewById(R.id.btSelectDate)
        btDateSelect.setOnClickListener {
            val dialog = DatePickerFragment()
            dialog.show(childFragmentManager, "date_picker")
        }

        // 登録ボタン押下時の処理
        btAddLog = view.findViewById(R.id.logRegisterButton)
        btAddLog.setOnClickListener(RegisterButtonClickListener())

        // ログ追加APIのObserver
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

            // ログ追加後ダイアログ表示
            createAddedLogDialog(it)
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

    private inner class RegisterButtonClickListener() : View.OnClickListener {
        override fun onClick(view: View?) {
            (activity as HomeActivity).hideKeyboard(view!!)
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
        }
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

    private fun createAddedLogDialog(logResponse: LogResponse) {
        // 追加完了ダイアログViewの設定
        val dialogContext =
            LayoutInflater.from(activity).inflate(R.layout.item_added_training, null)
        dialogContext.findViewById<TextView>(R.id.tvAddedTrainingDate).text =
            dateForView
        dialogContext.findViewById<TextView>(R.id.tvAddedTrainingMenu).text =
            logResponse.menuName
        dialogContext.findViewById<TextView>(R.id.tvAddedTrainingWeight).text =
            logResponse.trainingWeight.toString()
        dialogContext.findViewById<TextView>(R.id.tvAddedTrainingCount).text =
            logResponse.trainingCount.toString()

        val dialog = AlertDialog.Builder(activity)
        dialog.setView(dialogContext)
        dialog.setPositiveButton(
            R.string.bt_modify,
            DialogInterface.OnClickListener { _, _ ->
                // ログ詳細画面へ遷移
                val intent = Intent(activity, LogDetailActivity::class.java)
                intent.putExtra("logId", logResponse.logId.toString())
                startActivity(intent)
            }
        )
        dialog.setNegativeButton(R.string.bt_complete, null)
        dialog.show()
    }
}