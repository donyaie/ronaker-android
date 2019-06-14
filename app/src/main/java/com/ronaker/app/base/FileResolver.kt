package com.ronaker.app.base

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import io.reactivex.annotations.Nullable

internal class FileResolver(private val contentResolver: ContentResolver) {

    @Nullable
    fun getFilePath(selectedImage: Uri?): String? {
        if (selectedImage == null) {
            return null
        }
        var filePath:String?=null

        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null)

        cursor?.moveToFirst()
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
             filePath = cursor.getString(columnIndex)
        }

        cursor?.close()


        return filePath
    }
}