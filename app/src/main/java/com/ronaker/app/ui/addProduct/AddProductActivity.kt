package com.ronaker.app.ui.addProduct

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toFile
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Category
import com.ronaker.app.model.Product
import com.ronaker.app.ui.imagePicker.ImagePickerActivity
import com.ronaker.app.ui.phoneNumberValidation.PhoneNumberActivity
import com.ronaker.app.utils.*
import com.ronaker.app.utils.view.IPagerFragment
import com.ronaker.app.utils.view.ToolbarComponent
import java.io.IOException


class AddProductActivity : BaseActivity(), AddProductCategorySelectDialog.OnDialogResultListener {
    override fun onDialogResult(
        result: AddProductCategorySelectDialog.DialogResultEnum,
        parent: Category?,
        selectedCategory: Category?
    ) {


        if (parent == null) {
            selectedCategory?.let { viewModel.selectCategory(it) }
        } else {

            selectedCategory?.let { viewModel.selectSubCategory(it) }
        }
    }


    private val TAG = AddProductActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProductAddBinding
    private lateinit var viewModel: AddProductViewModel

    private lateinit var imageFragment: AddProductImageFragment
    private lateinit var infoFragment: AddProductInfoFragment
    private lateinit var priceFragment: AddProductPriceFragment
    private lateinit var locationFragment: AddProductLocationFragment
    private lateinit var categoryFragment: AddProductCategoryFragment

    private lateinit var adapter: ViewPagerAdapter
    private lateinit var screenLibrary: ScreenCalculator


    val REQUEST_IMAGE = 1233

    var UpdateMode = false;


    internal var actionState = AddProductViewModel.StateEnum.image
        set(value) {
            field = value
            if (UpdateMode)
                binding.viewpager.currentItem = 0
            else
                when (actionState) {

                    AddProductViewModel.StateEnum.image -> {
                        binding.viewpager.currentItem = AddProductViewModel.StateEnum.image.position
                    }

                    AddProductViewModel.StateEnum.info -> {
                        binding.viewpager.currentItem = AddProductViewModel.StateEnum.info.position
                    }
                    AddProductViewModel.StateEnum.price -> {
                        binding.viewpager.currentItem = AddProductViewModel.StateEnum.price.position
                    }
                    AddProductViewModel.StateEnum.location -> {

                        binding.viewpager.currentItem =
                            AddProductViewModel.StateEnum.location.position
                    }
                    AddProductViewModel.StateEnum.category -> {

                        binding.viewpager.currentItem =
                            AddProductViewModel.StateEnum.category.position
                    }
                }

        }
        get() {

            if (UpdateMode)
                return getState() ?: field

            return field
        }

    companion object {
        var SUID_KEY = "suid"
        var PRODUCT_KEY = "product"
        var STATE_KEY = "state"


        fun newInstance(context: Context): Intent {
            return Intent(context, AddProductActivity::class.java)
        }

        fun newInstance(
            context: Context,
            suid: String,
            state: AddProductViewModel.StateEnum
        ): Intent {
            val intent = Intent(context, AddProductActivity::class.java)
            val boundle = Bundle()
            boundle.putString(SUID_KEY, suid)
            boundle.putInt(STATE_KEY, state.position)
            intent.putExtras(boundle)

            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP



            return intent
        }


        fun newInstance(
            context: Context,
            product: Product,
            state: AddProductViewModel.StateEnum
        ): Intent {
            val intent = Intent(context, AddProductActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(PRODUCT_KEY, product)
            boundle.putInt(STATE_KEY, state.position)
            intent.putExtras(boundle)

            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP



            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_add)

        viewModel = ViewModelProviders.of(this).get(AddProductViewModel::class.java)

        binding.viewModel = viewModel


        screenLibrary = ScreenCalculator(this)

        UpdateMode = getSuid() != null



        viewModel.viewState.observe(this, Observer { state ->

            actionState = state

        })




        viewModel.parentCategory.observe(this, Observer { category ->

            AddProductCategorySelectDialog.DialogBuilder(supportFragmentManager).setListener(this)
                .setParent(category).show()

        })


        viewModel.showPickerNext.observe(this, Observer { state ->

            if (state)
                onProfileImageClick()


        })


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })



        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.visibility = View.VISIBLE
//                binding.loading.showLoading()
            } else
