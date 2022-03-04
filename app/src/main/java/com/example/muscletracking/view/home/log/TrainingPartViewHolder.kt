package com.example.muscletracking.view.home.log

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R

class TrainingPartViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    val trainingPartId = itemView.findViewById<TextView>(R.id.tvTrainingPartId)
    val trainingPartName = itemView.findViewById<TextView>(R.id.tvTrainingPartName)
}