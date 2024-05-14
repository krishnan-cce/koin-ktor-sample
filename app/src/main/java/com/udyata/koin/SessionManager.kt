package com.udyata.koin

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager(private val context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    companion object {
        private const val PREF_NAME = "MyApp"
    }

    private val _isLogin = MutableStateFlow(sharedPref.getBoolean("IS_LOGIN", false))
    val isLoginStateFlow: StateFlow<Boolean> = _isLogin.asStateFlow()

    var isLogin: Boolean
        get() = sharedPref.getBoolean("IS_LOGIN", false)
        set(value) {
            editor.putBoolean("IS_LOGIN", value).apply()
            _isLogin.value = value
        }

    var jwtToken: String
        get() = sharedPref.getString("JWT_TOKEN", "") ?: ""
        set(s) = sharedPref.edit().putString("JWT_TOKEN", s).apply()


    fun clear() {
        editor.clear().apply()
        _isLogin.value = false
    }


}
