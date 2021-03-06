package com.ronaker.app.data.local


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.byteArrayOfInts
import com.securepreferences.SecurePreferences
import java.lang.reflect.Type

class PreferencesProvider(context: Context) : PreferencesDataSource {

    private val TAG = PreferencesProvider::class.java.simpleName

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
            preferences = SecurePreferences(
                context,
                byteArrayOfInts(0xA1, 0x2E, 0x39, 0xF4, 0x89, 0xC3).contentToString()
            )
        } catch (e: Exception) {
//            preferences = context.getSharedPreferences("user_data", MODE_PRIVATE)
            AppDebug.log(TAG, e)
        }

    }

    override operator fun contains(key: String): Boolean {
        return preferences.contains(key)
    }

    override fun putString(key: String, value: String?) {
        try {


            edit()?.putString(key, value)
        } catch (e: Exception) {

            AppDebug.log(TAG, e)
        }

        edit()?.commit()
    }

    override fun putInt(key: String, value: Int) {
        try {
            edit()?.putInt(key, value)
        } catch (e: Exception) {
            AppDebug.log(TAG, e)
        }

        edit()?.commit()
    }

    override fun putLong(key: String, value: Long) {
        try {
            edit()?.putLong(key, value)
        } catch (e: Exception) {
            AppDebug.log(TAG, e)
        }

        edit()?.commit()
    }

    override fun putBoolean(key: String, value: Boolean) {
        try {
            edit()?.putBoolean(key, value)
        } catch (e: Exception) {
            AppDebug.log(TAG, e)
        }

        edit()?.commit()
    }

    override fun putFloat(key: String, value: Float) {
        try {
            edit()?.putFloat(key, value)
        } catch (e: Exception) {
            AppDebug.log(TAG, e)
        }

        edit()?.commit()
    }

    override fun getString(key: String, defValue: String?): String? {


        return preferences.getString(key, defValue)
    }


    override fun <T> getObject(key: String, type: Type): T? {
        return try {

            val json = preferences.getString(key, null)
            if (json == null)
                null
            else {
              return  Gson().fromJson(json, type) as T
            }
        } catch (e: Exception) {
            AppDebug.log(TAG, e)
            null
        }
    }


    override fun <T> putObject(key: String, obj: T?) {
        try {

            var json: String? = null
            if (obj != null)
                json = Gson().toJson(obj)
            edit()?.putString(key, json)
        } catch (e: Exception) {
            AppDebug.log(TAG, e)

        }

        edit()?.commit()
    }


    override fun <T> putObjectList(key: String, obj: ArrayList<T>?) {
        try {

            var json: String? = null
            if (obj != null)
                json = Gson().toJson(obj)
            edit()?.putString(key, json)
        } catch (e: Exception) {
            AppDebug.log(TAG, e)

        }

        edit()?.commit()
    }

    override fun <T> getObjectList(key: String,listType: Type): ArrayList<T>? {
        return try {
            val json = preferences.getString(key, null)
            if (json == null)
                null
            else {

               Gson().fromJson(json, listType) as ArrayList<T>?

            }
        } catch (e: Exception) {
            AppDebug.log(TAG, e)
            null
        }
    }


    override fun getInt(key: String, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }

    override fun getLong(key: String, defValue: Long): Long {
        return preferences.getLong(key, defValue)
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return preferences.getFloat(key, defValue)
    }

    override fun clearAll() {
        try {
            edit()?.clear()?.commit()
        } catch (e: Exception) {
            AppDebug.log(TAG, e)
        }

    }


}
