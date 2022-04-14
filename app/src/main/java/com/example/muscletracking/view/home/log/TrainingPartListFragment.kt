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

    // ホーム画面は今日の体組成とトレーニングを乗せる
    // ログ追加画面は別Activityに変更
    // ログ追加画面の代わりに体組成追加画面を追加
    // 履歴画面で日付検索できるようにする

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

//        val titleTextView = activity!!.findViewById<TextView>(R.id.tvToolBarTitle)
//        titleTextView.text = getString(R.string.label_training_muscle_groups)

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
                                tappedView.findViewById<TextView>(R.id.tvTrainingPartId).text.toString()

                            val musclePart =
                                tappedView.findViewById<TextView>(R.id.tvTrainingPartName).text.toString()

                            val transaction = parentFragmentManager.beginTransaction()

                            val fragment = TrainingMenuListFragment()
                            val bundle = Bundle()
                            bundle.putString("musclePartId", musclePartId)
                            bundle.putString("musclePart", musclePart)
                            fragment.arguments = bundle

                            transaction.replace(R.id.fcvTrainingMenuSelect, fragment)
                            transaction.commit()

//                            val action =
//                                TrainingPartListFragmentDirections.actionTrainingPartListFragmentToTrainingMenuListFragment(
//                                    musclePartId, musclePart
//                                )
//                            findNavController().navigate(action)
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
}