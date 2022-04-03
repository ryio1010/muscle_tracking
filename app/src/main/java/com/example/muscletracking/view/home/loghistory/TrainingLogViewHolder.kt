package com.example.muscletracking.view.home.loghistory

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R

class TrainingLogViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    val logId = itemView.findViewById<TextView>(R.id.tvLogIdInvisible)
    val trainingDate = itemView.findViewById<TextView>(R.id.tvTrainingDateOfLog)
    val trainingMenu = itemView.findViewById<TextView>(R.id.tvTrainingMenuOfLog)
    val trainingWeight = itemView.findViewById<TextView>(R.id.tvTrainingWeightOfLog)
    val trainingCount = itemView.findViewById<TextView>(R.id.tvTrainingCountOfLog)
}