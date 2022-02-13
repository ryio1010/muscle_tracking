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
import com.example.muscletracking.view.register.UserRegisterActivity
import com.example.muscletracking.viewmodel.user.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var userViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

//        db = AppDatabase.getInstance(applicationContext)
//        dao = db.userDao()

        // observer登録
        userViewModel.userList.observe(this, Observer {
            Toast.makeText(this,it[0].userName,Toast.LENGTH_SHORT).show()
        })


        val linkText = findViewById<TextView>(R.id.txtToRegister)
        linkText.apply {
            text = createLinkSpannable()
            movementMethod = LinkMovementMethod.getInstance()
        }

        val loginButton = findViewById<Button>(R.id.btLogin)
        loginButton.setOnClickListener(LoginButtonListener())
    }

    private inner class LoginButtonListener : View.OnClickListener {
        override fun onClick(view: View) {
            val uidInputView = findViewById<TextView>(R.id.inputId)
            val userName = uidInputView.text.toString()

            // TODO:User情報取得API実行
            // ViewModelに定義して呼び出し
            if (userName != ""){
                userViewModel.insertUser(User(0,userName))
                userViewModel.selectAllUsers()

                val intent = Intent(this@MainActivity, TopActivity::class.java)
                intent.putExtra("userName", userName)
                startActivity(intent)
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