package com.example.muscletracking.view.home.bodycomp

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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
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
        userHeightView = view.findViewById<TextView>(R.id.tvUserHeightInput)
        userWeightView = view.findViewById<TextView>(R.id.tvUserWeightInput)
        userBmiView = view.findViewById<TextView>(R.id.tvUserBmiInput)
        userBfpView = view.findViewById<TextView>(R.id.tvUserBfpInput)
        userLbmView = view.findViewById<TextView>(R.id.tvUserLbmInput)

        // 体組成データ設定
//        bodyCompViewModel.getLatestBodyCompOfDb()
//        bodyCompViewModel.latestBodyComp.observe(this, androidx.lifecycle.Observer {
//            if (it == null) {
//                needsInsertion = true
//                setBodyComp("--", "--", "--", "--", "--")
//            } else {
//                needsInsertion = false
//                latestBodyCompInfo = it
//                setBodyComp(
//                    it.height.toString(),
//                    it.weight.toString(),
//                    it.bmi.toString(),
//                    it.bfp.toString(),
//                    it.lbm.toString()
//                )
//            }
//        })

        bodyCompViewModel.getLatestBodyComp("ryio1010")
        bodyCompViewModel.latestBodyComp2.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is ApiResult.Proceeding -> {
                    showProgressDialog()
                }
                is ApiResult.Success -> {
                    hideProgressDialog()
                    Toast.makeText(activity, "体組成データ取得に成功！", Toast.LENGTH_SHORT).show()
                    needsInsertion = false

                    latestBodyCompInfo = BodyComp(
                        it.value.bodyCompId,
                        it.value.height,
                        it.value.weight,
                        it.value.bfp,
                        it.value.bmi,
                        it.value.lbm,
                        it.value.bodyCompDate
                    )
                    setBodyComp(
                        it.value.height.toString(),
                        it.value.weight.toString(),
                        it.value.bmi.toString(),
                        it.value.bfp.toString(),
                        it.value.lbm.toString()
                    )
                }
                is ApiResult.Error -> {
                    hideProgressDialog()
                    Toast.makeText(activity, "No Data Found！", Toast.LENGTH_SHORT).show()
                    needsInsertion = true
                    setBodyComp("--", "--", "--", "--", "--")
                }
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView = activity!!.findViewById<TextView>(R.id.tvToolBarTitle)
        titleTextView.text = getString(R.string.label_body_comp)

        val text = view.findViewById<TextView>(R.id.tvDayBodyComp)
        val sdf = SimpleDateFormat("yyyyMMdd")
        val sdfForView = SimpleDateFormat("MM月dd日")
        val today = sdf.format(Date(System.currentTimeMillis()))
        text.text = "${sdfForView.format(Date(System.currentTimeMillis()))} の体組成データ"
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

    private fun showProgressDialog() {
        if (!ProcessDialogFragment.getInstance().isAdded) {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(
                ProcessDialogFragment.getInstance(),
                ProcessDialogFragment::class.java.name
            )
            transaction.commit()
        }
    }

    private fun hideProgressDialog() {
        val processDialogFragment =
            parentFragmentManager.findFragmentByTag(ProcessDialogFragment::class.java.name)
        (processDialogFragment as? DialogFragment)?.dismiss()
    }
}