package com.example.muscletracking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.MenuRes
import com.example.muscletracking.model.menu.MenuResponse
import com.example.muscletracking.model.musclepart.MusclePartResponse

class TrainingMenuListAdapter(val context: Context, val trainingMenuList:ArrayList<MenuResponse>):BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.item_training_menu,null)
        val musclePart = view.findViewById<TextView>(R.id.tvTrainingPartOfMenu)
        val trainingMenu = view.findViewById<TextView>(R.id.tvTrainingMenu)
        val trainingMenuList = trainingMenuList[position]
        musclePart.text = trainingMenuList.musclePart
        trainingMenu.text = trainingMenuList.menuName
        return view
    }

    override fun getItem(position: Int): Any {
        return trainingMenuList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return trainingMenuList.size
    }

}