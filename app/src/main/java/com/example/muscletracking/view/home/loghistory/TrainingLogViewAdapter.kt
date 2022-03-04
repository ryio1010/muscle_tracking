package com.example.muscletracking.view.home.loghistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R
import com.example.muscletracking.model.log.Log

class TrainingLogViewAdapter(
    private val list: List<Log>,
    private val listener: ListListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ListListener {
        fun onClickItem(tappedView: View, log: Log)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_training_log, parent, false)
        return TrainingLogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tvTrainingDateOfLog).text =
            list[position].trainingDate
        holder.itemView.findViewById<TextView>(R.id.tvTrainingWeightOfLog).text =
            list[position].trainingWeight.toString()
        holder.itemView.findViewById<TextView>(R.id.tvTrainingCountOfLog).text =
            list[position].trainingCount.toString()
        holder.itemView.findViewById<TextView>(R.id.tvTrainingMenuOfLog).text = list[position].menuName
        holder.itemView.setOnClickListener {
            listener.onClickItem(it, list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}