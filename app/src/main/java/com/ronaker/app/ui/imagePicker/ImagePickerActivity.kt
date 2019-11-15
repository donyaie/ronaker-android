package com.ronaker.app.ui.imagePicker

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider.getUriForFile
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.AnimationHelper
import com.yalantis.ucrop.UCrop
import java.io.File
import android.os.Build


class ImagePickerActivity : BaseActivity() {

    private var lockAspectRatio = false
    private var setBitmapMaxWidthHeight = false
    private var ASPECT_RATIO_X = 16
    private var ASPECT_RATIO_Y = 9
    private var bitmapMaxWidth = 1000
    private var bitmapMaxHeight = 1000
    private var IMAGE_COMPRESSION = 80

    interface PickerOptionListener {
        fun onTakeCameraSelected()

        fun onChooseGallerySelected()
    }




    override fun onCreate(savedInstanceState: Bundle?) {

        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_picker)

        val intent = intent
        if (intent == null) {
            Toast.makeText(
                applicationContext,
                getString(R.string.toast_image_intent_null),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        ASPECT_RATIO_X = intent.getIntExtra(INTENT_ASPECT_RATIO_X, ASPECT_RATIO_X)
        ASPECT_RATIO_Y = intent.getIntExtra(INTENT_ASPECT_RATIO_Y, ASPECT_RATIO_Y)
        IMAGE_COMPRESSION = intent.getIntExtra(INTENT_IMAGE_COMPRESSION_QUALITY, IMAGE_COMPRESSION)
        lockAspectRatio = intent.getBooleanExtra(INTENT_LOCK_ASPECT_RATIO, false)
        setBitmapMaxWidthHeight = intent.getBooleanExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false)
        bitmapMaxWidth = intent.getIntExtra(INTENT_BITMAP_MAX_WIDTH, bitmapMaxWidth)
        bitmapMaxHeight = intent.getIntExtra(INTENT_BITMAP_MAX_HEIGHT, bitmapMaxHeight)

        val requestCode = intent.getIntExtra(INTENT_IMAGE_PICKER_OPTION, -1)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            takeCameraImage()
        } else {
            chooseImageFromGallery()
        }
    }

    private fun takeCameraImage() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        fileName = System.currentTimeMillis().toString() + ".jpg"
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            getCacheImagePath(fileName)
                        )

                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                            takePictureIntent.clipData = ClipData.newRawUri("", getCacheImagePath(fileName))
                            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        if (takePictureIntent.resolveActivity(packageManager) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
                }


            }).check()
    }

    private fun chooseImageFromGallery() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val pickPhoto = Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE)
                    }
                }


            }).check()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> if (resultCode == Activity.RESULT_OK) {
                cropImage(getCacheImagePath(fileName))
            } else {
                setResultCancelled()
            }
            REQUEST_GALLERY_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                val imageUri = data?.data
                imageUri?.let { cropImage(it) }
            } else {
                setResultCancelled()
            }
            UCrop.REQUEST_CROP -> if (resultCode == Activity.RESULT_OK) {
                handleUCropResult(data)
            } else {
                setResultCancelled()
            }
            UCrop.RESULT_ERROR -> {
                data?.let {
                    val cropError = UCrop.getError(it)
                    com.ronaker.app.utils.AppDebug.Log(TAG, "Crop error: " + cropError)
                    setResultCancelled()
                }
            }
            else -> setResultCancelled()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun cropImage(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, queryName(contentResolver, sourceUri)))
        val options = UCrop.Options()
        options.setCompressionQuality(IMAGE_COMPRESSION)
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary))

        if (lockAspectRatio)
            options.withAspectRatio(ASPECT_RATIO_X.toFloat(), ASPECT_RATIO_Y.toFloat())

        if (setBitmapMaxWidthHeight)
            options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight)

        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .start(this)
    }

    private fun handleUCropResult(data: Intent?) {
        if (data == null) {
            setResultCancelled()
            return
        }
        val resultUri = UCrop.getOutput(data)
        setResultOk(resultUri)
    }

    private fun setResultOk(imagePath: Uri?) {
        val intent = Intent()
        intent.putExtra("path", imagePath)
        setResult(Activity.RESULT_OK, intent)
        finishSafe()
    }

    private fun setResultCancelled() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finishSafe()
    }

    private fun getCacheImagePath(fileName: String): Uri {
        val path = File(externalCacheDir, "camera")
        if (!path.exists()) path.mkdirs()
        val image = File(path, fileName)
        return getUriForFile(this@ImagePickerActivity, "$packageName.provider", image)
    }

    companion object {
        private val TAG = ImagePickerActivity::class.java.simpleName
        val INTENT_IMAGE_PICKER_OPTION = "image_picker_option"
        val INTENT_ASPECT_RATIO_X = "aspect_ratio_x"
        val INTENT_ASPECT_RATIO_Y = "aspect_ratio_Y"
        val INTENT_LOCK_ASPECT_RATIO = "lock_aspect_ratio"
        val INTENT_IMAGE_COMPRESSION_QUALITY = "compression_quality"
        val INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT = "set_bitmap_max_width_height"
        val INTENT_BITMAP_MAX_WIDTH = "max_width"
        val INTENT_BITMAP_MAX_HEIGHT = "max_height"


        val REQUEST_IMAGE_CAPTURE = 0
        val REQUEST_GALLERY_IMAGE = 1
        lateinit var fileName: String


        fun launchGalleryIntent(activity: Activity, requestCode: Int) {

            val intent = Intent(activity, ImagePickerActivity::class.java)
            intent.putExtra(
                ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
                ImagePickerActivity.REQUEST_GALLERY_IMAGE
            )

            // setting aspect ratio
            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
            activity.startActivityForResult(intent, requestCode)

        }


        fun showImagePickerOptions(context: Context, listener: PickerOptionListener) {
            // setup the alert builder
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.lbl_set_profile_photo))

            // add a list
            val animals = arrayOf<String>(
                context.getString(R.string.lbl_take_camera_picture),
                context.getString(R.string.lbl_choose_from_gallery)
            )
            builder.setItems(animals) { _, which ->
                when (which) {
                    0 -> listener.onTakeCameraSelected()
                    1 -> listener.onChooseGallerySelected()
                }
            }

            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }

        private fun queryName(resolver: ContentResolver, uri: Uri): String {
            var name = ""

            val returnCursor = resolver.query(uri, null, null, null, null)
            val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor?.moveToFirst()
            if (nameIndex != null)
                name = returnCursor.getString(nameIndex)
            returnCursor?.close()
            return name
        }

        /**
         * Calling this will delete the images from cache directory
         * useful to clear some memory
         */
        fun clearCache(context: Context) {
            val path = File(context.externalCacheDir, "camera")
            if (path.exists() && path.isDirectory) {
                path.listFiles()?.let {

                    for (child: File? in it) {
                        child?.delete()
                    }
                }

            }
        }
    }
}