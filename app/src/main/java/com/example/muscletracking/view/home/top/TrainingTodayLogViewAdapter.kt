package com.example.muscletracking.view.home.top

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R
import com.example.muscletracking.model.log.Log

class TrainingTodayLogViewAdapter(
    private val list: List<Log>,
    private val listener: ListListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ListListener {
        fun onClickItem(tappedView: View, log: Log)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_today_training, parent, false)
        return TrainingTodayLogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.ivTrainingDateLog)
        val resId = when (list[position].musclePart) {
            "胸" -> R.drawable.chest
            "背中" -> R.drawable.back
            "腕" -> R.drawable.arm
            "肩" -> R.drawable.shoulder
            "脚" -> R.drawable.leg
            else -> R.drawable.others
        }
        imageView.setImageResource(resId)


        holder.itemView.findViewById<TextView>(R.id.tvLogIdTodayInvisible).text =
            list[position].logId.toString()
        holder.itemView.findViewById<TextView>(R.id.tvTrainingWeightOfTodayLog).text =
            list[position].trainingWeight.toString()
        holder.itemView.findViewById<TextView>(R.id.tvTrainingCountOfTodayLog).text =
            list[position].trainingCount.toString()
        holder.itemView.findViewById<TextView>(R.id.tvTrainingMenuOfTodayLog).text =
            list[position].menuName
        holder.itemView.setOnClickListener {
            listener.onClickItem(it, list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}