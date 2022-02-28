package com.example.muscletracking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.muscletracking.model.musclepart.MusclePartResponse

class ListAdapter(val context: Context, val musclePartList:ArrayList<MusclePartResponse>):BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.item_training_part,null)
        val musclePart = view.findViewById<TextView>(R.id.tvTrainingPart)
        val musclePartList = musclePartList[position]
        musclePart.text = musclePartList.musclePartName
        return view
    }

    override fun getItem(position: Int): Any {
        return musclePartList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return musclePartList.size
    }

}