package com.example.muscletracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.muscletracking.model.user.User
import com.example.muscletracking.view.user.UserInputHeightAndWeightActivity
import com.example.muscletracking.view.user.UserRegisterActivity
import com.example.muscletracking.viewmodel.user.UserViewModel

class MainActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            UserViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // エラー情報textview
        val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessage)

        // observer登録
        userViewModel.userList.observe(this, Observer {
            Toast.makeText(this, it[0].userName, Toast.LENGTH_SHORT).show()
        })

        userViewModel.mUserInfo.observe(this, Observer {
            if (it != null) {
                if (it.isFirstLogin) {
                    // 身長・体重入力画面へ遷移
                    val intent =
                        Intent(this@MainActivity, UserInputHeightAndWeightActivity::class.java)
                    intent.putExtra("userid", it.userid)
                    intent.putExtra("username", it.username)
                    startActivity(intent)
                } else {
                    // ローカルDBにユーザー情報を登録
                    val userInfo = userViewModel.selectUserById(it.userid)
                    if (userInfo == null) {
                        val userInfoForDB = User(it.userid, it.username, it.height, it.weight)
                        userViewModel.insertUser(userInfoForDB)
                    }
                    // トップ画面へ遷移
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    intent.putExtra("userName", it.username)
                    startActivity(intent)
                }
            } else {
                tvErrorMessage.setText(R.string.msg_can_not_login)
                tvErrorMessage.visibility = TextView.VISIBLE
            }
        })


        // 新規登録画面へのリンク登録
        val linkText = findViewById<TextView>(R.id.txtToRegister)
        linkText.apply {
            text = createLinkSpannable()
            movementMethod = LinkMovementMethod.getInstance()
        }

        // ログインボタン押下のリスナー登録
        val loginButton = findViewById<Button>(R.id.btLogin)
        loginButton.setOnClickListener(LoginButtonListener())
    }

    private inner class LoginButtonListener : View.OnClickListener {
        override fun onClick(view: View) {
            // 入力情報の取得
            val uidInputView = findViewById<TextView>(R.id.inputId)
            val pwInputView = findViewById<TextView>(R.id.inputPw)
            val userid = uidInputView.text.toString()
            val password = pwInputView.text.toString()

            // エラー情報textview
            val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessage)
            tvErrorMessage.visibility = TextView.INVISIBLE

            when {
                userid.isEmpty() -> {
                    tvErrorMessage.setText(R.string.msg_no_input_userid)
                    tvErrorMessage.visibility = TextView.VISIBLE
                }
                password.isEmpty() -> {
                    tvErrorMessage.setText(R.string.msg_no_input_password)
                    tvErrorMessage.visibility = TextView.VISIBLE
                }
                else -> {
                    // api実行
                    userViewModel.login(userid, password)
                }
            }
        }
    }

    // 新規登録繊維文字列ハイライト処理
    private fun createLinkSpannable(): SpannableStringBuilder {
        val highLightText = getString(R.string.txt_to_register_highlight)
        val introText = getString(R.string.txt_to_register)

        val spannableTextBuilder = SpannableStringBuilder(introText)
        val startPos = introText.indexOf(highLightText)
        return spannableTextBuilder.apply {
            setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {
                    val intent = Intent(this@MainActivity, UserRegisterActivity::class.java)
                    view.context.startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = ContextCompat.getColor(
                        this@MainActivity,
                        R.color.design_default_color_primary
                    )
                }
            }, startPos, startPos + highLightText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}