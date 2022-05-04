package com.example.muscletracking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.muscletracking.common.ApiResult
import com.example.muscletracking.model.user.User
import com.example.muscletracking.view.home.HomeActivity
import com.example.muscletracking.view.user.UserRegisterActivity
import com.example.muscletracking.viewmodel.user.UserViewModel
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    private var inputMethodManager: InputMethodManager? = null

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            UserViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO:　デザイン強化

        // 新規登録画面へのリンク登録
        val linkText = findViewById<TextView>(R.id.txtToRegister)
        linkText.apply {
            text = createLinkSpannable()
            movementMethod = LinkMovementMethod.getInstance()
        }

        // ログインボタン押下のリスナー登録
        val loginButton = findViewById<Button>(R.id.btLogin)
        loginButton.setOnClickListener(LoginButtonListener())

        // observer登録
        // ログインAPI実行時
        userViewModel.mUserInfo.observe(this, Observer {
            when (it) {
                is ApiResult.Success -> {
                    hideProgressDialog()

                    // ローカルDBにユーザー情報を登録
                    val userInfoForDB = User(it.value.userId, it.value.userName, it.value.password)
                    userViewModel.insertUser(userInfoForDB)

                    // トップ画面へ遷移
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    intent.putExtra("userId", it.value.userId)
                    startActivity(intent)
                    finish()
                }
                is ApiResult.Error -> {
                    hideProgressDialog()

                    // statusで分岐
                    if (it.exception is HttpException) {
                        Log.d("debug",it.exception.code().toString())
                        when (it.exception.code()) {
                            401 -> Toast.makeText(
                                this,
                                R.string.msg_can_not_login,
                                Toast.LENGTH_SHORT
                            ).show()
                            404 -> Toast.makeText(
                                this,
                                R.string.msg_can_not_find_user,
                                Toast.LENGTH_SHORT
                            ).show()
                            else -> Toast.makeText(
                                this,
                                R.string.msg_can_not_login_error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })
    }

    // ログインボタン押下処理
    private inner class LoginButtonListener : View.OnClickListener {
        override fun onClick(view: View) {
            hideKeyboard(view)
            val userId = findViewById<EditText>(R.id.inputId)
            val password = findViewById<EditText>(R.id.inputPw)

            val canLogin = checkValidation(userId, password)
            if (canLogin) {
                showProgressDialog()
                // loginAPI実行
                userViewModel.login(userId.text.toString(), password.text.toString())


            }
        }
    }

    private fun checkValidation(userId: EditText, password: EditText): Boolean {
        val id = userId.text.toString()
        val pw = password.text.toString()

        // userIdの正規表現
        val userIdPattern = "[a-zA-Z0-9]+$"
        val userIdRegex = Regex(pattern = userIdPattern)

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

    // 新規登録遷移文字列ハイライト処理
    private fun createLinkSpannable(): SpannableStringBuilder {
        val highLightText = getString(R.string.txt_to_register_highlight)
        val introText = getString(R.string.txt_to_register)

        val spannableTextBuilder = SpannableStringBuilder(introText)
        val startPos = introText.indexOf(highLightText)
        return spannableTextBuilder.apply {
            setSpan(object : ClickableSpan() {
                override fun onClick(view: View) {
                    val intent = Intent(this@MainActivity, UserRegisterActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
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

    private fun hideKeyboard(view: View) {
        this.inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun showProgressDialog() {
        if (!ProcessDialogFragment.getInstance().isAdded) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(
                ProcessDialogFragment.getInstance(),
                ProcessDialogFragment::class.java.name
            )
            transaction.commit()
        }
    }

    private fun hideProgressDialog() {
        val processDialogFragment =
            supportFragmentManager.findFragmentByTag(ProcessDialogFragment::class.java.name)
        (processDialogFragment as? DialogFragment)?.dismiss()
    }
}