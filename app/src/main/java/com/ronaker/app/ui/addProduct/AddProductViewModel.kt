package com.ronaker.app.ui.addProduct

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.data.CategoryRepository
import com.ronaker.app.data.ContentRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Category
import com.ronaker.app.model.Image
import com.ronaker.app.model.Place
import com.ronaker.app.model.Product
import com.ronaker.app.utils.AppDebug
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class AddProductViewModel(app: Application) : BaseViewModel(app) {

    private val TAG = AddProductViewModel::class.java.name


    @Inject
    lateinit var userRepository: UserRepository


    @Inject
    lateinit var resourcesRepository: ResourcesRepository

    @Inject
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var categoryRepository: CategoryRepository


    @Inject
    lateinit var contentRepository: ContentRepository


    private var createPostSubscription: Disposable? = null
    private var deleteImageSubscription: Disposable? = null


    private var getCategorySubscription: Disposable? = null


    private var getproductSubscription: Disposable? = null
    private var updateproductSubscription: Disposable? = null


    private lateinit var updateState: StateEnum
    private var updateSuid: String? = null


    val productTitle: MutableLiveData<String> = MutableLiveData()
    val productLocation: MutableLiveData<LatLng> = MutableLiveData()
    val productDescription: MutableLiveData<String> = MutableLiveData()
    val productPriceDay: MutableLiveData<String> = MutableLiveData()
    val productPriceMonth: MutableLiveData<String> = MutableLiveData()
    val productPriceWeek: MutableLiveData<String> = MutableLiveData()


    val productCategoryTitle: MutableLiveData<String> = MutableLiveData()
    val productSubCategoryTitle: MutableLiveData<String> = MutableLiveData()
    val productSubCategoryVisibility: MutableLiveData<Int> = MutableLiveData()


    private var uploadSubscriptionList: ArrayList<Disposable?> = ArrayList()

    var adapter: AddProductImageAdapter = AddProductImageAdapter(this)


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()


    val imageInsuranceVisibility: MutableLiveData<Int> = MutableLiveData()
    val imageInsuranceEmptyVisibility: MutableLiveData<Int> = MutableLiveData()

    val insuranceImage: MutableLiveData<String> = MutableLiveData()

    val insuranceMedia = Image(isLocal = true)


    val parentCategory: MutableLiveData<List<Category>> = MutableLiveData()
    val childCategory: MutableLiveData<Category> = MutableLiveData()

    val goNext: MutableLiveData<Boolean> = MutableLiveData()


    val showImagePicker: MutableLiveData<Boolean> = MutableLiveData()
    val showInsurancePicker: MutableLiveData<Boolean> = MutableLiveData()


    var categories: ArrayList<Category> = ArrayList()

    var product: Product = Product()

    val viewState: MutableLiveData<StateEnum> = MutableLiveData()

    lateinit var imagesTemp: ArrayList<Image>


    enum class StateEnum constructor(position: Int) {
        //        Insurance(0),
        Image(0),
        Info(1),
        Category(2),
        Price(3),
        Location(4);

        var position: Int = 0
            internal set

        init {
            this.position = position
        }

        companion object {
            operator fun get(position: Int): StateEnum {
                var state = values()[0]
                for (stateEnum in values()) {
                    if (position == stateEnum.position)
                        state = stateEnum
                }
                return state
            }
        }
    }


    fun onClickImageNext() {


        imagesTemp = adapter.getimages()

        if (checkInprogressSelectImage())
            return
        if (!adapter.isAllUploaded() && imagesTemp.size > 0) {
            uploadAll(imagesTemp)
        } else if ((adapter.isAllUploaded() && imagesTemp.size > 0)
        ) {


            checkNextSelectImage()

        } else errorMessage.value = resourcesRepository.getString(R.string.error_add_image)


    }

    fun onClickInfoNext(
        title: String,
        titleValid: Boolean,
        description: String,
        descriptionValid: Boolean
    ) {
        if (titleValid && descriptionValid) {
            product.description = description
            product.name = title
            if (!updateSuid.isNullOrEmpty()) {

                updateProduct(product)
            } else
                viewState.value = StateEnum.Category
        }

    }


    fun onClickCategoryNext() {


        if (categories.isEmpty()) {
            errorMessage.value = "Please Select Category"
        } else if (categories[0].sub_categories.isNullOrEmpty()) {

            errorMessage.value = "Please Select Sub-Category"
        } else {
            product.new_categories = ArrayList()
            product.new_categories.apply { add(categories[0].suid) }
            categories[0].sub_categories?.get(0)?.suid?.let { product.new_categories.add(it) }


            if (!updateSuid.isNullOrEmpty()) {

                updateProduct(product)
            } else
                viewState.value = StateEnum.Price


        }


    }

    fun selectCategory(category: Category) {

        productCategoryTitle.value = category.title
        categories.clear()
        categories.add(category.copy())

        productSubCategoryVisibility.value = View.VISIBLE
        productSubCategoryTitle.value = ""


    }

    fun selectSubCategory(category: Category) {

        if (categories.isNotEmpty()) {

            productSubCategoryTitle.value = category.title
            categories[0].sub_categories = listOf(category)
        }
    }


    private var categorySubscription: Disposable? = null


    private var cachedCategory: List<Category>? = null

    private fun getCategories(selectParent: Boolean = false, selectChild: Boolean = false) {

        if (selectParent && cachedCategory != null) {
            parentCategory.value = cachedCategory
            return
        }

        if (selectChild && cachedCategory != null) {
            onClickSelectSubCategory()
            return
        }


        categorySubscription?.dispose()

        categorySubscription = categoryRepository.getCategories(
        )
            .doOnSubscribe { }
            .doOnTerminate { }
            .subscribe { result ->
                if (result.isSuccess()) {

                    cachedCategory = result.data


                    if (selectParent) {
                        parentCategory.value = cachedCategory
                    }

                    if (selectChild && !cachedCategory.isNullOrEmpty()) {
                        onClickSelectSubCategory()
                    }

                } else {
                    errorMessage.postValue(result.error?.message)
                }
            }

    }


    fun onClickSelectCategory() {


        getCategories(selectParent = true)
    }


    fun onClickSelectSubCategory() {

        if (cachedCategory.isNullOrEmpty()) {
            getCategories(selectChild = true)
        } else if (categories.isNotEmpty()) {


            val parent =
                cachedCategory?.find { category -> category.suid == categories[0].suid }

            childCategory.value = parent

        }
    }


    fun onClickPriceNext(
        dayPrice: String,
        weekPrice: String,
        monthPrice: String
    ) {


        try {
            product.price_per_day = dayPrice.toDouble()
        } catch (e: Exception) {
            product.price_per_day = 0.0
            AppDebug.log(TAG, e)
        }


        try {

            product.price_per_week = weekPrice.toDouble()
        } catch (e: Exception) {
            product.price_per_week = 0.0
            AppDebug.log(TAG, e)
        }

        try {
            product.price_per_month = monthPrice.toDouble()
        } catch (e: Exception) {
            product.price_per_month = 0.0
            AppDebug.log(TAG, e)
        }
        if (!updateSuid.isNullOrEmpty() && (product.price_per_day ?: 0.toDouble()) > 0) {

            updateProduct(product)
        } else if (
            (product.price_per_day ?: 0.0) > 0
//            ||
//            (product.price_per_week ?: 0.toDouble()) > 0 ||
//            (product.price_per_month ?: 0.toDouble()) > 0


        )
            viewState.value = StateEnum.Location
        else
            errorMessage.value = resourcesRepository.getString(R.string.error_set_price)

    }

    fun onClickLocationNext(place: Place?) {

        if (place != null) {

            product.address = place.mainText
            product.location = place.latLng


            if (!updateSuid.isNullOrEmpty()) {

                updateProduct(product)
            } else
                createProduct()
        } else
            errorMessage.value = resourcesRepository.getString(R.string.error_set_location)


    }


    private fun createProduct() {

        createPostSubscription?.dispose()

        createPostSubscription = productRepository.productCreate(
            product
        )
            .doOnSubscribe { loadingButton.value = true }
            .doOnTerminate { loadingButton.value = false }
            .subscribe { result ->
                if (result.isSuccess()) {
                    goNext.value = false
                } else {
                    if (result.error?.responseCode == 406)
                        goNext.value = true
                    else

                        errorMessage.value = result.error?.message
                }
            }

    }


    private fun deleteImage(image: Image) {
        deleteImageSubscription?.dispose()

        deleteImageSubscription =
            image.suid?.let {
                contentRepository.deleteImage(
                    it
                )
                    .doOnSubscribe {
                        loadingButton.value = true

                    }
                    .doOnTerminate {

                        loadingButton.value = false

                    }
                    .subscribe { result ->
                        if (result.isSuccess()) {
                            adapter.removeItem(image)
                        } else {
                            adapter.removeItem(image)
                            errorMessage.value = result.error?.message
                        }
                    }
            }


    }

    fun onClickRemoveImage(image: Image?) {


        if (image?.isLocal == true)
            adapter.removeItem(image)
        else if (updateState == StateEnum.Image) {

            image?.let { deleteImage(it) }
        }


    }


    fun onClickSelectImage(image: Image) {

        if (!image.isSelected) {
            adapter.selectImage(image)
        }


    }

    fun onClickAddImage() {
        showImagePicker.value = true
    }


    fun selectImage(uri: Uri?) {


        adapter.addLocalImage(uri)

    }

    fun onClickAddInsuranceImage() {
//        showPickerNext.value = true
        showInsurancePicker.value = true
    }

    fun onClickInsuranceNext() {
        viewState.value = StateEnum.Image
    }

    fun selectInsurance(uri: Uri) {

        insuranceMedia.apply {
            this.isLocal = true
            this.uri = uri
            this.suid = null
        }

        insuranceImage.value = uri.toString()


    }

    private fun uploadAll(images: ArrayList<Image>) {
        images.forEach {
            if (it.isLocal) {
                upload(it)
            }
        }
    }


    private fun checkNextSelectImage(): Boolean {
        var resault = true

        imagesTemp.forEach {
            if (it.progress.value == true || it.isLocal)
                resault = false
        }

        if (resault && !imagesTemp.isNullOrEmpty()) {

            imagesTemp.forEach {
                if (it.isSelected) {


                    product.avatar = it.url
                    product.avatar_suid = it.suid
                }
            }
//            product.avatar = imagesTemp[0].url
//            product.avatar_suid = imagesTemp[0].suid
//
            product.images = imagesTemp


            if (!updateSuid.isNullOrEmpty()) {

                updateProduct(product)
            } else

                viewState.value = StateEnum.Info


        }


        return resault
    }

    private fun checkInprogressSelectImage(): Boolean {
        var resault = false
        imagesTemp.forEach {
            if (it.progress.value == true)
                resault = true
        }

        return resault
    }

    private fun upload(model: Image) {
//
//
        uploadSubscriptionList.add(model.uri?.let {
            contentRepository.uploadImageWithoutProgress(
                it
            )
                .doOnSubscribe { model.progress.value = true }
                .doOnTerminate { model.progress.value = false }
                .subscribe { result ->
                    if (result.isSuccess()) {
                        model.url = result.data?.url
                        model.suid = result.data?.suid
                        model.isLocal = false
                        model.progress.value = false
                        checkNextSelectImage()
                    } else
                        errorMessage.value = result.error?.message
                }
        }
        )


    }

    private fun updateProduct(product: Product) {
        updateproductSubscription?.dispose()
        updateproductSubscription =
            updateSuid?.let {
                productRepository.productUpdate(it, product)
                    .doOnSubscribe { loadingButton.value = true }
                    .doOnTerminate { loadingButton.value = false }
                    .subscribe { result ->
                        if (result.isSuccess()) {

                            goNext.value = false
                        } else {
                            errorMessage.value = result.error?.message

                        }
                    }
            }


    }


    fun getInfo(suid: String, state: StateEnum) {

        updateState = state
        updateSuid = suid

        getproductSubscription?.dispose()
        getproductSubscription =
            productRepository.getProduct(suid)
                .doOnSubscribe { loading.value = true }
                .doOnTerminate { loading.value = false }
                .subscribe { result ->
                    if (result.isSuccess()) {
                        when (state) {
                            StateEnum.Image -> {


                                result.data?.images?.let { imageList ->

                                    result.data.avatar?.let { url ->

                                        imageList.forEach { image ->
                                            if (image.url?.compareTo(url) == 0)
                                                image.isSelected = true
                                        }

                                    }


                                    adapter.addRemoteImage(imageList)
                                }


                            }
                            StateEnum.Price -> {


                                productPriceDay.value =
                                    if (result.data?.price_per_day ?: 0 != 0) {
                                        result.data?.price_per_day.toString()
                                    } else ""

                                productPriceWeek.value =
                                    if (result.data?.price_per_week ?: 0 != 0) {
                                        result.data?.price_per_week.toString()
                                    } else ""


                                productPriceMonth.value =
                                    if (result.data?.price_per_month ?: 0 != 0) {
                                        result.data?.price_per_month.toString()
                                    } else ""


                            }
                            StateEnum.Info -> {

                                productTitle.value = result.data?.name
                                productDescription.value = result.data?.description
                            }

                            StateEnum.Category -> {


                                result.data?.categories?.let {


//                                    val allCategory = categoryRepository.getCategories()
//
//                                    if (!allCategory.isNullOrEmpty()) {
//
//                                        if(it.isNotEmpty()){
//                                            allCategory.forEach {  }
//                                        }
//
//
//
//
//                                    } else {


                                    categories.clear()
                                    categories.addAll(it)

                                    if (categories.size > 1) {
                                        categories[0].sub_categories = listOf(categories[1])
                                        categories.removeAt(1)
                                    }
                                }
//
//                                }


                                productSubCategoryVisibility.value = View.GONE
                                productSubCategoryTitle.value = ""

                                if (categories.isNotEmpty()) {


                                    productCategoryTitle.value = categories[0].title


                                    productSubCategoryVisibility.value = View.VISIBLE

                                    if (!categories[0].sub_categories.isNullOrEmpty()) {
                                        productSubCategoryTitle.value =
                                            categories[0].sub_categories?.get(0)?.title

                                        productSubCategoryVisibility.value = View.VISIBLE

                                    }

                                }


                            }
                            StateEnum.Location -> {


                                result.data?.location?.let {


                                    productLocation.value = result.data.location


                                }


                            }
                        }
                    } else {
                        errorMessage.value = result.error?.message
                        goNext.value = false
                    }
                }


    }

    override fun onCleared() {
        super.onCleared()
        createPostSubscription?.dispose()
        deleteImageSubscription?.dispose()
        uploadSubscriptionList.forEach { it?.dispose() }
        getproductSubscription?.dispose()
        updateproductSubscription?.dispose()

        getCategorySubscription?.dispose()
    }

}