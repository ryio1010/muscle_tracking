package com.example.muscletracking.view.home

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

        val bundle = arguments
        watchTrainingMenuContainer.text = bundle?.getString("trainingMenu")
        watchTrainingDateContainer.text = bundle?.getString("trainingDate")
        watchTrainingWeightContainer.text = bundle?.getString("trainingWeight")
        watchTrainingCountContainer.text = bundle?.getString("trainingCount")

        // ボタン押下処理
        val modifyButton = view.findViewById<Button>(R.id.btLogModify)
        val deleteButton = view.findViewById<Button>(R.id.btLogDelete)

        modifyButton.setOnClickListener {
            val trainingMenu = watchTrainingMenuContainer.text.toString()
            val trainingDate = watchTrainingDateContainer.text.toString()
            val trainingWeight = watchTrainingWeightContainer.text.toString()
            val trainingCount = watchTrainingCountContainer.text.toString()
            // API実行して、前の画面に戻る
            logViewModel.updateLog(
                "1",
                "1",
                trainingMenu,
                trainingWeight,
                trainingCount,
                trainingDate,
                "ryio1010"
            )
        }
        logViewModel.updatedLog.observe(this, Observer {
            val log = com.example.muscletracking.model.log.Log(
                it.logId,
                it.menuName,
                it.trainingWeight,
                it.trainingCount,
                it.trainingDate
            )
            logViewModel.updateLogOfDB(log)
            findNavController().popBackStack()
        })

        deleteButton.setOnClickListener {
            // API実行して、前の画面に戻る
            logViewModel.deleteLog("1")
        }
        logViewModel.isLogDeleted.observe(this, Observer {
            //logViewModel.deleteLogOfDB()
        })


        // トレーニングメニュー選択
        btMenuSelect = view.findViewById<Button>(R.id.btWatchSelectMenu)
        btMenuSelect.setOnClickListener {
            musclePartViewModel.getAllMusclePartFromDB()
        }

        musclePartViewModel.musclePartListOfDB.observe(this, androidx.lifecycle.Observer {
            val allMusclePart = it
            Log.d("debug", allMusclePart.toString())
            findNavController().navigate(R.id.action_logWatchFragment_to_trainingPartListFragment)
        })

        // トレーニング日付選択
        tvDate = view.findViewById<TextView>(R.id.tvWatchTrainingDate)
        btDateSelect = view.findViewById<Button>(R.id.btWatchSelectDate)
        btDateSelect.setOnClickListener {
            val dialog = DatePickerFragment()
            dialog.show(childFragmentManager, "date_picker")
        }


        return view
    }

    override fun selectedDate(year: Int, month: Int, dayOfMonth: Int) {
        tvDate.text = "%04d%02d%02d".format(year, month + 1, dayOfMonth)
    }
}