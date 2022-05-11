package com.example.muscletracking.view.home.top

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarView
import com.example.muscletracking.view.home.logdetail.LogDetailActivity
import com.example.muscletracking.R
import com.example.muscletracking.model.log.Log
import com.example.muscletracking.viewmodel.log.LogViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
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
    private lateinit var noDataFoundText : TextView

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data.let { data: Intent? ->
                    val date = data?.getStringExtra("trainingDate")
                    logViewModel.getLogByDate(date!!)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        noDataFoundText = view.findViewById(R.id.tvNoLogDataFound)
        noDataFoundText.visibility = TextView.INVISIBLE

        // ログ履歴用カレンダーの設定
        val cv = view.findViewById<CalendarView>(R.id.cvForLogByDate)

        // ログ有り日付のハイライト処理
        val trainingDates = mutableListOf<Calendar>()
        val calendarDate = Calendar.getInstance()
        calendarDate.set(2022, 3, 17)
        trainingDates.add(calendarDate)
        cv.setHighlightedDays(trainingDates)

        // 日付押下処理
        val text = view.findViewById<TextView>(R.id.tvTodayTraining)
        cv.setOnDayClickListener {
            val selectedDate = it.calendar
            val date = "%04d%02d%02d".format(
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH) + 1,
                selectedDate.get(Calendar.DATE)
            )
            val dateForView = "%02d月%02d日".format(
                selectedDate.get(Calendar.MONTH) + 1,
                selectedDate.get(Calendar.DATE)
            )

            text.text = "$dateForView のトレーニング"
            logViewModel.getLogByDate(date)
        }

        // ログ履歴用RecyclerViewの設定
        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        this.recyclerView = view.findViewById(R.id.rvTodayLog)
        this.recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(dividerItemDecoration)
            adapter = TrainingTodayLogViewAdapter(
                logList,
                object : TrainingTodayLogViewAdapter.ListListener {
                    override fun onClickItem(tappedView: View, log: Log) {
                        val logId =
                            tappedView.findViewById<TextView>(R.id.tvLogIdTodayInvisible).text.toString()

                        // ログ詳細画面へ遷移
                        val intent = Intent(activity, LogDetailActivity::class.java)
                        intent.putExtra("logId", logId)
                        startForResult.launch(intent)
                    }
                }
            )
        }

        // Observer登録
        // ログ日付検索時
        logViewModel.logListByDate.observe(this, androidx.lifecycle.Observer {
            noDataFoundText.visibility = TextView.INVISIBLE

            if (it.isEmpty()) {
                noDataFoundText.visibility = TextView.VISIBLE
            }
            generateList(it)
            recyclerView?.adapter?.notifyDataSetChanged()
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noDataFoundText.visibility = TextView.INVISIBLE

        // AppBarのタイトル設定
        val titleTextView = activity!!.findViewById<TextView>(R.id.tvToolBarTitle)
        titleTextView.text = getString(R.string.label_home)

        // 本日日付のログ検索
        val dateLogText = view.findViewById<TextView>(R.id.tvTodayTraining)
        val sdf = SimpleDateFormat("yyyyMMdd")
        val sdfForView = SimpleDateFormat("MM月dd日")
        val today = sdf.format(Date(System.currentTimeMillis()))
        dateLogText.text = "${sdfForView.format(Date(System.currentTimeMillis()))} のトレーニング"
        logViewModel.getLogByDate(today)
    }

    private fun generateList(logs: List<Log>) {
        logList.clear()
        for (log in logs) {
            logList.add(log)
        }
    }
}