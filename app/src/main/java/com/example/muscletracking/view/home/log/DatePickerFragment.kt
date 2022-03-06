package com.example.muscletracking.view.home.log

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    interface OnselectedListener {
        fun selectedDate(year: Int, month: Int, dayOfMonth: Int)
    }

    private lateinit var listener: OnselectedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnselectedListener) {
            listener = parentFragment as OnselectedListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = Date().time
        val context = context
        return when {
            context != null -> {
                DatePickerDialog(
                    context,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE),
                )
            }
            else -> super.onCreateDialog(savedInstanceState)
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.listener.selectedDate(year, month, dayOfMonth)
    }
}