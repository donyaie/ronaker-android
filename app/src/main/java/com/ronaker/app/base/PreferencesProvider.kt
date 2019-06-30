package com.ronaker.app.base


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


import java.lang.reflect.Type

class PreferencesProvider(context: Context) {

   lateinit var context: Context


    private lateinit var preferences: SharedPreferences

    lateinit  var editor: SharedPreferences.Editor

    init {

        try {
            this.context = context
            preferences = PreferenceManager
                .getDefaultSharedPreferences(context)
            editor = preferences.edit()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    operator fun contains(key: String): Boolean {
        return preferences.contains(key)
    }

    fun putString(key: String, value: String?): Boolean {
        try {
            editor.putString(key, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return editor.commit()
    }

    fun putInt(key: String, value: Int): Boolean {
        try {
            editor.putInt(key, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return editor.commit()
    }

    fun putLong(key: String, value: Long?): Boolean {
        try {
            editor.putLong(key, value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return editor.commit()
    }

    fun putBoolean(key: String, value: Boolean): Boolean {
        try {
            editor.putBoolean(key, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return editor.commit()
    }

    fun putFloat(key: String, value: Float?): Boolean {
        try {
            editor.putFloat(key, value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return editor.commit()
    }

    fun getString(key: String, defValue: String?): String? {
        return preferences.getString(key, defValue)
    }

//    fun getObject(key: String, type: Type): Any {
//
//        val json = preferences!!.getString(key, "")
//
//
//        return JsonHelper.fromJson(json, type)
//
//
//    }
//
//
//    fun <T> putObject(key: String, `object`: T): Boolean {
//        try {
//
//            val json = JsonHelper.toJson(`object`)
//            editor.putString(key, json)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return editor.commit()
//    }


    fun getInt(key: String, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return preferences.getLong(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean?): Boolean? {
        return preferences.getBoolean(key, defValue!!)
    }

    fun getFloat(key: String, defValue: Float?): Float? {
        return preferences.getFloat(key, defValue!!)
    }

    fun clearAll() {
        try {
            editor.clear().commit()
        } catch (ex: Exception) {

        }

    }




}
