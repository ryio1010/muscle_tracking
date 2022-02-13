package com.example.muscletracking.viewmodel.user

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.regex.Pattern

class UserRegisterViewModel : ViewModel() {
    val userid = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _enableRegisterFlag = MediatorLiveData<Boolean>()
    val enableRegisterFlag: MutableLiveData<Boolean>
        get() = _enableRegisterFlag

    init {
        _enableRegisterFlag.addSource(userid) { validateRegister() }
        _enableRegisterFlag.addSource(password) { validateRegister() }
    }

    private fun validateRegister() {
        _enableRegisterFlag.value = validateUserId() && validatePassword()
    }

    private fun validateUserId(): Boolean {
        return Pattern.compile("^[a-zA-Z0-9]+$")
            .matcher(userid.value ?: "").matches()
    }

    private fun validatePassword(): Boolean {
        return Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*])(?=\\S+$).{8,}$")
            .matcher(password.value ?: "").matches()
    }

    fun registerButtonClicked(context: Context) {
        val userid = userid.value ?: return
        val password = password.value ?: return
        Toast.makeText(context, "userid:$userid, password:$password", Toast.LENGTH_SHORT).show()
    }

    override fun onCleared() {
        super.onCleared()
        _enableRegisterFlag.removeSource(userid)
        _enableRegisterFlag.removeSource(password)
    }
}