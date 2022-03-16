package com.example.muscletracking.view.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.muscletracking.HomeActivity
import com.example.muscletracking.R
import com.example.muscletracking.model.user.User
import com.example.muscletracking.viewmodel.user.UserViewModel

class UserInputHeightAndWeightActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            UserViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_input_height_and_weight)

        // エラー情報textview
        val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessageForHeightAndWeight)
        tvErrorMessage.visibility = TextView.INVISIBLE

        // observer登録
        userViewModel.mUserInfoUpdate.observe(this, Observer {
            if (it != null) {
                val userInfo = userViewModel.selectUserById(it.userId)
                if (userInfo == null) {
                    // ローカルDBにユーザー情報を登録
                    val userInfoForDB = User(it.userId, it.userName,it.password)
                    userViewModel.insertUser(userInfoForDB)
                }
                // トップ画面へ遷移
                val intent =
                    Intent(this@UserInputHeightAndWeightActivity, HomeActivity::class.java)
                intent.putExtra("userName", it.userName)
                startActivity(intent)
            }else {
                tvErrorMessage.setText(R.string.msg_can_not_update)
                tvErrorMessage.visibility = TextView.VISIBLE
            }
        })

        val btConfirm = findViewById<Button>(R.id.btHeightAndWeight)
        btConfirm.setOnClickListener(ConfirmClickListener())
    }

    private inner class ConfirmClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val heightInputView = findViewById<TextView>(R.id.inputHeight)
            val weightInputView = findViewById<TextView>(R.id.inputWeight)
            val height = heightInputView.text.toString()
            val weight = weightInputView.text.toString()

            // intentから情報取得
            val userid = intent.getStringExtra("userid").toString()
            val username = intent.getStringExtra("username").toString()

            // エラー情報textview
            val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessageForHeightAndWeight)
            tvErrorMessage.visibility = TextView.INVISIBLE

            when {
                height.isEmpty() -> {
                    tvErrorMessage.setText(R.string.msg_no_input_height)
                    tvErrorMessage.visibility = TextView.VISIBLE
                }
                weight.isEmpty() -> {
                    tvErrorMessage.setText(R.string.msg_no_input_weight)
                    tvErrorMessage.visibility = TextView.VISIBLE
                }
                else -> {
                    // ユーザー情報更新API
                    userViewModel.updateUserInfo(userid, username, username)
                }
            }
        }
    }
}