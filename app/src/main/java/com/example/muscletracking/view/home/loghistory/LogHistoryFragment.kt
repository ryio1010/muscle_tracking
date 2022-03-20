package com.example.muscletracking.view.home.loghistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R
import com.example.muscletracking.model.log.Log
import com.example.muscletracking.viewmodel.log.LogViewModel
import kotlin.math.log10

class LogHistoryFragment : Fragment() {

    private val logViewModel: LogViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity!!.application)
        ).get(
            LogViewModel::class.java
        )
    }

    private var recyclerView: RecyclerView? = null
    private var logList = mutableListOf<Log>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_log_history, container, false)
        val tvError = view.findViewById<TextView>(R.id.tvNoResult)
        tvError.visibility = TextView.INVISIBLE

        // observer登録
        // トレーニングメニュー検索ボタン押下時
        logViewModel.logListByMenu.observe(this, Observer {

            tvError.visibility = TextView.INVISIBLE

            logList.clear()

            if (it.isEmpty()) {
                // 検索結果なし
                tvError.setText(R.string.txt_no_find_log_history)
                tvError.visibility = TextView.VISIBLE
            } else {

                for (log in it) {
                    logList.add(log)
                }
            }
            recyclerView?.adapter?.notifyDataSetChanged()
        })

        // listener登録
        // 検索ボタン押下処理
        val btSearchLog = view.findViewById<Button>(R.id.btSearchLog)
        btSearchLog.setOnClickListener {
            val searchTrainingMenu =
                view.findViewById<EditText>(R.id.etSearchTrainingMenu).text.toString()
            logViewModel.getLogByMenu(searchTrainingMenu)
        }

        // クリアボタン押下時
        val btClearLog = view.findViewById<Button>(R.id.btClear)
        btClearLog.setOnClickListener {
            tvError.visibility = TextView.INVISIBLE

            val inputView = view.findViewById<EditText>(R.id.etSearchTrainingMenu)
            inputView.setText("")
            logViewModel.getAllLogFromDB()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logViewModel.getAllLogFromDB()
        logViewModel.logListOfDB.observe(this, Observer {
            val dividerItemDecoration =
                DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            this.recyclerView = view.findViewById(R.id.rvTrainingLog)
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

    override fun onDestroyView() {
        super.onDestroyView()
        this.recyclerView?.adapter = null
        this.recyclerView = null
    }

    private fun generateList(logs: List<Log>): List<Log> {
        logList = mutableListOf<Log>()
        for (log in logs) {
            logList.add(log)
        }
        return logList
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogHistoryFragment().apply {
            }
    }
}