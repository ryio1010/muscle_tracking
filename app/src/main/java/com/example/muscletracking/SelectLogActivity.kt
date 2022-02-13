package com.example.muscletracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.view.log.LogWatchActivity
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.*

class SelectLogActivity : AppCompatActivity() {
    val globalApplication = GlobalApplication.getInstance()
    private var logTrainingHistories = globalApplication._addTrainingMenu
    private var tmpLogTrainingHistories = logTrainingHistories.toMutableList()
    private lateinit var selectedCalendarDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_log)

        // get chosen calendar
        val sdf = SimpleDateFormat("yyyyMMdd")
        val calendarView = findViewById<CalendarView>(R.id.selectLogCalendar)
        calendarView.setOnDateChangeListener { view, year, month, day ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            val selectedDate = calendar.time
            selectedCalendarDate = sdf.format(selectedDate)
            Toast.makeText(this, selectedCalendarDate, Toast.LENGTH_SHORT).show()
        }

        // set spinner of muscle groups
        val spinnerMuscleGroups = findViewById<Spinner>(R.id.spinnerMuscleGroups)
        val adapterMuscleGroups =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, globalApplication.muscleGroups)
        adapterMuscleGroups.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerMuscleGroups.adapter = adapterMuscleGroups


        // set listener of search button
        val searchLogButton = findViewById<Button>(R.id.btSearchLog)
        searchLogButton.setOnClickListener {
            val client = OkHttpClient.Builder().build()

//            val logInfoService = Retrofit.Builder()
//                .baseUrl("http://0.0.0.0:8080")
//                .client(client)
//                .addConverterFactory(MoshiConverterFactory.create())
//                .build()
//                .create(ApiService::class.java)

            val intent = Intent(this@SelectLogActivity, LogWatchActivity::class.java)
            startActivity(intent)
        }


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
        RecyclerView.Adapter<SelectLogActivity.RecyclerListViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SelectLogActivity.RecyclerListViewHolder {
            val inflater = LayoutInflater.from(this@SelectLogActivity)
            val view = inflater.inflate(R.layout.row, parent, false)
            val holder = RecyclerListViewHolder(view)
            return holder
        }

        override fun onBindViewHolder(
            holder: SelectLogActivity.RecyclerListViewHolder,
            position: Int
        ) {
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