package com.example.muscletracking.view.home.log

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R
import com.example.muscletracking.model.menu.Menu
import com.example.muscletracking.viewmodel.menu.MenuViewModel

class TrainingMenuListFragment : Fragment() {

    private val args: TrainingMenuListFragmentArgs by navArgs()

    private val menuViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity!!.application)
        ).get(
            MenuViewModel::class.java
        )
    }

    private var menuList = mutableListOf<Menu>()
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_training_menu_list, container, false)

        menuViewModel.addedMenuList.observe(this, Observer {

            for (menu in it) {
                val addMenu = Menu(menu.menuId,menu.menuName,menu.musclePart)
                menuViewModel.insertMenu(addMenu)
            }

            menuViewModel.getAllMenuByMusclePartFromDB(args.musclePart)
        })

        val btAddMenu = view.findViewById<Button>(R.id.btAddMenu)
        btAddMenu.setOnClickListener {
            val myedit = EditText(activity)
            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle("メニューを入力してください")
            dialog.setView(myedit)
            dialog.setPositiveButton("追加", DialogInterface.OnClickListener { _, _ ->
                val input = myedit.text.toString()
                menuViewModel.addMenu(args.musclePartId, input)
                Toast.makeText(activity, "$input と入力しました", Toast.LENGTH_SHORT).show()
            })
            dialog.setNegativeButton("キャンセル", null)
            dialog.show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuViewModel.getAllMenuByMusclePartFromDB(args.musclePart)
        menuViewModel.menuListByPartOfDB.observe(this, Observer {
            this.recyclerView = view.findViewById(R.id.rvTrainingMenu)
            this.recyclerView?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                adapter = TrainingMenuListAdapter(
                    generateList(it),
                    object : TrainingMenuListAdapter.ListListener {
                        override fun onClickItem(tappedView: View, menu: Menu) {
                            // findNavController().navigate(R.id.action_trainingMenuListFragment_to_logFragment)
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

    private fun generateList(menus: List<Menu>): List<Menu> {
        menuList = mutableListOf<Menu>()
        for (menu in menus) {
            menuList.add(menu)
        }
        return menuList
    }


    companion object {
        @JvmStatic
        fun newInstance(): TrainingMenuListFragment {
            return TrainingMenuListFragment()
        }


    }
}