package com.ronaker.app.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import java.io.File

object FileUtils{


     fun getCacheCameraPath(context: Context, fileName: String): Uri {
        val path = File(context.externalCacheDir, "camera")
        if (!path.exists()) path.mkdirs()
        val image = File(path, fileName)
        return FileProvider.getUriForFile(context, "$context.packageName.provider", image)
    }



     fun queryName(resolver: ContentResolver, uri: Uri): String {
        var name = ""

        val returnCursor = resolver.query(uri, null, null, null, null)
        val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor?.moveToFirst()
        if (nameIndex != null)
            name = returnCursor.getString(nameIndex)
        returnCursor?.close()
        return name
    }


      fun clearCameraCache(context: Context) {
          val path = File(context.externalCacheDir, "camera")
          if (path.exists() && path.isDirectory) {
              path.listFiles()?.let {

                  for (child: File? in it) {
                      child?.delete()
                  }
              }

          }
      }



    fun getCacheContractPath(context: Context, fileName: String): Uri {
        val path = File(context.externalCacheDir, "contract")
        if (!path.exists()) path.mkdirs()
        val image = File(path, fileName)
        return FileProvider.getUriForFile(context, "$context.packageName.provider", image)
    }
    fun getCacheContractFile(context: Context, fileName: String): File {
        val path = File(context.externalCacheDir, "contract")
        if (!path.exists()) path.mkdirs()
        return File(path, fileName)
    }




}