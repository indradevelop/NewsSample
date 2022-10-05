package com.example.news.api

import android.content.SharedPreferences
import javax.inject.Inject


class SessionManager @Inject constructor(private val mPref: SharedPreferences) {
    private fun putTempString(key: String?, valor: String?) {
        with(mPref.edit()) {
            putString(key, valor)
            apply()
        }
    }

    private fun getTempString(key: String?): String? {
        return mPref.getString(key, "")
    }

    private fun putTempBoolean(key: String, valor: Boolean?) {
        with(mPref.edit()) {
            putBoolean(key, valor ?: false)
            apply()
        }
    }

    private fun getTempBoolean(key: String, valor: Boolean): Boolean {
        return mPref.getBoolean(key, valor)
    }

    fun clear() {
        with(mPref.edit()) {
            clear()
            apply()
        }
    }

    var isLogin: Boolean?
        get() {
            return getTempBoolean(IS_LOGIN, false)
        }
        set(value) = putTempBoolean(
            IS_LOGIN, value
        )

    var userToken: String?
        get() {
            return getTempString(USER_TOKEN)
        }
        set(value) = putTempString(
            USER_TOKEN, value
        )

    var name: String?
        get() {
            return getTempString(NAME)
        }
        set(value) = putTempString(
            NAME, value
        )

    var email: String?
        get() {
            return getTempString(EMAIL)
        }
        set(value) = putTempString(
            EMAIL, value
        )

    companion object {
        const val IS_LOGIN = "IS_LOGIN"
        const val USER_TOKEN = "USER_TOKEN"
        const val NAME = "NAME"
        const val EMAIL = "EMAIL"
    }
}