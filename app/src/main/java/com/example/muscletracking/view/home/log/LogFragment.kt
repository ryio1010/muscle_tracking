package com.example.muscletracking.view.home.log

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.muscletracking.R
import com.example.muscletracking.model.log.Log
import com.example.muscletracking.viewmodel.log.LogViewModel
import com.example.muscletracking.viewmodel.musclepart.MusclePartViewModel
import java.text.SimpleDateFormat
import java.util.*


class LogFragment : Fragment(), DatePickerFragment.OnselectedListener {

    private val musclePartViewModel: MusclePartViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity!!.application)
        ).get(
            MusclePartViewModel::class.java
        )
    }
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
    private lateinit var tvErrorMessage: TextView

    // 日付
    private lateinit var dateForView: String
    private lateinit var dateForApi: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log, container, false)

        // 画面要素の取得
        val inputTrainingMenuContainer = view.findViewById<TextView>(R.id.tvTrainingMenu)
        val inputTrainingDateContainer = view.findViewById<TextView>(R.id.tvTrainingDate)
        val inputTrainingWeightContainer = view.findViewById<TextView>(R.id.etTrainingWeight)
        val inputTrainingCountContainer = view.findViewById<TextView>(R.id.etTrainingCount)
        val inputTrainingMemoContainer = view.findViewById<TextView>(R.id.etTrainingMemo)

        tvErrorMessage = view.findViewById<TextView>(R.id.tvErrorMessageForAddLog)
        tvErrorMessage.visibility = TextView.INVISIBLE

        // トレーニングメニュー選択時の処理
        btMenuSelect = view.findViewById<Button>(R.id.btSelectMenu)
        btMenuSelect.setOnClickListener {
            musclePartViewModel.getAllMusclePartFromDB()
        }
        musclePartViewModel.musclePartListOfDB.observe(this, androidx.lifecycle.Observer {
            findNavController().navigate(R.id.action_logFragment_to_trainingPartListFragment)
        })

        // bundleで値が渡って来た場合に、メニューを設定する
        val bundle = arguments
        inputTrainingMenuContainer.text =
            bundle?.getString("selectedMenu") ?: "選択してください"
        val selectedMenuId = bundle?.getString("selectedMenuId")

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
            // 入力情報の取得
            val inputTrainingMenu = inputTrainingMenuContainer.text.toString()
            val inputTrainingWeight =
                inputTrainingWeightContainer.text.toString()
            val inputTrainingCount =
                inputTrainingCountContainer.text.toString()
            val inputTrainingDate = dateForApi
            val inputTrainingMemo = inputTrainingMemoContainer.text.toString()

            // 入力項目バリデーション
            when {
                inputTrainingMenu.equals(R.string.txt_select) -> {
                    tvErrorMessage.setText(R.string.msg_no_input_training_menu)
                    tvErrorMessage.visibility = TextView.VISIBLE
                }
                inputTrainingWeight.isEmpty() -> {
                    tvErrorMessage.setText(R.string.msg_no_input_training_weight)
                    tvErrorMessage.visibility = TextView.VISIBLE
                }
                inputTrainingCount.isEmpty() -> {
                    tvErrorMessage.setText(R.string.msg_no_input_training_count)
                    tvErrorMessage.visibility = TextView.VISIBLE
                }
                else -> {
                    // ログ追加API実行
                    logViewModel.addLog(
                        selectedMenuId!!,
                        inputTrainingMenu,
                        inputTrainingWeight,
                        inputTrainingCount,
                        inputTrainingDate,
                        inputTrainingMemo,
                        "ryio1010"
                    )
                }
            }
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
                inputTrainingDateContainer.text.toString()
            dialogContext.findViewById<TextView>(R.id.tvAddedTrainingMenu).text =
                inputTrainingMenuContainer.text.toString()
            dialogContext.findViewById<TextView>(R.id.tvAddedTrainingWeight).text =
                inputTrainingWeightContainer.text.toString()
            dialogContext.findViewById<TextView>(R.id.tvAddedTrainingCount).text =
                inputTrainingCountContainer.text.toString()

            val dialog = AlertDialog.Builder(activity)
            dialog.setView(dialogContext)
            dialog.setPositiveButton(
                "修正",
                DialogInterface.OnClickListener { _, _ ->

                }
            )
            dialog.setNegativeButton("完了", null)
            dialog.show()
        })
        return view
    }

    override fun selectedDate(year: Int, month: Int, dayOfMonth: Int) {
        dateForView = "%04d年%02d月%02d日".format(year, month + 1, dayOfMonth)
        dateForApi = "%04d%02d%02d".format(year, month + 1, dayOfMonth)
        tvDate.text = dateForView
    }
}