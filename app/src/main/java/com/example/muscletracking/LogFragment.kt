package com.example.muscletracking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.muscletracking.model.musclepart.MusclePart
import com.example.muscletracking.viewmodel.musclepart.MusclePartViewModel
import com.example.muscletracking.viewmodel.user.UserViewModel
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

        btMenuSelect = view.findViewById<Button>(R.id.btSelectMenu)
        btMenuSelect.setOnClickListener {
            musclePartViewModel.getAllMusclePartFromDB()
        }

        musclePartViewModel.musclePartListOfDB.observe(this, androidx.lifecycle.Observer {
            val allMusclePart = it
            Log.d("debug", allMusclePart.toString())
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.flHomeContainer,
                    TrainingMenuListFragment.newInstance()
                )
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()

        })

        // 日付設定
        tvDate = view.findViewById<TextView>(R.id.tvTrainingDate)
        val sdf = SimpleDateFormat("yyyyMMdd")
        val date = sdf.format(Date(System.currentTimeMillis()))
        tvDate.text = date

        btDateSelect = view.findViewById<Button>(R.id.btSelectDate)
        btDateSelect.setOnClickListener {
            val dialog = DatePickerFragment()
            dialog.show(parentFragmentManager, "date_picker")
        }
        return view
    }

    override fun selectedDate(year: Int, month: Int, dayOfMonth: Int) {
        tvDate.setText("%04d/%02d/%02d".format(year, month + 1, dayOfMonth))
    }
}