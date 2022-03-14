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

        // TODO: ユーザー名、パスワードのバリデーション追加（英数字記号のみ）
        // TODO:　Toolbarの設置
        // TODO:　デザイン強化
        // TODO:　ログインAPIのレスポンス確認

        // toolbarの設置
//        val root = findViewById<ViewGroup>(R.id.activity_main_container)
//        val toolbar = LayoutInflater.from(this).inflate(R.layout.toolbar, root, false) as Toolbar
//        root.addView(toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 新規登録画面へのリンク登録
        val linkText = findViewById<TextView>(R.id.txtToRegister)
        linkText.apply {
            text = createLinkSpannable()
            movementMethod = LinkMovementMethod.getInstance()
        }

        // ログインボタン押下のリスナー登録
        val loginButton = findViewById<Button>(R.id.btLogin)
        loginButton.setOnClickListener(LoginButtonListener())


        // エラー情報textview
        val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessage)

        // observer登録
        // ログインAPI実行時
        userViewModel.mUserInfo.observe(this, Observer {
            if (it != null) {
                // ローカルDBにユーザー情報を登録
                val userInfoForDB = User(it.userId, it.userName)
                userViewModel.insertUser(userInfoForDB)

                // トップ画面へ遷移
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                intent.putExtra("userId", it.userId)
                startActivity(intent)

            } else {
                tvErrorMessage.setText(R.string.msg_can_not_login)
                tvErrorMessage.visibility = TextView.VISIBLE
            }
        })
    }

    // ログインボタン押下処理
    private inner class LoginButtonListener : View.OnClickListener {
        override fun onClick(view: View) {
            // 入力情報の取得
            val userId = findViewById<TextView>(R.id.inputId).text.toString()
            val password = findViewById<TextView>(R.id.inputPw).text.toString()

            // エラー情報textview
            val tvErrorMessage = findViewById<TextView>(R.id.tvErrorMessage)
            tvErrorMessage.visibility = TextView.INVISIBLE

            when {
                userId.isEmpty() -> {
                    tvErrorMessage.setText(R.string.msg_no_input_userid)
                    tvErrorMessage.visibility = TextView.VISIBLE
                }
                password.isEmpty() -> {
                    tvErrorMessage.setText(R.string.msg_no_input_password)
                    tvErrorMessage.visibility = TextView.VISIBLE
                }
                else -> {
                    // ログインAPI実行
                    userViewModel.login(userId, password)
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