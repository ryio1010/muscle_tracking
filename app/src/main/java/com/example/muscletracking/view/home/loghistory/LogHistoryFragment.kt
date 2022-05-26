package com.example.muscletracking.view.home.loghistory

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.view.home.HomeActivity
import com.example.muscletracking.view.home.logdetail.LogDetailActivity
import com.example.muscletracking.R
import com.example.muscletracking.model.log.Log
import com.example.muscletracking.viewmodel.log.LogViewModel

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
    private var trainingLogList = mutableListOf<Log>()

    private lateinit var searchTrainingMenuContainer: TextView
    private lateinit var tvError : TextView

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data.let { data: Intent? ->
                    if (searchTrainingMenuContainer.text.toString().isNotEmpty()) {
                        logViewModel.getLogByMenu(searchTrainingMenuContainer.text.toString())
                    } else {
                        logViewModel.getAllLogFromDB()
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_log_history, container, false)
        tvError = view.findViewById<TextView>(R.id.tvNoResult)
        searchTrainingMenuContainer = view.findViewById<EditText>(R.id.etSearchTrainingMenu)
        tvError.visibility = TextView.INVISIBLE

        // observer登録
        // トレーニングメニュー検索ボタン押下時
        logViewModel.logListByMenu.observe(this, Observer {

            tvError.visibility = TextView.INVISIBLE
            if (it.isEmpty()) {
                // 検索結果なし
                trainingLogList.clear()
                tvError.setText(R.string.txt_no_find_log_history)
                tvError.visibility = TextView.VISIBLE
            } else {
                generateTrainingList(it)
            }
            recyclerView?.adapter?.notifyDataSetChanged()
        })

        // listener登録
        // 検索ボタン押下処理
        val btSearchLog = view.findViewById<Button>(R.id.btSearchLog)
        btSearchLog.setOnClickListener {
            (activity as HomeActivity).hideKeyboard(it)
            val searchTrainingMenu = searchTrainingMenuContainer.text.toString()
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

        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        this.recyclerView = view.findViewById(R.id.rvTrainingLog)
        this.recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(dividerItemDecoration)
            adapter = TrainingLogViewAdapter(
                trainingLogList,
                object : TrainingLogViewAdapter.ListListener {
                    override fun onClickItem(tappedView: View, log: Log) {
                        val logId =
                            tappedView.findViewById<TextView>(R.id.tvLogIdInvisible).text.toString()

                        // ログ詳細画面へ遷移
                        val intent = Intent(activity, LogDetailActivity::class.java)
                        intent.putExtra("logId", logId)
                        startForResult.launch(intent)
                    }
                }
            )
        }

        logViewModel.logListOfDB.observe(this, Observer {
            generateTrainingList(it)
            recyclerView?.adapter?.notifyDataSetChanged()
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // エラー文言の初期化
        tvError.text = ""
        tvError.visibility = TextView.INVISIBLE

        val titleTextView = activity!!.findViewById<TextView>(R.id.tvToolBarTitle)
        titleTextView.text = getString(R.string.label_log_history)

        logViewModel.getAllLogFromDB()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.recyclerView?.adapter = null
        this.recyclerView = null
    }

    private fun generateTrainingList(logs: List<Log>) {
        trainingLogList.clear()
        for (log in logs) {
            trainingLogList.add(log)
        }
    }
}