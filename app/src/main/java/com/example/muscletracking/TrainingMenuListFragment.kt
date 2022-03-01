package com.example.muscletracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TrainingMenuListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_training_menu_list, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(): TrainingMenuListFragment {
            return TrainingMenuListFragment()
        }


    }
}