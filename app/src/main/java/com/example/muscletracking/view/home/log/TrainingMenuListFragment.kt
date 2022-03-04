package com.example.muscletracking.view.home.log

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
import com.example.muscletracking.model.menu.Menu
import com.example.muscletracking.viewmodel.menu.MenuViewModel

class TrainingMenuListFragment : Fragment() {

    private val menuViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity!!.application)
        ).get(
            MenuViewModel::class.java
        )
    }

    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_training_menu_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuViewModel.getAllMenuByMusclePartFromDB(musclePart = "胸")
        menuViewModel.menuListByPartOfDB.observe(this, Observer {
            this.recyclerView = view.findViewById(R.id.rvTrainingMenu)
            this.recyclerView?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                adapter = TrainingMenuListAdapter(
                    generateList(it),
                    object :TrainingMenuListAdapter.ListListener {
                        override fun onClickItem(tappedView: View, menu: Menu) {
                            TODO("Not yet implemented")
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

    private fun generateList(menus:List<Menu>):List<Menu> {
        val list = mutableListOf<Menu>()
        for (menu in menus) {
            list.add(menu)
        }
        return list
    }


    companion object {
        @JvmStatic
        fun newInstance(): TrainingMenuListFragment {
            return TrainingMenuListFragment()
        }


    }
}