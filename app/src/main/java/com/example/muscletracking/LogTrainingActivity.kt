package com.example.muscletracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LogTrainingActivity : AppCompatActivity() {
    private val muscleGroups = mutableListOf(
        "Chest",
        "Back",
        "Leg",
        "Arm",
        "Shoulder"
    )

    private var chestTrainingList = mutableListOf(
        "chest",
        "aaaa",
        "bbbb",
        "cccc"
    )

    private var backTrainingList = mutableListOf(
        "back",
        "dddd",
        "eeee",
        "ffff"
    )

    private var legTrainingList = mutableListOf(
        "leg",
        "gggg",
        "hhhh",
        "iiii"
    )

    private var armTrainingList = mutableListOf(
        "arm",
        "jjjj",
        "kkkk",
        "llll"
    )

    private var shoulderTrainingList = mutableListOf(
        "shoulder",
        "mmmm",
        "nnnn",
        "oooo"
    )
    val globalApplication = GlobalApplication.getInstance()
    private val addTrainingMenus: MutableList<MutableMap<String, Any>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_training)


        // set spinner of muscle groups
        val spinnerMuscleGroups = findViewById<Spinner>(R.id.spinnerMuscleGroups)
        val adapterMuscleGroups =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, muscleGroups)
        adapterMuscleGroups.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerMuscleGroups.adapter = adapterMuscleGroups
        spinnerMuscleGroups.onItemSelectedListener = spinnerSelectedMuscleGroupsListener()


        // set spinner of training menus
        val spinnerTrainingMenus = findViewById<Spinner>(R.id.spinnerTrainingMenus)
        val adapterTrainingMenus =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, chestTrainingList)
        adapterTrainingMenus.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerTrainingMenus.adapter = adapterTrainingMenus

        //set recycleview of training menus
        val addTrainingContainer = findViewById<RecyclerView>(R.id.addTrainingContainer)
        val layout = LinearLayoutManager(this@LogTrainingActivity)
        addTrainingContainer.layoutManager = layout
        val adapter = RecyclerListAdapter(addTrainingMenus)
        addTrainingContainer.adapter = adapter
        val decorator = DividerItemDecoration(this@LogTrainingActivity, layout.orientation)
        addTrainingContainer.addItemDecoration(decorator)


        // set listener of add button
        val addButton = findViewById<Button>(R.id.btAddLog)
        addButton.setOnClickListener {
            val inputMuscleGroups =
                findViewById<Spinner>(R.id.spinnerMuscleGroups).selectedItem.toString()
            val inputTrainingMenu =
                findViewById<Spinner>(R.id.spinnerTrainingMenus).selectedItem.toString()
            val inputTrainingWeight =
                findViewById<TextView>(R.id.inputTrainingWeight).text.toString()
            val inputTrainingCount = findViewById<TextView>(R.id.inputTrainingCount).text.toString()

            if (inputTrainingWeight.isNotEmpty() and inputTrainingCount.isNotEmpty()) {
                var menu = mutableMapOf<String, Any>(
                    "parts" to inputMuscleGroups,
                    "menu" to inputTrainingMenu,
                    "weight" to inputTrainingWeight,
                    "count" to inputTrainingCount
                )
                addTrainingMenus.add(menu)
                adapter.notifyItemInserted(addTrainingMenus.lastIndex)
            }
        }

        // set listener of register button
        val registerButton = findViewById<Button>(R.id.btRegisterLog)
        registerButton.setOnClickListener {
            if (addTrainingMenus.size > 0) {
                for (menu in addTrainingMenus) {
                    globalApplication._addTrainingMenu.add(menu)
                }
                addTrainingMenus.clear()
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Registerd!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private inner class spinnerSelectedMuscleGroupsListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val spinner = parent as Spinner
            val selectedMuscleGroups = spinner!!.selectedItem.toString()

            var trainingList = when (selectedMuscleGroups) {
                "Chest" -> chestTrainingList
                "Back" -> backTrainingList
                "Leg" -> legTrainingList
                "Arm" -> armTrainingList
                "Shoulder" -> shoulderTrainingList
                else -> chestTrainingList
            }

            val spinnerTrainingMenus = findViewById<Spinner>(R.id.spinnerTrainingMenus)
            val adapterTrainingMenus =
                ArrayAdapter(
                    this@LogTrainingActivity,
                    android.R.layout.simple_spinner_item,
                    trainingList
                )
            adapterTrainingMenus.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spinnerTrainingMenus.adapter = adapterTrainingMenus
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
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
            val inflater = LayoutInflater.from(this@LogTrainingActivity)
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