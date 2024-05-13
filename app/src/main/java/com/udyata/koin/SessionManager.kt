package com.udyata.koin

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SessionManager(private val context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    companion object {
        private const val PREF_NAME = "MyApp"
    }

    fun clear() {
        editor.clear()
        editor.apply()
    }

    var isLogin: Boolean
        get() = sharedPref.getBoolean("IS_LOGIN", false)
        set(shuffle) = sharedPref.edit().putBoolean("IS_LOGIN", shuffle).apply()

}

