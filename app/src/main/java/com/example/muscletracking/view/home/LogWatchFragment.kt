package com.example.muscletracking.view.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.muscletracking.R
import com.example.muscletracking.view.home.log.DatePickerFragment
import com.example.muscletracking.viewmodel.log.LogViewModel
import com.example.muscletracking.viewmodel.musclepart.MusclePartViewModel
import java.text.SimpleDateFormat
import java.util.*

class LogWatchFragment : Fragment(), DatePickerFragment.OnselectedListener {

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

    private lateinit var tvDate: TextView
    private lateinit var btDateSelect: Button
    private lateinit var btMenuSelect: Button
    private lateinit var watchLog: com.example.muscletracking.model.log.Log

    // 日付
    private lateinit var dateForView: String
    private lateinit var dateForApi: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log_watch, container, false)
        val watchTrainingMenuContainer = view.findViewById<TextView>(R.id.tvWatchTrainingMenu)
        val watchTrainingDateContainer = view.findViewById<TextView>(R.id.tvWatchTrainingDate)
        val watchTrainingWeightContainer = view.findViewById<TextView>(R.id.etWatchTrainingWeight)
        val watchTrainingCountContainer = view.findViewById<TextView>(R.id.etWatchTrainingCount)
        val watchTrainingMemoContainer = view.findViewById<TextView>(R.id.etWatchTrainingMemo)

        tvDate = view.findViewById<TextView>(R.id.tvWatchTrainingDate)
        val sdfForView = SimpleDateFormat("yyyy年MM月dd日")
        val sdfForApi = SimpleDateFormat("yyyyMMdd")

        val bundle = arguments
        val logId = bundle?.getString("logId")
        logViewModel.getLogById(logId!!)
        logViewModel.logById.observe(this, Observer {
            watchLog = it
            watchTrainingMenuContainer.text = watchLog.menuName
            watchTrainingDateContainer.text = watchLog.trainingDate
            watchTrainingWeightContainer.text = watchLog.trainingWeight.toString()
            watchTrainingCountContainer.text = watchLog.trainingCount.toString()
            watchTrainingMemoContainer.text = watchLog.trainingMemo
        })


        // 修正ボタン押下処理
        val modifyButton = view.findViewById<Button>(R.id.btLogModify)
        modifyButton.setOnClickListener {
            val trainingMenu = watchTrainingMenuContainer.text.toString()
            val trainingDate = watchTrainingDateContainer.text.toString()
            val trainingWeight = watchTrainingWeightContainer.text.toString()
            val trainingCount = watchTrainingCountContainer.text.toString()
            val trainingMemo = watchTrainingMemoContainer.text.toString()
            // API実行して、前の画面に戻る
            val dialog = AlertDialog.Builder(activity)
            dialog.setMessage(R.string.msg_dialog_modify)
            dialog.setPositiveButton(
                R.string.bt_dialog_modify,
                DialogInterface.OnClickListener { _, _ ->
                    logViewModel.updateLog(
                        watchLog.logId.toString(),
                        watchLog.menuId.toString(),
                        trainingMenu,
                        trainingWeight,
                        trainingCount,
                        trainingDate,
                        trainingMemo,
                        "ryio1010"
                    )
                }
            )
            dialog.setNegativeButton(R.string.bt_dialog_cancel, null)
            dialog.show()

        }
        logViewModel.updatedLog.observe(this, Observer {
            val log = com.example.muscletracking.model.log.Log(
                it.logId,
                it.menuId,
                it.menuName,
                it.trainingWeight,
                it.trainingCount,
                it.trainingDate,
                it.trainingMemo
            )
            logViewModel.updateLogOfDB(log)
            findNavController().popBackStack()
        })

        // 削除ボタン押下処理
        val deleteButton = view.findViewById<Button>(R.id.btLogDelete)
        deleteButton.setOnClickListener {
            // API実行して、前の画面に戻る
            val dialog = AlertDialog.Builder(activity)
            dialog.setMessage(R.string.msg_dialog_delete)
            dialog.setPositiveButton(
                R.string.bt_dialog_delete,
                DialogInterface.OnClickListener { _, _ ->
                    logViewModel.deleteLog(watchLog.logId.toString())
                }
            )
            dialog.setNegativeButton(R.string.bt_dialog_cancel, null)
            dialog.show()
        }
        logViewModel.logIdDeleted.observe(this, Observer {
            logViewModel.deleteLogOfDB(watchLog)
            findNavController().popBackStack()
        })


        // トレーニングメニュー選択
//        btMenuSelect = view.findViewById<Button>(R.id.btWatchSelectMenu)
//        btMenuSelect.setOnClickListener {
//            musclePartViewModel.getAllMusclePartFromDB()
//        }
//        musclePartViewModel.musclePartListOfDB.observe(this, androidx.lifecycle.Observer {
//            val allMusclePart = it
//            Log.d("debug", allMusclePart.toString())
//            findNavController().navigate(R.id.action_logWatchFragment_to_trainingPartListFragment)
//        })

        // トレーニング日付選択
//        tvDate = view.findViewById<TextView>(R.id.tvWatchTrainingDate)
//        val sdfForView = SimpleDateFormat("yyyy年MM月dd日")
//        val sdfForApi = SimpleDateFormat("yyyyMMdd")
//        dateForView = sdfForView.format(Date(System.currentTimeMillis()))
//        dateForApi = sdfForApi.format(Date(System.currentTimeMillis()))
//        tvDate.text = dateForView
//
//        // トレーニング日付選択時の処理
//        btDateSelect = view.findViewById<Button>(R.id.btWatchSelectDate)
//        btDateSelect.setOnClickListener {
//            val dialog = DatePickerFragment()
//            dialog.show(childFragmentManager, "date_picker")
//        }

        return view
    }

    override fun selectedDate(year: Int, month: Int, dayOfMonth: Int) {
        dateForView = "%04d年%02d月%02d日".format(year, month + 1, dayOfMonth)
        dateForApi = "%04d%02d%02d".format(year, month + 1, dayOfMonth)
        tvDate.text = dateForView
    }
}