package com.example.muscletracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


class LogFragment : Fragment(),DatePickerFragment.OnselectedListener {

    private lateinit var tvDate:TextView
    private lateinit var btDateSelect : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log, container, false)

        tvDate = view.findViewById<TextView>(R.id.tvTrainingDate)
        val sdf = SimpleDateFormat("yyyyMMdd")
        val date = sdf.format(Date(System.currentTimeMillis()))
        tvDate.text = date

        btDateSelect = view.findViewById<Button>(R.id.btSelectDate)
        btDateSelect.setOnClickListener{
            val dialog = DatePickerFragment()
            dialog.show(parentFragmentManager,"date_picker")
        }
        return view
    }

    override fun selectedDate(year: Int, month: Int, dayOfMonth: Int) {
        tvDate.setText("%04d/%02d/%02d".format(year, month + 1, dayOfMonth))
    }
}