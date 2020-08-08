@file:Suppress("DEPRECATION")

package com.ronaker.app.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.ronaker.app.data.DefaultUserRepository.Companion.USER_LANGUAGE_KEY
import com.ronaker.app.data.local.PreferencesProvider
import java.util.Locale

object LocaleHelper {


    fun onAttach(context: Context): Context {
        return setLocale(context, Locale.getDefault().language)
    }

    fun setLocale(context: Activity, language: String): Context {
        persist(context, language)

        val local= Locale(language)
        val configuration=Configuration(context.resources.configuration)
        Locale.setDefault(local)
        configuration.setLocale(local)
        context.baseContext.resources.updateConfiguration(configuration,context.baseContext.resources.displayMetrics)

        context.applicationContext.resources.updateConfiguration(configuration,context.applicationContext.resources.displayMetrics)

        return context


    }

   private fun setLocale(context: Context, language: String): Context {
        persist(context, language)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else
            updateResourcesLegacy(context, language)



    }



    private fun getPersistedData(context: Context, defaultLanguage: String?=null): String {
        val preferences = PreferencesProvider(context)
        return preferences.getString(USER_LANGUAGE_KEY, defaultLanguage)?: LANGUAGE_DEFAULT
    }

    private fun persist(context: Context, language: String) {
        val preferences = PreferencesProvider(context)
        preferences.putString(USER_LANGUAGE_KEY, language)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    @SuppressWarnings("deprecation")
    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources

        val configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }


}