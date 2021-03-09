package com.ronaker.app.ui.addProduct

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toFile
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Category
import com.ronaker.app.model.Product
import com.ronaker.app.ui.imagePicker.ImagePickerActivity
import com.ronaker.app.ui.orders.OrdersFragment
import com.ronaker.app.ui.profileCompleteEdit.ProfileCompleteActivity
import com.ronaker.app.ui.selectCategory.AddProductCategorySelectDialog
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.IntentManeger
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.view.IPagerFragment
import com.ronaker.app.utils.view.ToolbarComponent
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class AddProductActivity : BaseActivity(), AddProductCategorySelectDialog.OnDialogResultListener {


    private val TAG = AddProductActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProductAddBinding
    private val viewModel: AddProductViewModel by viewModels()

    private lateinit var imageFragment: AddProductImageFragment

    //    private lateinit var insuranceFragment: AddProductInsuranceFragment
    private lateinit var infoFragment: AddProductInfoFragment
    private lateinit var priceFragment: AddProductPriceFragment
    private lateinit var locationFragment: AddProductLocationFragment
    private lateinit var categoryFragment: AddProductCategoryFragment

    private lateinit var adapter: ViewPagerAdapter

//    private lateinit var screenLibrary: ScreenCalculator


    private val REQUEST_IMAGE = 1233
    private val REQUEST_INSURNCE = 1224

    private var UpdateMode = false


    private var actionState = AddProductViewModel.StateEnum.values()[0]
        set(value) {
            field = value
            if (UpdateMode)
                binding.viewpager.currentItem = 0
            else {
                binding.viewpager.currentItem = actionState.position
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
        super.onCreate(savedInstanceState)
        enableKeyboardAnimator()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_add)


        binding.viewModel = viewModel


//        screenLibrary = ScreenCalculator(this)

        UpdateMode = getSuid() != null



        viewModel.viewState.observe(this, { state ->

            actionState = state

        })




        viewModel.parentCategory.observe(this, { categoies ->

            AddProductCategorySelectDialog.DialogBuilder(supportFragmentManager).setListener(this)
                .setParent(null).setCategories(categoies).show()

        })


        viewModel.childCategory.observe(this, { category ->

            category?.sub_categories?.let {
                AddProductCategorySelectDialog.DialogBuilder(supportFragmentManager)
                    .setListener(this)
                    .setParent(category).setCategories(it).show()
            }

        })



        viewModel.showImagePicker.observe(this, { state ->

            if (state)
                pickImage(REQUEST_IMAGE)


        })

        viewModel.showInsurancePicker.observe(this, { state ->

            if (state)
                pickImage(REQUEST_INSURNCE)


        })


        viewModel.errorMessage.observe(this, { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })



        viewModel.loading.observe(this, { value ->
            if (value == true) {
                binding.loading.visibility = View.VISIBLE
//                binding.loading.showLoading()
            } else
//                binding.loading.hideLoading()
                binding.loading.visibility = View.GONE
        })



        binding.toolbar.cancelClickListener = View.OnClickListener { prePage() }
        binding.toolbar.actionTextClickListener = View.OnClickListener { finish() }



        viewModel.goNext.observe(this, { value ->
            if (value)
                startActivity(ProfileCompleteActivity.newInstance(this@AddProductActivity))
            else
                finish()
        })


        viewModel.goReview.observe(this, {

            showReviewDialog()
        })

//        binding.viewpager.setScrollDurationFactor(2.0)

        binding.viewpager.isUserInputEnabled = false


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
                actionState = AddProductViewModel.StateEnum.Image
            }

        }

    }


    private fun getSuid(): String? {
        return intent.getStringExtra(SUID_KEY)
    }

    private fun getState(): AddProductViewModel.StateEnum? {

        return if (intent.hasExtra(STATE_KEY))
            AddProductViewModel.StateEnum[intent.getIntExtra(STATE_KEY, 0)]
        else
            null
    }

    fun getProduct(): Product? {
        return intent.getParcelableExtra(PRODUCT_KEY)
    }


    private fun init(state: AddProductViewModel.StateEnum?) {

        initViewPager(state)


        initViewPagerRegister(state)


    }


    private fun prePage() {

        if (UpdateMode) {
            finish()
            return
        }

        if (binding.viewpager.currentItem - 1 == AddProductViewModel.StateEnum.Image.position)
            KeyboardManager.hideSoftKeyboard(this)

        if (binding.viewpager.currentItem == 0)
            finish()


        if (binding.viewpager.currentItem > 0) {
            binding.viewpager.setCurrentItem(binding.viewpager.currentItem - 1, true)
        }

    }


    private fun initViewPagerRegister(state: AddProductViewModel.StateEnum?) {
        adapter.clear()



        if (state != null)
            when (state) {
                AddProductViewModel.StateEnum.Image -> {

                    adapter.addFragment(imageFragment)
                }
                AddProductViewModel.StateEnum.Info -> {

                    adapter.addFragment(infoFragment)
                }
                AddProductViewModel.StateEnum.Category -> {

                    adapter.addFragment(categoryFragment)
                }
                AddProductViewModel.StateEnum.Location -> {

                    adapter.addFragment(locationFragment)
                }
                AddProductViewModel.StateEnum.Price -> {

                    adapter.addFragment(priceFragment)
                }
//                AddProductViewModel.StateEnum.Insurance -> {
//                    adapter.addFragment(insuranceFragment)
//                }
            }
        else {
//            adapter.addFragment(insuranceFragment)
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


    private fun initViewPager(state: AddProductViewModel.StateEnum?) {

        adapter = ViewPagerAdapter()



        if (state != null) {

            when (state) {
                AddProductViewModel.StateEnum.Image -> {

                    imageFragment = AddProductImageFragment()
                }
                AddProductViewModel.StateEnum.Info -> {

                    infoFragment = AddProductInfoFragment()
                }
                AddProductViewModel.StateEnum.Category -> {

                    categoryFragment = AddProductCategoryFragment()
                }
                AddProductViewModel.StateEnum.Location -> {

                    locationFragment = AddProductLocationFragment()
                }
                AddProductViewModel.StateEnum.Price -> {

                    priceFragment = AddProductPriceFragment()
                }
//                AddProductViewModel.StateEnum.Insurance -> {
//
//                    insuranceFragment = AddProductInsuranceFragment()
//                }
            }
        } else {
            infoFragment = AddProductInfoFragment()
            categoryFragment = AddProductCategoryFragment()
            locationFragment = AddProductLocationFragment()
            priceFragment = AddProductPriceFragment()
            imageFragment = AddProductImageFragment()
//            insuranceFragment = AddProductInsuranceFragment()
        }



        binding.viewpager.adapter = adapter



        binding.viewpager.registerOnPageChangeCallback(viewPager2PageChangeCallback)



        KeyboardManager.hideSoftKeyboard(this)
    }

    private val viewPager2PageChangeCallback =
        OrdersFragment.ViewPager2PageChangeCallback { position ->

            actionState = AddProductViewModel.StateEnum[position]




            AppDebug.log(TAG, String.format("onSelect:%s", actionState.name))
            (adapter.getItem(position) as IPagerFragment).onSelect()


            if (actionState == AddProductViewModel.StateEnum.Image)
                KeyboardManager.hideSoftKeyboard(this@AddProductActivity)
//
//            if (actionState == AddProductViewModel.StateEnum.Insurance)
//                KeyboardManager.hideSoftKeyboard(this@AddProductActivity)


            if (actionState == AddProductViewModel.StateEnum.Category)
                KeyboardManager.hideSoftKeyboard(this@AddProductActivity)

            if (actionState == AddProductViewModel.StateEnum.Location)
                KeyboardManager.hideSoftKeyboard(this@AddProductActivity)

            if (!UpdateMode) {
                if (actionState == AddProductViewModel.StateEnum.Image) {

                    showBack(false)
                } else {
                    showBack(true)
                }


                binding.toolbar.showNavigator(true, position)
            }

        }


    private fun showBack(visiable: Boolean) {
        if (visiable) {

            binding.toolbar.cancelContainer = ToolbarComponent.CancelContainer.BACK
        } else {

            binding.toolbar.cancelContainer = ToolbarComponent.CancelContainer.NONE
        }
    }


    private fun pickImage(request: Int) {
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
                        showImagePickerOptions(request)
                    }

                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }


            }).check()
    }


    private fun showImagePickerOptions(request: Int) {
        ImagePickerActivity.showImagePickerOptions(
            this,
            object : ImagePickerActivity.PickerOptionListener {
                override fun onTakeCameraSelected() {
                    launchCameraIntent(request)
                }

                override fun onChooseGallerySelected() {
                    launchGalleryIntent(request)
                }
            })
    }

    private fun launchCameraIntent(requset: Int) {
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
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 2048)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 2048)

