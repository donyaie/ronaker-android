package com.ronaker.app.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.onesignal.OneSignal
import com.ronaker.app.model.Category
import com.ronaker.app.model.Product
import com.ronaker.app.utils.AnalyticsManager.EVENT.SELECT_CATEGORY
import com.ronaker.app.utils.AnalyticsManager.Param.PRODUCT
import io.branch.referral.Branch

object AnalyticsManager {

    object Param {
        const val PRODUCT = "product"
        const val LOGIN_METHOD_NORMAL = "normal"
        const val LOGIN_METHOD_GOOGLE= "normal"
        const val LOGIN_METHOD_FACEBOOK = "facebook"
        const val NAME= "name"
        const val SUID= "suid"
    }
    object EVENT {

        const val SELECT_CATEGORY= "select_category"
        const val SELECT_ITEM= "select_item"
    }


    fun setUserId(userId: String) {
        OneSignal.sendTag("user_id", userId)

        FirebaseCrashlytics.getInstance().setUserId(userId)
        Branch.getInstance().setIdentity(userId)
    }
}


fun FirebaseAnalytics.actionOpenProduct(id: String?, name: String?, category: String?) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
    bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category)
    logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)

}


fun FirebaseAnalytics.actionLogin(method: String) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.METHOD, method)
    logEvent(FirebaseAnalytics.Event.LOGIN, bundle)

}





fun FirebaseAnalytics.actionShareProduct(itemId: String) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, PRODUCT)
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
    logEvent(FirebaseAnalytics.Event.SHARE, bundle)

}

fun FirebaseAnalytics.actionShare(contentType: String, itemId: String) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
    logEvent(FirebaseAnalytics.Event.SHARE, bundle)

}

fun FirebaseAnalytics.categorySelect(category: Category) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, category.title)
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, category.suid)
    logEvent(SELECT_CATEGORY, bundle)

}

fun FirebaseAnalytics.itemSelect(item: Product) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, item.name)
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, item.suid)
    logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

}

fun FirebaseAnalytics.actionSearch(search_term: String) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, search_term)
    logEvent(FirebaseAnalytics.Event.SEARCH, bundle)

}

fun FirebaseAnalytics.actionSignUp(method: String) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.METHOD, method)
    logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)

}
