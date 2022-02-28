package com.example.muscletracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R
import com.example.muscletracking.model.log.LogResponse

class LogHistoryFragment : Fragment() {

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

        this.recyclerView = view.findViewById(R.id.rvTrainingLog)
        this.recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = TrainingLogViewAdapter(
                generateList(),
                object : TrainingLogViewAdapter.ListListener {
                    override fun onClickItem(tappedView: View, logResponse: LogResponse) {
                        onClickItem(tappedView,logResponse)
                    }
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.recyclerView?.adapter = null
        this.recyclerView = null
    }

    private fun generateList(): List<LogResponse> {
        val list = mutableListOf<LogResponse>()
        for(i in 0..10) {
            val logResponse:LogResponse = LogResponse(1,1,90.0,15,"20220301")
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