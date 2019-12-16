package com.ronaker.app.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import com.irozon.sneaker.Sneaker

class Alert {


    enum class Type {
        Warning,
        Error,
        Success
    }


    companion object {


        private fun makeText(context: Activity?, message: String?, type: Type) {


            if (!message.isNullOrBlank())
                context?.let {


                    val sneaker = Sneaker.with(it) // Activity, Fragment or ViewGroup
                        .setMessage(message)
                        .autoHide(true)


                    when (type) {
                        Type.Warning -> {
                            sneaker.setTitle("Warning!!")
                            sneaker.sneakWarning()

                        }
                        Type.Error -> {
                            sneaker.setTitle("Error!!")
                            sneaker.sneakError()

                        }
                        Type.Success -> {
                            sneaker.setTitle("Success!!")
                            sneaker.sneakSuccess()

                        }

                    }

                }
        }


        private fun makeText(context: Fragment?, message: String?, type: Type) {
            if (!message.isNullOrBlank())
                context?.let {
                    val sneaker = Sneaker.with(it) // Activity, Fragment or ViewGroup
                        .setMessage(message)
                        .autoHide(true)


                    when (type) {
                        Type.Warning -> {
                            sneaker.setTitle("Warning!!")
                            sneaker.sneakWarning()

                        }
                        Type.Error -> {
                            sneaker.setTitle("Error!!")
                            sneaker.sneakError()

                        }
                        Type.Success -> {
                            sneaker.setTitle("Success!!")
                            sneaker.sneakSuccess()

                        }

                    }

                }
        }


        fun makeTextError(context: Activity?, message: String?) {
            makeText(context, message, Type.Error)
        }


        fun makeTextError(context: Fragment?, message: String?) {
            makeText(context, message, Type.Error)
        }


        fun makeTextWarning(context: Activity?, message: String?) {
            makeText(context, message, Type.Warning)
        }


        fun makeTextWarning(context: Fragment?, message: String?) {
            makeText(context, message, Type.Warning)
        }


        fun makeTextSuccess(context: Activity?, message: String?) {
            makeText(context, message, Type.Success)
        }


        fun makeTextSuccess(context: Fragment?, message: String?) {
            makeText(context, message, Type.Success)
        }


    }


}