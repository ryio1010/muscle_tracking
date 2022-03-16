package com.example.muscletracking.view.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.muscletracking.MainActivity
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

        // TODO:　入力項目のバリデーション追加
        // TODO:　デザイン強化
        // TODO:　ログインAPIのレスポンス確認
        // TODO:　アクティビティ遷移時多重起動させない

        // 新規登録画面へのリンク登録
        val linkText = findViewById<TextView>(R.id.txtToLogin)
        linkText.apply {
            text = createLinkSpannable()
            movementMethod = LinkMovementMethod.getInstance()
        }

        // 登録ボタン押下のリスナー登録
        val registerButton = findViewById<Button>(R.id.btRegisterUser)
        registerButton.setOnClickListener(RegisterUserButtonListener())

        // エラー情報Textview
        val tvErrorMessage = findViewById<TextView>(R.id.tvRegisterErrorMessage)

        // observer登録
        // 新規ユーザー登録API実行時
        userViewModel.isUserRegistered.observe(this, Observer {
            if (it) {
                // ユーザー登録成功
                val intent =
                    Intent(this@UserRegisterActivity, RegisterUserCompleteActivity::class.java)
                startActivity(intent)
            } else {
                // ユーザー登録失敗
                tvErrorMessage.setText(R.string.msg_can_not_register)
                tvErrorMessage.visibility = TextView.VISIBLE
            }
        })
    }

    private inner class RegisterUserButtonListener : View.OnClickListener {
        override fun onClick(view: View?) {
            // 入力情報の取得
            val registerUserId = findViewById<TextView>(R.id.inputRegisterUserId).text.toString()
            val registerUserName =
                findViewById<TextView>(R.id.inputRegisterUserName).text.toString()
            val registerUserPw = findViewById<TextView>(R.id.inputRegisterUserPw).text.toString()

            // エラー情報Textview
            val tvErrorMessage = findViewById<TextView>(R.id.tvRegisterErrorMessage)
            tvErrorMessage.visibility = TextView.INVISIBLE

            // バリデーション
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
                    // 新規ユーザー登録API実行
                    userViewModel.register(registerUserId, registerUserName, registerUserPw)
                }
            }
        }
    }

    // ログイン画面遷移文字列ハイライト処理
    private fun createLinkSpannable(): SpannableStringBuilder {
        val highLightText = getString(R.string.txt_to_login_highlight)
        val introText = getString(R.string.txt_to_login)

        val spannableTextBuilder = SpannableStringBuilder(introText)
        val startPos = introText.indexOf(highLightText)
        return spannableTextBuilder.apply {
            setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {
                    val intent = Intent(this@UserRegisterActivity, MainActivity::class.java)
                    view.context.startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = ContextCompat.getColor(
                        this@UserRegisterActivity,
                        R.color.design_default_color_primary
                    )
                }
            }, startPos, startPos + highLightText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}