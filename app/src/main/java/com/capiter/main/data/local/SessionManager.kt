package com.capiter.main.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.capiter.main.constants.ConstString
import javax.inject.Inject

class SessionManager @Inject constructor(private val prefs: SharedPreferences) {

    fun getApiKey(): String = prefs.getString(ConstString.PREF_API_KEY, ConstString.DEFAULT_API_KEY)
        ?: ConstString.DEFAULT_API_KEY

    fun getPrefLanguage(): String =
        prefs.getString(ConstString.PREF_LANG, ConstString.DEFAULT_LANG) ?: ConstString.DEFAULT_LANG


    fun setPrefLanguage(value: String) {
        prefs.edit {
            putString(ConstString.PREF_LANG, value).apply()
        }
    }

}