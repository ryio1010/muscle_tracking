package com.example.muscletracking.view.home.log

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R
import com.example.muscletracking.model.log.Log
import com.example.muscletracking.model.musclepart.MusclePart
import com.example.muscletracking.model.musclepart.MusclePartResponse
import com.example.muscletracking.view.home.loghistory.TrainingLogViewHolder

class TrainingPartListAdapter(
    private val list: List<MusclePart>,
    private val listener: ListListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ListListener {
        fun onClickItem(tappedView: View, musclePart:MusclePart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_training_part, parent, false)
        return TrainingPartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tvTrainingPartId).text =
            list[position].musclePartId
        holder.itemView.findViewById<TextView>(R.id.tvTrainingPartName).text =
            list[position].musclePartName

        holder.itemView.setOnClickListener {
            listener.onClickItem(it, list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}