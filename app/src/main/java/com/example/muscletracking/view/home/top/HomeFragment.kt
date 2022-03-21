package com.example.muscletracking.view.home.top

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R
import com.example.muscletracking.model.bodycomp.BodyComp
import com.example.muscletracking.model.log.Log
import com.example.muscletracking.view.home.loghistory.TrainingLogViewAdapter
import com.example.muscletracking.viewmodel.bodycomp.BodyCompViewModel
import com.example.muscletracking.viewmodel.log.LogViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToLong

class HomeFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var logList = mutableListOf<Log>()
    private var needsInsertion = false
    private var latestBodyCompInfo: BodyComp? = null

    private val logViewModel: LogViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity!!.application)
        ).get(
            LogViewModel::class.java
        )
    }

    private val bodyCompViewModel: BodyCompViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity!!.application)
        ).get(
            BodyCompViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val userNameView = view.findViewById<TextView>(R.id.tvUserNameInput)
        val userHeightView = view.findViewById<TextView>(R.id.tvUserHeightInput)
        val userWeightView = view.findViewById<TextView>(R.id.tvUserWeightInput)
        val userBmiView = view.findViewById<TextView>(R.id.tvUserBmiInput)
        val userBfpView = view.findViewById<TextView>(R.id.tvUserBfpInput)
        val userLbmView = view.findViewById<TextView>(R.id.tvUserLbmInput)

        bodyCompViewModel.getLatestBodyCompOfDb()
        bodyCompViewModel.latestBodyComp.observe(this, androidx.lifecycle.Observer {
            android.util.Log.d("debug", it.toString())
            if (it == null) {
                needsInsertion = true
                userNameView.text = "ryo"
                userHeightView.text = "--"
                userWeightView.text = "--"
                userBmiView.text = "--"
                userBfpView.text = "--"
                userLbmView.text = "--"
            } else {
                needsInsertion = false
                latestBodyCompInfo = it
                userNameView.text = "ryo"
                userHeightView.text = it.height.toString()
                userWeightView.text = it.weight.toString()
                userBmiView.text = it.bmi.toString()
                userBfpView.text = it.bfp.toString()
                userLbmView.text = it.lbm.toString()
            }
        })

        val llBodyCompContainer = view.findViewById<LinearLayout>(R.id.llBodyCompContainer)
        llBodyCompContainer.setOnClickListener {
            val dialogContent =
                LayoutInflater.from(activity).inflate(R.layout.item_input_bodycomp, null)

            val modifyHeightContainer = dialogContent.findViewById<EditText>(R.id.tvModifyHeight)
            modifyHeightContainer.setText(userHeightView.text.toString())

            val modifyWeightContainer = dialogContent.findViewById<EditText>(R.id.tvModifyWeight)
            modifyWeightContainer.setText(userWeightView.text.toString())

            val modifyBfpContainer = dialogContent.findViewById<EditText>(R.id.tvModifyBfp)
            modifyBfpContainer.setText(userBfpView.text.toString())

            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle("体組成修正")
            dialog.setPositiveButton(
                "修正",
                DialogInterface.OnClickListener { _, _ ->
                    val modifyHeightValue = modifyHeightContainer.text.toString().toDouble()
                    val modifyWeightValue = modifyWeightContainer.text.toString().toDouble()
                    val modifyBfpValue = modifyBfpContainer.text.toString().toDouble()

                    userHeightView.text = modifyHeightValue.toString()
                    userWeightView.text = modifyWeightValue.toString()
                    userBmiView.text = calculateBmi(modifyHeightValue, modifyWeightValue).toString()
                    userBfpView.text = modifyBfpValue.toString()
                    userLbmView.text = calculateLbm(modifyWeightValue, modifyBfpValue).toString()

                    if (needsInsertion) {
                        bodyCompViewModel.insertBodyComp(
                            modifyHeightValue,
                            modifyWeightValue,
                            modifyBfpValue,
                            latestBodyCompInfo!!.bodyCompDate,
                            "ryio1010"
                        )
                    } else {
                        bodyCompViewModel.updateBodyComp(
                            latestBodyCompInfo!!.bodyCompId,
                            modifyHeightValue,
                            modifyWeightValue,
                            modifyBfpValue,
                            "ryio1010"
                        )
                    }
                }
            )
            dialog.setNegativeButton("戻る", null)
            dialog.setView(dialogContent)
            dialog.show()
        }
        bodyCompViewModel.insertedBodyComp.observe(this, androidx.lifecycle.Observer {
            val insertBodyComp = BodyComp(
                it.bodyCompId,
                it.height,
                it.weight,
                it.bfp,
                calculateBmi(it.height, it.weight),
                calculateLbm(it.weight, it.bfp),
                it.bodyCompDate
            )
            bodyCompViewModel.insertBodyCompDb(insertBodyComp)
        })

        bodyCompViewModel.updatedBodyComp.observe(this, androidx.lifecycle.Observer {
            val updateBodyComp = BodyComp(
                it.bodyCompId,
                it.height,
                it.weight,
                it.bfp,
                calculateBmi(it.height, it.weight),
                calculateLbm(it.weight, it.bfp),
                it.bodyCompDate
            )
            bodyCompViewModel.updateBodyCompDb(updateBodyComp)
        })


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("yyyyMMdd")
        val today = sdf.format(Date(System.currentTimeMillis()))
        logViewModel.getTodayLog(today)
        logViewModel.todayLogList.observe(this, androidx.lifecycle.Observer {
            android.util.Log.d("debug", it.toString())
            val dividerItemDecoration =
                DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            this.recyclerView = view.findViewById(R.id.rvTodayLog)
            this.recyclerView?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                addItemDecoration(dividerItemDecoration)
                adapter = TrainingLogViewAdapter(
                    generateList(it),
                    object : TrainingLogViewAdapter.ListListener {
                        override fun onClickItem(tappedView: View, log: Log) {
                        }
                    }
                )
            }
        })

    }

    private fun generateList(logs: List<Log>): List<Log> {
        logList = mutableListOf<Log>()
        for (log in logs) {
            logList.add(log)
        }
        return logList
    }

    private fun calculateBmi(height: Double, weight: Double): Double {
        return (weight / (height / 100 * (height / 100)) * 100.0).roundToLong() / 100.0
    }

    private fun calculateLbm(weight: Double, bfp: Double): Double {
        return (weight * (100 - bfp) / 100 * 100.0).roundToLong() / 100.0
    }
}