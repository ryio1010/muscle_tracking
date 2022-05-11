package com.example.muscletracking.view.home.log

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R
import com.example.muscletracking.model.menu.Menu

class TrainingMenuListAdapter(
    private val list: List<Menu>,
    private val listener: ListListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ListListener {
        fun onClickItem(tappedView: View, menu: Menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_training_menu, parent, false)
        return TrainingMenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.ivTrainingMenu)
        val resId = when (list[position].musclePart) {
            "胸" -> R.drawable.chest
            "背中" -> R.drawable.back
            "腕" -> R.drawable.arm
            "肩" -> R.drawable.shoulder
            "脚" -> R.drawable.leg
            else -> R.drawable.others
        }
        imageView.setImageResource(resId)

        holder.itemView.findViewById<TextView>(R.id.tvTrainingMenu).text =
            list[position].menuName

        holder.itemView.findViewById<TextView>(R.id.tvTrainingMenuId).text =
            list[position].menuId.toString()

        holder.itemView.setOnClickListener {
            listener.onClickItem(it, list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}