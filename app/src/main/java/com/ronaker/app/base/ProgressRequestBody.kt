package com.ronaker.app.base

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.Exception


class ProgressRequestBody(private val mFile: File, private val mListener: UploadCallbacks) : RequestBody() {
    private val mPath: String? = null

    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int)
        fun onError()
        fun onFinish()
    }

    override fun contentType(): MediaType? {
        // i want to upload only images
        return MediaType.parse("image/*")
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val input = FileInputStream(mFile)
        var uploaded: Long = 0

        val handler = Handler(Looper.getMainLooper())
        try {
            var read: Int
            while (true) {
                read = input.read(buffer)
                if (read == -1) break

                // update progress on UI thread
                handler.post(ProgressUpdater(uploaded, fileLength))

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }
            handler.post(ProgressFinish())

        } catch (e: Exception) {

            handler.post(ProgressError())
        } finally {
            input.close()
        }
    }

    private inner class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) : Runnable {

        override fun run() {
            mListener.onProgressUpdate((100 * mUploaded / mTotal).toInt())
        }
    }

    private inner class ProgressFinish() : Runnable {

        override fun run() {
            mListener.onFinish()
        }
    }

    private inner class ProgressError() : Runnable {

        override fun run() {
            mListener.onError()
        }
    }

    companion object {

        private val DEFAULT_BUFFER_SIZE = 2048
    }
}