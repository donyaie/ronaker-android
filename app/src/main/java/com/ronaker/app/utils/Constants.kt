package com.ronaker.app.utils

import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.BuildConfig

/** The base URL  */
val BASE_URL: String = BuildConfig.hostAPI
const val SHARE_URL: String = "https://ronaker.com/product/"
const val FONT_PATH: String = "fonts/regular.otf"
const val LANGUAGE_DEFAULT: String = "lt"


const val SUPPORT_URL: String = "support@ronaker.com"
const val SUPPORT_FACEBOOK_ID: String = "768503373489257"
const val SUPPORT_FACEBOOK_NAME: String = "RonakerApp"

const val SUPPORT_PHONE: String = "+37062457924"
//const val SUPPORT_URL: String = "ronaker.app@gmail.com"

const val TERM_URL: String = "https://ronaker.com/terms-en.html"
const val TERM_URL_LT: String = "https://ronaker.com/terms-lt.html"

const val PRIVACY_URL: String = "https://ronaker.com/privacy-policy-en.html"
const val PRIVACY_URL_LT: String = "https://ronaker.com/privacy-policy-lt.html"



/** The Google URL  */
const val GOOGLE_URL: String = "https://maps.googleapis.com"

val DEFULT_LOCATION = LatLng(54.688023, 25.274819)

const val MAP_ZOOM: Float = 17f