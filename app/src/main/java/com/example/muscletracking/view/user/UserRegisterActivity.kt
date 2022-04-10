package com.example.muscletracking.view.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.muscletracking.MainActivity
import com.example.muscletracking.R
import com.example.muscletracking.viewmodel.user.UserViewModel
import okhttp3.*

class UserRegisterActivity : AppCompatActivity() {

    private var inputMethodManager: InputMethodManager? = null

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            UserViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

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
                Toast.makeText(this, R.string.msg_can_not_register, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private inner class RegisterUserButtonListener : View.OnClickListener {
        override fun onClick(view: View) {
            hideKeyboard(view)
            // 入力情報の取得
            val registerUserId = findViewById<EditText>(R.id.inputRegisterUserId)
            val registerUserName = findViewById<EditText>(R.id.inputRegisterUserName)
            val registerUserPw = findViewById<EditText>(R.id.inputRegisterUserPw)

            val canRegister = checkValidation(registerUserId, registerUserName, registerUserPw)
            if (canRegister) {
                // RegisterAPI実行
                userViewModel.register(
                    registerUserId.text.toString(),
                    registerUserName.text.toString(),
                    registerUserPw.text.toString()
                )
            }
        }
    }

    private fun checkValidation(userId: EditText, userName: EditText, password: EditText): Boolean {
        val id = userId.text.toString()
        val name = userName.text.toString()
        val pw = password.text.toString()

        // userIdの正規表現
        val userIdPattern = "[a-zA-Z0-9]+$"
        val userIdRegex = Regex(pattern = userIdPattern)

        // userNameの正規表現
        val userNamePattern = "[a-zA-Z0-9]+$"
        val userNameRegex = Regex(pattern = userNamePattern)

        // passwordの正規表現
        val passwordPattern = "[a-zA-Z0-9]+$"
        val passwordRegex = Regex(pattern = passwordPattern)

        // userId
        if (id.isEmpty()) {
            userId.requestFocus()
            Toast.makeText(this, R.string.msg_no_input_userid, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!userIdRegex.matches(id)) {
            userId.requestFocus()
            Toast.makeText(this, R.string.msg_no_accept_userid, Toast.LENGTH_SHORT).show()
            return false
        }

        // userName
        if (name.isEmpty()) {
            userName.requestFocus()
            Toast.makeText(this, R.string.msg_no_input_username, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!userNameRegex.matches(name)) {
            userName.requestFocus()
            Toast.makeText(this, R.string.msg_no_accept_username, Toast.LENGTH_SHORT).show()
            return false
        }

        // password
        if (pw.isEmpty()) {
            password.requestFocus()
            Toast.makeText(this, R.string.msg_no_input_password, Toast.LENGTH_SHORT).show()
            return false
        }
        if (!passwordRegex.matches(pw)) {
            password.requestFocus()
            Toast.makeText(this, R.string.msg_no_accept_password, Toast.LENGTH_SHORT).show()
            return false
        }

        return true
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

    private fun hideKeyboard(view: View) {
        this.inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}