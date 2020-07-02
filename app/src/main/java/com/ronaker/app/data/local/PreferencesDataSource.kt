package com.ronaker.app.data.local


import java.lang.reflect.Type

interface PreferencesDataSource {

    operator fun contains(key: String): Boolean

    fun putString(key: String, value: String?)

    fun putInt(key: String, value: Int)
    fun putLong(key: String, value: Long)

    fun putBoolean(key: String, value: Boolean)

    fun putFloat(key: String, value: Float)

    fun getString(key: String, defValue: String?): String?

    fun <T> getObject(key: String, type: Type): T?

    fun <T> putObject(key: String, obj: T?)

    fun getInt(key: String, defValue: Int): Int

    fun getLong(key: String, defValue: Long): Long

    fun getBoolean(key: String, defValue: Boolean): Boolean

    fun getFloat(key: String, defValue: Float): Float

    fun clearAll()


}
