package com.example.muscletracking.view.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.muscletracking.R
import com.example.muscletracking.viewmodel.user.UserViewModel
import okhttp3.*

class UserRegisterActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            UserViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        // エラー情報Textview
        val tvErrorMessage = findViewById<TextView>(R.id.tvRegisterErrorMessage)

        // observer登録
        userViewModel.isUserRegistered.observe(this, Observer {
            if (it) {
                val intent =
                    Intent(this@UserRegisterActivity, RegisterUserCompleteActivity::class.java)
                startActivity(intent)
            }else {
                tvErrorMessage.setText(R.string.msg_can_not_register)
                tvErrorMessage.visibility = TextView.VISIBLE
            }
        })

        // listener登録
        val registerButton = findViewById<Button>(R.id.btRegisterUser)
        registerButton.setOnClickListener(RegisterUserButtonListener())
    }

    private inner class RegisterUserButtonListener : View.OnClickListener {
        override fun onClick(view: View?) {
            // 入力情報の取得
            val registerUserIdComponent = findViewById<TextView>(R.id.inputRegisterUserId)
            val registerUserNameComponent = findViewById<TextView>(R.id.inputRegisterUserName)
            val registerUserPwComponent = findViewById<TextView>(R.id.inputRegisterUserPw)
            val registerUserId = registerUserIdComponent.text.toString()
            val registerUserName = registerUserNameComponent.text.toString()
            val registerUserPw = registerUserPwComponent.text.toString()

            // エラー情報Textview
            val tvErrorMessage = findViewById<TextView>(R.id.tvRegisterErrorMessage)
            tvErrorMessage.visibility = TextView.INVISIBLE

            when {
                registerUserId.isEmpty() -> {
                    tvErrorMessage.setText(R.string.msg_no_input_userid)
                    tvErrorMessage.visibility = TextView.VISIBLE
                }
                registerUserName.isEmpty() -> {
                    tvErrorMessage.setText(R.string.msg_no_input_username)
                    tvErrorMessage.visibility = TextView.VISIBLE
                }
                registerUserPw.isEmpty() -> {
                    tvErrorMessage.setText(R.string.msg_no_input_password)
                    tvErrorMessage.visibility = TextView.VISIBLE
                }
                else -> {
                    // api実行
                    userViewModel.register(registerUserId, registerUserName, registerUserPw)
                }
            }
        }
    }
}