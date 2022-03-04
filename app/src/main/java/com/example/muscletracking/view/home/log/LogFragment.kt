package com.example.muscletracking.view.home.log

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.muscletracking.R
import com.example.muscletracking.viewmodel.log.LogViewModel
import com.example.muscletracking.viewmodel.musclepart.MusclePartViewModel
import org.w3c.dom.Text
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

    private lateinit var tvDate: TextView
    private lateinit var btDateSelect: Button
    private lateinit var btMenuSelect: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log, container, false)

        val menu = view.findViewById<TextView>(R.id.tvTrainingMenu)

        // ボタンListener登録
        val btAddLog = view.findViewById<Button>(R.id.logRegisterButton)
        btAddLog.setOnClickListener {
            // 入力情報の取得
            val inputTrainingMenu = view.findViewById<TextView>(R.id.tvTrainingMenu).text.toString()
            val inputTrainingDate = view.findViewById<TextView>(R.id.tvTrainingDate).text.toString()
            val inputTrainingWeight =
                view.findViewById<EditText>(R.id.etTrainingWeight).text.toString()
            val inputTrainingCount =
                view.findViewById<EditText>(R.id.etTrainingCount).text.toString()

            logViewModel.addLog(
                "1",
                "menuSample",
                inputTrainingWeight,
                inputTrainingCount,
                inputTrainingDate,
                "ryio1010"
            )
        }
        logViewModel.isLogAdded.observe(this, androidx.lifecycle.Observer {
            if (it) {
                logViewModel.getAllLog("ryio1010")
            }
        })

        btMenuSelect = view.findViewById<Button>(R.id.btSelectMenu)
        btMenuSelect.setOnClickListener {
            musclePartViewModel.getAllMusclePartFromDB()
        }

        musclePartViewModel.musclePartListOfDB.observe(this, androidx.lifecycle.Observer {
            val allMusclePart = it
            Log.d("debug", allMusclePart.toString())
            findNavController().navigate(R.id.action_logFragment_to_trainingPartListFragment)
//            val fragmentTransaction = parentFragmentManager.beginTransaction()
//            fragmentTransaction.replace(
//                R.id.flHomeContainer,
//                TrainingMenuListFragment.newInstance()
//            )
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()

        })

        // 日付設定
        tvDate = view.findViewById<TextView>(R.id.tvTrainingDate)
        val sdf = SimpleDateFormat("yyyyMMdd")
        val date = sdf.format(Date(System.currentTimeMillis()))
        tvDate.text = date

        btDateSelect = view.findViewById<Button>(R.id.btSelectDate)
        btDateSelect.setOnClickListener {
            val dialog = DatePickerFragment()
            dialog.show(childFragmentManager, "date_picker")
        }
        return view
    }

    override fun selectedDate(year: Int, month: Int, dayOfMonth: Int) {
        tvDate.text = "%04d/%02d/%02d".format(year, month + 1, dayOfMonth)
    }
}