//                binding.loading.hideLoading()
                binding.loading.visibility = View.GONE
        })



        binding.toolbar.cancelClickListener = View.OnClickListener { prePage() }
        binding.toolbar.actionTextClickListener = View.OnClickListener { finishSafe() }



        viewModel.goNext.observe(this, Observer { value ->
            if (value)
                startActivityMakeScene(PhoneNumberActivity.newInstance(this@AddProductActivity))
            else
                finishSafe()
        })


        binding.viewpager.setScrollDurationFactor(2.0)


    }


    override fun onStart() {
        super.onStart()

        if (isFistStart()) {

            showBack(false)


            if (UpdateMode)
                binding.toolbar.centerContainer = ToolbarComponent.CenterContainer.NONE
            else
                binding.toolbar.centerContainer = ToolbarComponent.CenterContainer.DOTS

            binding.toolbar.showNavigator(false, 0)
            if (UpdateMode) {

                val state = getState()


                getSuid()?.let { state?.let { it1 -> viewModel.getInfo(it, it1) } }

                init(state)
//                if (state != null) {
//                    actionState = state
//                }

                binding.viewpager.currentItem = 0

            } else {
                binding.loading.visibility = View.GONE
                init(null)
                actionState = AddProductViewModel.StateEnum.image
            }

        }

    }


    fun getSuid(): String? {
        return intent.getStringExtra(SUID_KEY)
    }

    fun getState(): AddProductViewModel.StateEnum? {

        if (intent.hasExtra(STATE_KEY))
            return AddProductViewModel.StateEnum.get(intent.getIntExtra(STATE_KEY, 0))
        else
            return null
    }

    fun getProduct(): Product? {
        return intent.getParcelableExtra(PRODUCT_KEY)
    }


    private fun init(state: AddProductViewModel.StateEnum?) {

        initViewPager(state)


        initViewPagerRegister(state)


    }


    internal fun prePage() {

        if (UpdateMode) {
            finishSafe()
            return
        }

        if (binding.viewpager.currentItem - 1 == AddProductViewModel.StateEnum.image.position)
            KeyboardManager.hideSoftKeyboard(this)

        if (binding.viewpager.currentItem == 0)
            finishSafe()


        if (binding.viewpager.currentItem > AddProductViewModel.StateEnum.image.position) {
            binding.viewpager.setCurrentItem(binding.viewpager.currentItem - 1, true)
        }

    }


    fun initViewPagerRegister(state: AddProductViewModel.StateEnum?) {
        adapter.clear()



        if (state != null)
            when (state) {
                AddProductViewModel.StateEnum.image -> {

                    adapter.addFragment(imageFragment)
                }
                AddProductViewModel.StateEnum.info -> {

                    adapter.addFragment(infoFragment)
                }
                AddProductViewModel.StateEnum.category -> {

                    adapter.addFragment(categoryFragment)
                }
                AddProductViewModel.StateEnum.location -> {

                    adapter.addFragment(locationFragment)
                }
                AddProductViewModel.StateEnum.price -> {

                    adapter.addFragment(priceFragment)
                }
            }


        else{
            adapter.addFragment(imageFragment)
            adapter.addFragment(infoFragment)
            adapter.addFragment(categoryFragment)
            adapter.addFragment(priceFragment)
            adapter.addFragment(locationFragment)
        }

        binding.viewpager.adapter?.notifyDataSetChanged()

    }

    override fun onBackPressed() {
        prePage()
    }


    internal fun initViewPager(state: AddProductViewModel.StateEnum?) {

        adapter = ViewPagerAdapter(supportFragmentManager)



        if (state != null) {

            when (state) {
                AddProductViewModel.StateEnum.image -> {

                    imageFragment = AddProductImageFragment()
                }
                AddProductViewModel.StateEnum.info -> {

                    infoFragment = AddProductInfoFragment()
                }
                AddProductViewModel.StateEnum.category -> {

                    categoryFragment = AddProductCategoryFragment()
                }
                AddProductViewModel.StateEnum.location -> {

                    locationFragment = AddProductLocationFragment()
                }
                AddProductViewModel.StateEnum.price -> {

                    priceFragment = AddProductPriceFragment()
                }
            }
        } else {
            infoFragment = AddProductInfoFragment()
            categoryFragment = AddProductCategoryFragment()
            locationFragment = AddProductLocationFragment()
            priceFragment = AddProductPriceFragment()
            imageFragment = AddProductImageFragment()
        }



        binding.viewpager.adapter = adapter

        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {


                actionState = AddProductViewModel.StateEnum.get(position)




                AppDebug.Log(TAG, String.format("onSelect:%s", actionState.name))
                (adapter.getItem(position) as IPagerFragment).onSelect()


                if (actionState == AddProductViewModel.StateEnum.image)
                    KeyboardManager.hideSoftKeyboard(this@AddProductActivity)


                if (actionState == AddProductViewModel.StateEnum.category)
                    KeyboardManager.hideSoftKeyboard(this@AddProductActivity)

                if (actionState == AddProductViewModel.StateEnum.location)
                    KeyboardManager.hideSoftKeyboard(this@AddProductActivity)

                if (!UpdateMode) {
                    if (actionState == AddProductViewModel.StateEnum.image) {

                        showBack(false)
                    } else {
                        showBack(true)
                    }


                    binding.toolbar.showNavigator(true, position)
                }

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        KeyboardManager.hideSoftKeyboard(this)
    }


    internal fun showBack(visiable: Boolean) {
        if (visiable) {

//            binding.backButton.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
//            binding.backButton.setClickable(true)
//
            binding.toolbar.cancelContainer = ToolbarComponent.CancelContainer.BACK
        } else {

            binding.toolbar.cancelContainer = ToolbarComponent.CancelContainer.NONE
//            binding.backButton.animate().scaleX(0f).scaleY(0f).setDuration(100).start()
//            binding.backButton.setClickable(false)
        }
    }


    fun onProfileImageClick() {
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
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
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
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent, REQUEST_IMAGE)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri: Uri? = data?.getParcelableExtra("path")
                try {

                    uri?.toFile()
                    viewModel.selectImage(uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
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


    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mFragmentList = ArrayList<Fragment>()

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getItemPosition(`object`: Any): Int {

            return POSITION_NONE
        }


        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        fun addFragment(fragment: Fragment) {
            mFragmentList.add(fragment)
        }


        fun clear() {
            mFragmentList.clear()
        }

        override fun getPageTitle(position: Int): CharSequence {
            return ""
        }
    }


}