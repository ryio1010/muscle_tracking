package com.example.muscletracking.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.muscletracking.R
import com.example.muscletracking.RegisterUserCompleteActivity
import com.example.muscletracking.databinding.ActivityUserRegisterBinding
import com.example.muscletracking.viewmodel.user.UserRegisterViewModel
import okhttp3.*
import java.io.IOException

class UserRegisterActivity : AppCompatActivity() {
    private val userRegisterViewModel: UserRegisterViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(UserRegisterViewModel::class.java)
    }
    private lateinit var activityUserRegisterBinding: ActivityUserRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // databinding登録
        activityUserRegisterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_user_register)
        activityUserRegisterBinding.also {
            it.registerViewModel = userRegisterViewModel
            it.lifecycleOwner = this
        }

        // observer登録
        userRegisterViewModel.enableRegisterFlag.observe(this) {
            activityUserRegisterBinding.btRegisterUser.isEnabled = it
        }

        // listener登録
        activityUserRegisterBinding.btRegisterUser.setOnClickListener(RegisterUserButtonListener())
    }

    private inner class RegisterUserButtonListener : View.OnClickListener {
        override fun onClick(view: View?) {
            // 入力情報の取得
            val registerUserIdComponent = findViewById<TextView>(R.id.inputRegisterUserId)
            val registerUserPwComponent = findViewById<TextView>(R.id.inputRegisterUserPw)
            val registerUserId = registerUserIdComponent.text.toString()
            val registerUserPw = registerUserPwComponent.text.toString()

            // api call
            // 入力項目をパラメータにユーザーをDBに登録するAPIを実行


            val intent =
                Intent(this@UserRegisterActivity, RegisterUserCompleteActivity::class.java)
            startActivity(intent)

        }
    }
}