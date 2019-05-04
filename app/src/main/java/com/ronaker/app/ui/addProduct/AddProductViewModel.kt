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
import io.reactivex.disposables.Disposable
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

    var emailError = MutableLiveData<Boolean?>()

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



  lateinit var imagesTemp:ArrayList<Product.ProductImage>

    fun onClickImageNext() {



        imagesTemp=adapter.getimages()

        if(checkInprogressSelectImage())
            return
        if (!adapter.isAllUploaded() && imagesTemp.size > 0) {
            uploadAll(imagesTemp)
        } else if ((adapter.isAllUploaded() && imagesTemp.size > 0)
        ) {
            checkNextSelectImage()

        } else errorMessage.value = "upload all image"


    }

    fun onClickInfoNext() {

        viewState.value = StateEnum.price
    }

    fun onClickPriceNext() {

        viewState.value = StateEnum.location

    }

    fun onClickLocationNext() {

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




    fun checkNextSelectImage():Boolean{
        var resault=true
        imagesTemp.forEach {if( it.progress.value==true || it.isLocal )
            resault=false
        }

        if(resault){
            product.images=imagesTemp
            viewState.value = StateEnum.info

        }


        return resault
    }

    fun checkInprogressSelectImage():Boolean{
        var resault=false
        imagesTemp.forEach {if( it.progress.value==true  )
            resault=true
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
            .doOnSubscribe { model.progress.value=true }
            .doOnTerminate {  model.progress.value=false }
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