//        startActivityForResult(intent, requset)



        if (requset == REQUEST_IMAGE)
            getImage.launch(intent)
        else if (requset == REQUEST_INSURNCE)
            getInsurance.launch(intent)

    }

    private fun launchGalleryIntent(requset: Int) {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)


        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 2048)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 2048)


        if (requset == REQUEST_IMAGE)
            getImage.launch(intent)
        else if (requset == REQUEST_INSURNCE)
            getInsurance.launch(intent)

    }


    val getImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            // Handle the returned Uri
            if (it.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = it.data?.getParcelableExtra("path")
                try {

                    uri?.toFile()
                    viewModel.selectImage(uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

    val getInsurance =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            // Handle the returned Uri

            if (it.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = it.data?.getParcelableExtra("path")
                try {

                    uri?.toFile()
                    uri?.let { viewModel.selectInsurance(it) }
                } catch (e: IOException) {
                    AppDebug.log(TAG, e)
                }

            }

        }

    private fun showReviewDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.text_pending_dialog))
        builder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog?.cancel()
            finish()
        }
        builder.setOnDismissListener {
            finish()
        }

        builder.show()
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


    internal inner class ViewPagerAdapter :
        FragmentStateAdapter(this@AddProductActivity) {
        private val mFragmentList = ArrayList<Fragment>()


        fun addFragment(fragment: Fragment) {
            mFragmentList.add(fragment)
        }


        fun clear() {
            mFragmentList.clear()
        }


        override fun getItemCount(): Int {
            return mFragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return mFragmentList[position]
        }

        fun getItem(position: Int): Fragment {

            return mFragmentList[position]
        }
    }


}