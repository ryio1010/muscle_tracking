package com.example.muscletracking.view.home.top

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R

class TrainingTodayLogViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    val logId = itemView.findViewById<TextView>(R.id.tvLogIdInvisible)
    val trainingMenu = itemView.findViewById<TextView>(R.id.tvTrainingMenuOfTodayLog)
    val trainingWeight = itemView.findViewById<TextView>(R.id.tvTrainingWeightOfTodayLog)
    val trainingCount = itemView.findViewById<TextView>(R.id.tvTrainingCountOfTodayLog)
}