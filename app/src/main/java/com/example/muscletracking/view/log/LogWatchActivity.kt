package com.example.muscletracking.view.log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.GlobalApplication
import com.example.muscletracking.R

class LogWatchActivity : AppCompatActivity() {
    val globalApplication = GlobalApplication.getInstance()
    private var logTrainingHistories = globalApplication._addTrainingMenu
    private var tmpLogTrainingHistories = logTrainingHistories.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_watch)


        // set spinner of muscle groups
        val spinnerMuscleGroups = findViewById<Spinner>(R.id.spinnerMuscleGroups)
        val adapterMuscleGroups =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, globalApplication.muscleGroups)
        adapterMuscleGroups.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerMuscleGroups.adapter = adapterMuscleGroups


//        // set recycleview of training menus
//        val logHistories = findViewById<RecyclerView>(R.id.rvLogHistories)
//        val layout = LinearLayoutManager(this@SelectLogActivity)
//        logHistories.layoutManager = layout
//        val adapter = RecyclerListAdapter(tmpLogTrainingHistories)
//        logHistories.adapter = adapter
//        val decorator = DividerItemDecoration(this@SelectLogActivity, layout.orientation)
//        logHistories.addItemDecoration(decorator)
//
//        // set listener of filter button
//        val filterButton = findViewById<Button>(R.id.btFilter)
//        filterButton.setOnClickListener {
//            tmpLogTrainingHistories = logTrainingHistories.toMutableList()
//            val muscleGroup = spinnerMuscleGroups.selectedItem.toString()
//            var filteredLogHistories : MutableList<MutableMap<String, Any>> = mutableListOf()
//            for (history in tmpLogTrainingHistories){
//                if (history.values.contains(muscleGroup)){
//                    filteredLogHistories.add(history)
//                }
//            }
//            tmpLogTrainingHistories.clear()
//            for (history in filteredLogHistories){
//                tmpLogTrainingHistories.add(history)
//            }
//            adapter.notifyDataSetChanged()
//        }
    }

    private inner class RecyclerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _trainingParts: TextView
        var _trainingMenu: TextView
        var _trainingWeight: TextView
        var _trainingCount: TextView

        init {
            _trainingParts = itemView.findViewById(R.id.trainingParts)
            _trainingMenu = itemView.findViewById(R.id.trainingMenu)
            _trainingWeight = itemView.findViewById(R.id.trainingWeight)
            _trainingCount = itemView.findViewById(R.id.trainingCount)
        }
    }

    private inner class RecyclerListAdapter(private val _listData: MutableList<MutableMap<String, Any>>) :
        RecyclerView.Adapter<RecyclerListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerListViewHolder {
            val inflater = LayoutInflater.from(this@LogWatchActivity)
            val view = inflater.inflate(R.layout.row, parent, false)
            val holder = RecyclerListViewHolder(view)
            return holder
        }

        override fun onBindViewHolder(holder: RecyclerListViewHolder, position: Int) {
            val item = _listData[position]
            val trainingParts = item["parts"] as String
            val trainingMenu = item["menu"] as String
            val trainingWeight = item["weight"] as String
            val trainingCount = item["count"] as String

            holder._trainingParts.text = trainingParts
            holder._trainingMenu.text = trainingMenu
            holder._trainingWeight.text = trainingWeight
            holder._trainingCount.text = trainingCount
        }

        override fun getItemCount(): Int {
            return _listData.size
        }
    }

}