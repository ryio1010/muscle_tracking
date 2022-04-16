package com.example.muscletracking.view.home.top

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.muscletracking.view.home.HomeActivity
import com.example.muscletracking.view.home.logdetail.LogDetailActivity
import com.example.muscletracking.R
import com.example.muscletracking.model.bodycomp.BodyComp
import com.example.muscletracking.model.log.Log
import com.example.muscletracking.viewmodel.bodycomp.BodyCompViewModel
import com.example.muscletracking.viewmodel.log.LogViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToLong

class HomeFragment : Fragment() {
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

    private var recyclerView: RecyclerView? = null
    private var logList = mutableListOf<Log>()
    private var needsInsertion = false
    private var latestBodyCompInfo: BodyComp? = null

    private lateinit var userHeightView: TextView
    private lateinit var userWeightView: TextView
    private lateinit var userBmiView: TextView
    private lateinit var userBfpView: TextView
    private lateinit var userLbmView: TextView

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

        // 画面要素取得
        userHeightView = view.findViewById<TextView>(R.id.tvUserHeightInput)
        userWeightView = view.findViewById<TextView>(R.id.tvUserWeightInput)
        userBmiView = view.findViewById<TextView>(R.id.tvUserBmiInput)
        userBfpView = view.findViewById<TextView>(R.id.tvUserBfpInput)
        userLbmView = view.findViewById<TextView>(R.id.tvUserLbmInput)

        // 体組成データ設定
        bodyCompViewModel.getLatestBodyCompOfDb()
        bodyCompViewModel.latestBodyComp.observe(this, androidx.lifecycle.Observer {
            if (it == null) {
                needsInsertion = true
                setBodyComp("--", "--", "--", "--", "--")
            } else {
                needsInsertion = false
                latestBodyCompInfo = it
                setBodyComp(
                    it.height.toString(),
                    it.weight.toString(),
                    it.bmi.toString(),
                    it.bfp.toString(),
                    it.lbm.toString()
                )
            }
        })

        // 体組成データ押下処理
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
                            (activity as HomeActivity).mUser!!.userId
                        )
                    } else {
                        bodyCompViewModel.updateBodyComp(
                            latestBodyCompInfo!!.bodyCompId,
                            modifyHeightValue,
                            modifyWeightValue,
                            modifyBfpValue,
                            (activity as HomeActivity).mUser!!.userId
                        )
                    }
                }
            )
            dialog.setNegativeButton("戻る", null)
            dialog.setView(dialogContent)
            dialog.show()
        }
        // 体組成データInsertAPI実行後の処理
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

        // 体組成データUpdateAPI実行後の処理
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

        // ログ履歴用カレンダーの設定
        val cv = view.findViewById<CalendarView>(R.id.cvForLogByDate)
        val text = view.findViewById<TextView>(R.id.tvTodayTraining)
        cv.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val date = "%04d%02d%02d".format(year, month + 1, dayOfMonth)
            val dateForView = "%02d月%02d日".format(month + 1, dayOfMonth)
//            val bundle = Bundle()
//            bundle.putString("trainingDate", date)
//            findNavController().navigate(R.id.action_homeFragment_to_logHistoryFragment, bundle)
            text.text = "$dateForView のトレーニング"
            logViewModel.getLogByDate(date)
        }

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
//                        startActivity(intent)
                        startForResult.launch(intent)
                    }
                }
            )
        }

        logViewModel.logListByDate.observe(this, androidx.lifecycle.Observer {
            generateList(it)
            recyclerView?.adapter?.notifyDataSetChanged()
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView = activity!!.findViewById<TextView>(R.id.tvToolBarTitle)
        titleTextView.text = getString(R.string.label_home)

        val sdf = SimpleDateFormat("yyyyMMdd")
        val today = sdf.format(Date(System.currentTimeMillis()))
        logViewModel.getLogByDate(today)
    }

    private fun generateList(logs: List<Log>) {
        logList.clear()
        for (log in logs) {
            logList.add(log)
        }
    }

    private fun setBodyComp(height: String, weight: String, bmi: String, bfp: String, lbm: String) {
        userHeightView.text = height
        userWeightView.text = weight
        userBmiView.text = bmi
        userBfpView.text = bfp
        userLbmView.text = lbm
    }

    private fun calculateBmi(height: Double, weight: Double): Double {
        return (weight / (height / 100 * (height / 100)) * 100.0).roundToLong() / 100.0
    }

    private fun calculateLbm(weight: Double, bfp: Double): Double {
        return (weight * (100 - bfp) / 100 * 100.0).roundToLong() / 100.0
    }
}