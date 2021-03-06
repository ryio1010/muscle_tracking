package com.example.muscletracking.view.home.log

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R

class TrainingMenuViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    val trainingMenuId = itemView.findViewById<TextView>(R.id.tvTrainingMenuId)
    val trainingMenu = itemView.findViewById<TextView>(R.id.tvTrainingMenu)
}