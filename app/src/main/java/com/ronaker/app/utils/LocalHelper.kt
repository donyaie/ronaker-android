@file:Suppress("DEPRECATION")

package com.ronaker.app.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import com.ronaker.app.data.DefaultUserRepository.Companion.USER_LANGUAGE_KEY
import com.ronaker.app.data.local.PreferencesProvider
import java.util.*

object LocaleHelper {

    private val TAG = LocaleHelper::class.java.simpleName

    fun onAttach(context: Context): Context {
        return setLocale(context, getPersistedData(context, LANGUAGE_DEFAULT))// Locale.getDefault().language))
    }

    fun setLocale(context: Activity, language: String): Context {
        persist(context, language)
        return updateResources(context.baseContext, language)
    }

    private fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return updateResources(context, language)
    }


    private fun getPersistedData(context: Context, defaultLanguage: String? = null): String {
        val preferences = PreferencesProvider(context)
        return preferences.getString(USER_LANGUAGE_KEY, defaultLanguage) ?: LANGUAGE_DEFAULT
    }

    private fun persist(context: Context, language: String) {
        val preferences = PreferencesProvider(context)
        preferences.putString(USER_LANGUAGE_KEY, language)
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        val configuration = Configuration(context.resources.configuration)

        Locale.setDefault(locale)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)


    }


}