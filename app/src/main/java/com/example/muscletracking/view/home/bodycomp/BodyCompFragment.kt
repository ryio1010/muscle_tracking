package com.example.muscletracking.view.home.bodycomp

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.applandeo.materialcalendarview.CalendarView
import com.example.muscletracking.ProcessDialogFragment
import com.example.muscletracking.R
import com.example.muscletracking.common.ApiResult
import com.example.muscletracking.model.bodycomp.BodyComp
import com.example.muscletracking.view.home.HomeActivity
import com.example.muscletracking.viewmodel.bodycomp.BodyCompViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToLong

class BodyCompFragment : Fragment() {
    private val bodyCompViewModel: BodyCompViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity!!.application)
        ).get(
            BodyCompViewModel::class.java
        )
    }

    private var needsInsertion = false
    private var latestBodyCompInfo: BodyComp? = null

    private lateinit var dateForApi: String
    private lateinit var dateForView: String

    private lateinit var userHeightView: TextView
    private lateinit var userWeightView: TextView
    private lateinit var userBmiView: TextView
    private lateinit var userBfpView: TextView
    private lateinit var userLbmView: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_body_comp, container, false)

        // 画面要素取得
        userHeightView = view.findViewById(R.id.tvUserHeightInput)
        userWeightView = view.findViewById(R.id.tvUserWeightInput)
        userBmiView = view.findViewById(R.id.tvUserBmiInput)
        userBfpView = view.findViewById(R.id.tvUserBfpInput)
        userLbmView = view.findViewById(R.id.tvUserLbmInput)

        // ログ履歴用カレンダーの設定
        val cv = view.findViewById<CalendarView>(R.id.cvForBodyCompByDate)
        // 日付押下処理
        val text = view.findViewById<TextView>(R.id.tvDayBodyComp)
        cv.setOnDayClickListener {
            val selectedDate = it.calendar
            dateForApi = "%04d%02d%02d".format(
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH) + 1,
                selectedDate.get(Calendar.DATE)
            )
            dateForView = "%02d月%02d日".format(
                selectedDate.get(Calendar.MONTH) + 1,
                selectedDate.get(Calendar.DATE)
            )

            text.text = "$dateForView の体組成データ"
            bodyCompViewModel.getBodyCompByDateOfDb(dateForApi)
        }


        // 体組成データ押下処理
        val llBodyCompContainer = view.findViewById<LinearLayout>(R.id.llBodyCompContainer)
        llBodyCompContainer.setOnClickListener {
            createModifyDialog()
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
            latestBodyCompInfo = insertBodyComp
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

        // 日付体組成データ取得時の処理
        bodyCompViewModel.bodyCompByDate.observe(this, androidx.lifecycle.Observer {
            if (it == null) {
                needsInsertion = true
                latestBodyCompInfo = null
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // AppBarのタイトル設定
        val titleTextView = activity!!.findViewById<TextView>(R.id.tvToolBarTitle)
        titleTextView.text = getString(R.string.label_body_comp)

        // 本日日付の体組成データ検索
        val text = view.findViewById<TextView>(R.id.tvDayBodyComp)
        val sdf = SimpleDateFormat("yyyyMMdd")
        val sdfForView = SimpleDateFormat("MM月dd日")

        dateForApi = sdf.format(Date(System.currentTimeMillis()))
        dateForView = sdfForView.format(Date(System.currentTimeMillis()))

        text.text = "$dateForView の体組成データ"
        bodyCompViewModel.getBodyCompByDateOfDb(dateForApi)
    }

    private fun createModifyDialog() {
        val dialogContent =
            LayoutInflater.from(activity).inflate(R.layout.item_input_bodycomp, null)

        val modifyHeightContainer = dialogContent.findViewById<EditText>(R.id.tvModifyHeight)
        val modifyWeightContainer = dialogContent.findViewById<EditText>(R.id.tvModifyWeight)
        val modifyBfpContainer = dialogContent.findViewById<EditText>(R.id.tvModifyBfp)

        if (latestBodyCompInfo != null) {
            modifyHeightContainer.setText(userHeightView.text.toString())
            modifyWeightContainer.setText(userWeightView.text.toString())
            modifyBfpContainer.setText(userBfpView.text.toString())
        }

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
                        dateForApi,
                        (activity as HomeActivity).mUser!!.userId
                    )
                    needsInsertion = false
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