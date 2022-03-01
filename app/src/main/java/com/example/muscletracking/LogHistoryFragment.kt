package com.example.muscletracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R
import com.example.muscletracking.model.log.LogResponse
import com.example.muscletracking.viewmodel.log.LogViewModel
import com.example.muscletracking.viewmodel.musclepart.MusclePartViewModel

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_log_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logViewModel.getAllLog("ryio1010")
        logViewModel.logList.observe(this, Observer {
            this.recyclerView = view.findViewById(R.id.rvTrainingLog)
            this.recyclerView?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                adapter = TrainingLogViewAdapter(
                    generateList(it),
                    object : TrainingLogViewAdapter.ListListener {
                        override fun onClickItem(tappedView: View, logResponse: LogResponse) {
                            onClickItem(tappedView,logResponse)
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

    private fun generateList(logResponses: List<LogResponse>): List<LogResponse> {
        val list = mutableListOf<LogResponse>()
        for(logResponse in logResponses) {
            list.add(logResponse)
        }
        return list
    }

    private fun onClickedItem(tappedView:View,logResponse:LogResponse)  {

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogHistoryFragment().apply {
            }
    }
}