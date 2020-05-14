package com.ronaker.app.ui.addProduct

import android.app.Application
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.NetworkError.Companion.error_unverified_phone_number
import com.ronaker.app.data.CategoryRepository
import com.ronaker.app.data.ContentRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Category
import com.ronaker.app.model.Place
import com.ronaker.app.model.Product
import com.ronaker.app.utils.AppDebug
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class AddProductViewModel(app: Application) : BaseViewModel(app) {

    internal val TAG = AddProductViewModel::class.java.name


    @Inject
    lateinit var userRepository: UserRepository


    @Inject
    lateinit var context: Context

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


    val parentCategory: MutableLiveData<Category> = MutableLiveData()

    val goNext: MutableLiveData<Boolean> = MutableLiveData()


    val showPickerNext: MutableLiveData<Boolean> = MutableLiveData()


    var categories: ArrayList<Category> = ArrayList()

    var product: Product = Product()

    val viewState: MutableLiveData<StateEnum> = MutableLiveData()

    lateinit var imagesTemp: ArrayList<Product.ProductImage>


    enum class StateEnum constructor(position: Int) {
        image(0),
        info(1),
        category(2),
        price(3),
        location(4);

        var position: Int = 0
            internal set

        init {
            this.position = position
        }

        companion object {
            operator fun get(position: Int): StateEnum {
                var state = image
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

        } else errorMessage.value = context.getString(R.string.error_add_image)


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
                viewState.value = StateEnum.category
        }

    }


    fun onClickCategoryNext() {


        if (categories.isEmpty()) {
            errorMessage.value = "Please Select Category"
        } else if (categories[0].sub_categories.isNullOrEmpty()) {

            errorMessage.value = "Please Select Sub-Category"
        } else {
            product.new_categories = ArrayList()
            product.new_categories?.apply { add(categories[0].suid) }
            categories[0].sub_categories?.get(0)?.suid?.let { product.new_categories?.add(it) }


            if (!updateSuid.isNullOrEmpty()) {

                updateProduct(product)
            } else
                viewState.value = StateEnum.price


        }


    }

    fun selectCategory(category: Category) {

        productCategoryTitle.value = category.title
        categories.clear()
        category.sub_categories = null
        categories.add(category)

        productSubCategoryVisibility.value = View.VISIBLE
        productSubCategoryTitle.value = ""


    }

    fun selectSubCategory(category: Category) {

        if (categories.isNotEmpty()) {

            productSubCategoryTitle.value = category.title
            categories[0].sub_categories = listOf(category)
        }
    }

    fun onClickSelectCategory() {
        parentCategory.value = null
    }

    fun onClickSelectSubCategory() {
        if (categories.isNotEmpty())
            parentCategory.value = categories[0]
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
        if (!updateSuid.isNullOrEmpty()) {

            updateProduct(product)
        } else if (product.price_per_day ?: 0.toDouble() > 0 || product.price_per_week ?: 0.toDouble() > 0 || product.price_per_month ?: 0.toDouble() > 0)
            viewState.value = StateEnum.location
        else
            errorMessage.value = context.getString(R.string.error_set_price)

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
            errorMessage.value = context.getString(R.string.error_set_location)


    }


    fun createProduct() {

        createPostSubscription?.dispose()

        createPostSubscription = productRepository.productCreate(
            userRepository.getUserToken(),
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


    fun deleteImage(image: Product.ProductImage) {
        deleteImageSubscription?.dispose()

        deleteImageSubscription =
            image.suid?.let {
                contentRepository.deleteImage(
                    userRepository.getUserToken(),
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

    fun onClickRemoveImage(image: Product.ProductImage?) {


        if (image?.isLocal == true)
            adapter.removeItem(image)
        else if (updateState == StateEnum.image) {

            image?.let { deleteImage(it) }
        }


    }

    fun onClickAddImage() {
        showPickerNext.value = true
    }


    fun selectImage(uri: Uri?) {


        adapter.addLocalImage(uri)

    }

    fun uploadAll(images: ArrayList<Product.ProductImage>) {
        images.forEach {
            if (it.isLocal) {
                upload(it)
            }
        }
    }


    fun checkNextSelectImage(): Boolean {
        var resault = true

        imagesTemp.forEach {
            if (it.progress.value == true || it.isLocal)
                resault = false
        }

        if (resault && !imagesTemp.isNullOrEmpty()) {

            product.avatar = imagesTemp[0].url
            product.avatar_suid = imagesTemp[0].suid
            product.images = imagesTemp


            if (!updateSuid.isNullOrEmpty()) {

                updateProduct(product)
            } else

                viewState.value = StateEnum.info


        }


        return resault
    }

    fun checkInprogressSelectImage(): Boolean {
        var resault = false
        imagesTemp.forEach {
            if (it.progress.value == true)
                resault = true
        }

        return resault
    }

    fun upload(model: Product.ProductImage) {
//
//
        uploadSubscriptionList.add(model.uri?.let {
            contentRepository.uploadImageWithoutProgress(
                userRepository.getUserToken(),
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

    fun updateProduct(product: Product) {
        updateproductSubscription?.dispose()
        updateproductSubscription =
            updateSuid?.let {
                productRepository.productUpdate(userRepository.getUserToken(), it, product)
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
            productRepository.getProduct(userRepository.getUserToken(), suid)
                .doOnSubscribe { loading.value = true }
                .doOnTerminate { loading.value = false }
                .subscribe { result ->
                    if (result.isSuccess()) {
                        when (state) {
                            StateEnum.image -> {
                                result.data?.images?.let { adapter.addRemoteImage(it) }

                            }
                            StateEnum.price -> {


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
                            StateEnum.info -> {

                                productTitle.value = result.data?.name
                                productDescription.value = result.data?.description
                            }

                            StateEnum.category -> {

                                result.data?.categories?.let {

                                    categories.clear()
                                    categories.addAll(it)


                                    if (categories.size > 1) {
                                        categories[0].sub_categories = listOf(categories[1])
                                        categories.removeAt(1)
                                    }


                                }


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
                            StateEnum.location -> {


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