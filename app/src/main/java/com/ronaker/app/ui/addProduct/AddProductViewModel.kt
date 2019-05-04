package com.ronaker.app.ui.addProduct

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.base.NetworkError
import com.ronaker.app.data.ContentRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.data.network.response.ContentImageResponceModel
import com.ronaker.app.model.Product
import com.ronaker.app.model.User
import com.ronaker.app.utils.Debug
import io.reactivex.disposables.Disposable
import java.lang.Exception
import java.text.DecimalFormat
import javax.inject.Inject

class AddProductViewModel : BaseViewModel() {

    internal val TAG = AddProductViewModel::class.java.name


    @Inject
    lateinit var userRepository: UserRepository


    @Inject
    lateinit var productRepository: ProductRepository


    @Inject
    lateinit var contentRepository: ContentRepository


    private var createPostSubscription: Disposable? = null


    private var uploadSubscriptionList: ArrayList<Disposable?> = ArrayList()

    var adapter: AddProductImageAdapter


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val goNext: MutableLiveData<Boolean> = MutableLiveData()


    val showPickerNext: MutableLiveData<Boolean> = MutableLiveData()

    var product: Product = Product()


    enum class StateEnum constructor(position: Int) {
        image(0),
        info(1),
        price(2),
        location(3);

        var position: Int = 0
            internal set

        init {
            this.position = position
        }

        companion object {
            operator fun get(position: Int): StateEnum {
                var state = image
                for (stateEnum in StateEnum.values()) {
                    if (position == stateEnum.position)
                        state = stateEnum
                }
                return state
            }
        }
    }


    lateinit var imagesTemp: ArrayList<Product.ProductImage>

    fun onClickImageNext() {


        imagesTemp = adapter.getimages()

        if (checkInprogressSelectImage())
            return
        if (!adapter.isAllUploaded() && imagesTemp.size > 0) {
            uploadAll(imagesTemp)
        } else if ((adapter.isAllUploaded() && imagesTemp.size > 0)
        ) {
            checkNextSelectImage()

        } else errorMessage.value = "add images"


    }

    fun onClickInfoNext(title: String, titleValid: Boolean, description: String, descriptionValid: Boolean) {
        if (titleValid && descriptionValid) {
            product.description = description
            product.name = title
            viewState.value = StateEnum.price
        }

    }

    fun onClickPriceNext(
        dayPrice: String,
        dayValid: Boolean,
        weekPrice: String,
        weekValid: Boolean,
        monthPrice: String,
        monthValid: Boolean
    ) {


        try {

            product.price_per_day = dayPrice.toDouble()
        } catch (e: Exception) {
            product.price_per_day = 0.0
            Debug.Log(TAG, e)
        }


        try {

            product.price_per_week = weekPrice.toDouble()
        } catch (e: Exception) {
            product.price_per_week = 0.0
            Debug.Log(TAG, e)
        }

        try {
            product.price_per_month = monthPrice.toDouble()
        } catch (e: Exception) {
            product.price_per_month = 0.0
            Debug.Log(TAG, e)
        }

        if (product.price_per_day!! > 0 || product.price_per_week!! > 0 || product.price_per_month!! > 0)
            viewState.value = StateEnum.location
        else
            errorMessage.value = "set price"

    }

    fun onClickLocationNext() {
        createProduct()
    }


    fun createProduct() {

        createPostSubscription = productRepository.productCreate(
            userRepository.getUserToken(),
            product
        )
            .doOnSubscribe { loading.value = true }
            .doOnTerminate { loading.value = false }
            .subscribe { result ->
                if (result.isSuccess()) {
                    goNext.value = true
                } else
                    errorMessage.value = result.error?.detail
            }

    }

    fun onClickRemoveImage(image: Product.ProductImage?) {


        if (image?.isLocal == true)
            adapter.removeItem(image)
    }

    fun onClickAddImage() {
        showPickerNext.value = true
    }


    val viewState: MutableLiveData<StateEnum> = MutableLiveData()


    init {
        adapter = AddProductImageAdapter(this)

    }


    override fun onCleared() {
        super.onCleared()
        createPostSubscription?.dispose()
        uploadSubscriptionList.forEach { it?.dispose() }
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
        uploadSubscriptionList.add(contentRepository.uploadImageWithoutProgress(
            userRepository.getUserToken(),
            model.uri
        )
            .doOnSubscribe { model.progress.value = true }
            .doOnTerminate { model.progress.value = false }
            .subscribe { result ->
                if (result.isSuccess()) {
                    model.url = result.data?.content
                    model.suid = result.data?.suid
                    model.isLocal = false
                    checkNextSelectImage()
                } else
                    errorMessage.value = result.error?.detail
            }
        )


    }


}