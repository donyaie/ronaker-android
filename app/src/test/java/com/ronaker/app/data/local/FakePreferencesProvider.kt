package com.ronaker.app.data.local

import java.lang.reflect.Type

class FakePreferencesProvider:PreferencesDataSource {
    override fun contains(key: String): Boolean {
        return false
    }

    override fun putString(key: String, value: String?) {
    }

    override fun putInt(key: String, value: Int) {
    }

    override fun putLong(key: String, value: Long) {
    }

    override fun putBoolean(key: String, value: Boolean) {
    }

    override fun putFloat(key: String, value: Float) {
    }

    override fun getString(key: String, defValue: String?): String? {
        return null
    }

    override fun <T> getObject(key: String, type: Type): T? {

      return null
    }

    override fun <T> putObject(key: String, obj: T?) {


    }

    override fun getInt(key: String, defValue: Int): Int {
        return 0
    }

    override fun getLong(key: String, defValue: Long): Long {
        return 0
    }

    override fun getBoolean(key: String, defValue: Boolean?): Boolean? {
        return false
    }

    override fun getFloat(key: String, defValue: Float?): Float? {
        return 0f
    }

    override fun clearAll() {

    }


}