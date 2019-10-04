package com.ronaker.app.base


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import java.lang.reflect.Type

class PreferencesProvider(context: Context) {

    lateinit var context: Context


    private lateinit var preferences: SharedPreferences

    private var editor: SharedPreferences.Editor? = null


    @SuppressLint("CommitPrefEdits")
    fun edit(): SharedPreferences.Editor? {
        if (editor == null)
            editor = preferences.edit()
        return editor
    }


    init {

        try {
            this.context = context
            preferences = PreferenceManager
                .getDefaultSharedPreferences(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    operator fun contains(key: String): Boolean {
        return preferences.contains(key)
    }

    fun putString(key: String, value: String?) {
        try {
            edit()?.putString(key, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        edit()?.commit()
    }

    fun putInt(key: String, value: Int) {
        try {
            edit()?.putInt(key, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        edit()?.commit()
    }

    fun putLong(key: String, value: Long) {
        try {
            edit()?.putLong(key, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        edit()?.commit()
    }

    fun putBoolean(key: String, value: Boolean) {
        try {
            edit()?.putBoolean(key, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        edit()?.commit()
    }

    fun putFloat(key: String, value: Float) {
        try {
            edit()?.putFloat(key, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        edit()?.commit()
    }

    fun getString(key: String, defValue: String?): String? {
        return preferences.getString(key, defValue)
    }

    fun <T> getObject(key: String, type: Type): T? {


        val json = preferences.getString(key, null)
        return if (json == null)
            null
        else
            Gson().fromJson(json, type)
    }


    fun <T> putObject(key: String, obj: T?) {
        try {

            var json: String? = null
            if (obj != null)
                json = Gson().toJson(obj)
            edit()?.putString(key, json)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        edit()?.commit()
    }


    fun getInt(key: String, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return preferences.getLong(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean?): Boolean? {
        return defValue?.let { preferences.getBoolean(key, it) }
    }

    fun getFloat(key: String, defValue: Float?): Float? {
        return defValue?.let { preferences.getFloat(key, it) }
    }

    fun clearAll() {
        try {
            edit()?.clear()?.commit()
        } catch (ex: Exception) {

        }

    }


}
