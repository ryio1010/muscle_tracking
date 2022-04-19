package com.example.muscletracking.view.home.log

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muscletracking.R
import com.example.muscletracking.model.menu.Menu
import com.example.muscletracking.viewmodel.menu.MenuViewModel

class TrainingMenuListFragment : Fragment() {

    //private val args: TrainingMenuListFragmentArgs by navArgs()

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


        // メニュー登録ボタン押下処理
        val btAddMenu = view.findViewById<Button>(R.id.btAddMenu)
        btAddMenu.setOnClickListener {
            val myedit = EditText(activity)
            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle(R.string.txt_select_menu_add)
            dialog.setView(myedit)

            // positivebutton押下処理
            dialog.setPositiveButton(
                R.string.bt_dialog_add_menu,
                DialogInterface.OnClickListener { _, _ ->
                    // メニュー追加API実行
                    val musclePartId = arguments?.getString("musclePartId")
                    val input = myedit.text.toString()
                    menuViewModel.addMenu(
                        musclePartId!!,
                        input,
//                        (activity as HomeActivity).mUser!!.userId
                        "ryio1010"
                    )
                })

            // negativebutton押下処理
            dialog.setNegativeButton(R.string.bt_dialog_cancel, null)
            dialog.show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView = activity!!.findViewById<TextView>(R.id.tvToolBarTitle)
        val leftButton = activity!!.findViewById<ImageButton>(R.id.ibToolBarLeft)
        titleTextView.text = getString(R.string.label_training_menus)
        leftButton.setOnClickListener {
            parentFragmentManager.popBackStack("menuListFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        // observer登録

        // メニュー追加API実行時
        menuViewModel.addedMenu.observe(this, Observer {
            val menu = Menu(it.menuId, it.menuName, it.musclePartName)
            menuViewModel.insertMenu(menu)

            menuList.add(menu)
            recyclerView?.adapter?.notifyDataSetChanged()
        })
        // ローカルDBメニュー全件取得時（トレーニング部位別）
        menuViewModel.menuListByPartOfDB.observe(this, Observer {
            if (menuList.isEmpty()) {
                // 初回
                Log.d("debug", "メニュー初回表示時の処理です")
                this.recyclerView = view.findViewById(R.id.rvTrainingMenu)
                this.recyclerView?.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    itemAnimator = DefaultItemAnimator()
                    adapter = TrainingMenuListAdapter(
                        generateList(it),
                        object : TrainingMenuListAdapter.ListListener {
                            override fun onClickItem(tappedView: View, menu: Menu) {
                                val selectedMenuId =
                                    tappedView.findViewById<TextView>(R.id.tvTrainingMenuId).text.toString()
                                val selectedMenu =
                                    tappedView.findViewById<TextView>(R.id.tvTrainingMenu).text.toString()

                                val intent = Intent()
                                intent.putExtra("selectedMenuId", selectedMenuId)
                                intent.putExtra("selectedMenu", selectedMenu)
                                activity?.setResult(Activity.RESULT_OK, intent)
                                activity?.finish()
                            }
                        }
                    )
                }
            }
        })

        // トレーニング部位別メニュー取得
        val musclePart = arguments?.getString("musclePart")
        menuViewModel.getAllMenuByMusclePartFromDB(musclePart!!)

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
}