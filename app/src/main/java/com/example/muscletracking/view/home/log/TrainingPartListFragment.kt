package com.example.muscletracking.view.home.log

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R
import com.example.muscletracking.model.musclepart.MusclePart
import com.example.muscletracking.viewmodel.musclepart.MusclePartViewModel

class TrainingPartListFragment : Fragment() {

    private val musclePartViewModel: MusclePartViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity!!.application)
        ).get(
            MusclePartViewModel::class.java
        )
    }

    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_training_part_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        musclePartViewModel.getAllMusclePartFromDB()
        musclePartViewModel.musclePartListOfDB.observe(this, Observer {
            this.recyclerView = view.findViewById(R.id.rvTrainingPart)
            this.recyclerView?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                adapter = TrainingPartListAdapter(
                    generateList(it),
                    object : TrainingPartListAdapter.ListListener {
                        override fun onClickItem(tappedView: View, musclePart: MusclePart) {
                            val musclePartId =
                                tappedView.findViewById<TextView>(R.id.tvTrainingPartId).text
                            Log.d("debug", musclePartId.toString())
                            findNavController().navigate(R.id.action_trainingPartListFragment_to_trainingMenuListFragment)
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

    private fun generateList(muscleParts: List<MusclePart>): List<MusclePart> {
        val list = mutableListOf<MusclePart>()
        for (musclePart in muscleParts) {
            list.add(musclePart)
        }
        return list
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TrainingPartListFragment().apply {
            }
    }
}