package com.ronaker.app.ui.profileIdentify

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toFile
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.DocumentTypeEnum
import com.ronaker.app.ui.dialog.SelectDialog
import com.ronaker.app.ui.imagePicker.ImagePickerActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.IntentManeger
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


@AndroidEntryPoint
class ProfileIdentifyActivity : BaseActivity(), SelectDialog.OnDialogResultListener {


    private lateinit var binding: com.ronaker.app.databinding.ActivityProfileIndentifyBinding
    private val viewModel: ProfileIdentifyViewModel by viewModels()


    companion object {

        const val REQUEST_IMAGE = 1233
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProfileIdentifyActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_indentify)


        binding.viewModel = viewModel






        viewModel.errorMessage.observe(this, {errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })

        viewModel.loading.observe(this, {value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })



        viewModel.pickImage.observe(this, {
            onProfileImageClick()
        })
        viewModel.finish.observe(this, {
            finish()
        })



        binding.documentInput.setOnClickListener {


            val items = ArrayList<SelectDialog.SelectItem>()

            DocumentTypeEnum.values().forEach {
                if (it != DocumentTypeEnum.None)
                    items.add(SelectDialog.SelectItem(it.key, it.title))

            }



            SelectDialog.DialogBuilder(supportFragmentManager).setTitle("Select Document Type")
                .setItems(items).setListener(this).show()

        }



        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }



        binding.loading.hideLoading()


    }


    private fun onProfileImageClick() {
        Dexter.withContext(this)
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
                        showImagePickerOptions()
                    }

                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }


            }).check()
    }


    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(
            this,
            object : ImagePickerActivity.PickerOptionListener {
                override fun onTakeCameraSelected() {
                    launchCameraIntent()
                }

                override fun onChooseGallerySelected() {
                    launchGalleryIntent()
                }
            })
    }

    private fun launchCameraIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)

        startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent, REQUEST_IMAGE)

    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_permission_title))
        builder.setMessage(getString(R.string.dialog_permission_message))
        builder.setPositiveButton(
            getString(R.string.go_to_settings)

        ) { dialog, _ ->
            dialog?.cancel()
            IntentManeger.openSettings(this, 101)
        }
        builder.setNegativeButton(getString(android.R.string.cancel))
        { dialog, _ -> dialog?.cancel() }
        builder.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri: Uri? = data?.getParcelableExtra("path")
                try {

                    uri?.toFile()
                    uri?.let { viewModel.selectImage(it) }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data)


    }

    override fun onDialogResult(
        result: SelectDialog.DialogResultEnum,
        selectedItem: SelectDialog.SelectItem?
    ) {
        selectedItem?.let { viewModel.selectItem(selectedItem) }
    }


}