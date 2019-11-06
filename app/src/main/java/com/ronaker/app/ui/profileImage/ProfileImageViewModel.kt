package com.ronaker.app.ui.profileImage


import android.content.Context
import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ContentRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import com.ronaker.app.utils.BASE_URL
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfileImageViewModel : BaseViewModel() {


    @Inject
    lateinit
    var userRepository: UserRepository

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var contentRepository: ContentRepository


    @Inject
    lateinit
    var context: Context


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val pickImage: MutableLiveData<Boolean> = MutableLiveData()
    val finish: MutableLiveData<Boolean> = MutableLiveData()


    val emptyImageVisibility: MutableLiveData<Int> = MutableLiveData()
    val imageVisibility: MutableLiveData<Int> = MutableLiveData()
    val identifyImage: MutableLiveData<String> = MutableLiveData()
    val uploadVisibility: MutableLiveData<Int> = MutableLiveData()


    lateinit var mUri: Uri
    var mImageSuid: String? = null


    private var uploadSubscription: Disposable? = null
    private var identitySubscription: Disposable? = null

    init {

    }

    fun selectImage(uri: Uri) {

        if (!uri.toString().isNullOrEmpty()) {
            emptyImageVisibility.value = View.GONE
            imageVisibility.value = View.VISIBLE
            uploadVisibility.value = View.VISIBLE

            mUri = uri
            mImageSuid = null

            identifyImage.value = uri.toString()
        }


    }

    fun onClickAddImage() {
        pickImage.value = true

    }

    fun showImage(image: String?) {
        image?.let {
            emptyImageVisibility.value = View.GONE
            imageVisibility.value = View.VISIBLE
            uploadVisibility.value = View.GONE

            identifyImage.value= BASE_URL+it
        }

    }


    fun onClickUpload() {

        if (!::mUri.isInitialized)
            return

        uploadSubscription?.dispose()

        mImageSuid?.let {
            addIdentity(it)
            return
        }




        uploadSubscription = contentRepository
            .uploadImageWithoutProgress(
                userRepository.getUserToken(),
                mUri
            )

            .doOnSubscribe {
                loading.value = true
            }
            .doOnTerminate {
                loading.value = false
            }

            .subscribe { result ->
                if (result.isSuccess()) {


                    result.data?.suid?.let { mImageSuid = it }

                    result.data?.suid?.let { addIdentity(it) }

                } else {
                    errorMessage.value = result.error?.detail
                }
            }


    }


    fun addIdentity(imageSuid: String) {

        identitySubscription?.dispose()

        var user= User()
        user.avatar=imageSuid

        identitySubscription = userRepository
            .updateUserInfo(
                userRepository.getUserToken(),
                user
            )

            .doOnSubscribe {
                loading.value = true
            }
            .doOnTerminate {
                loading.value = false
            }

            .subscribe { result ->
                if (result.isSuccess()) {
                    finish.value = true
                } else {
                    errorMessage.value = result.error?.detail
                }
            }

    }


    override fun onCleared() {
        super.onCleared()
        uploadSubscription?.dispose()
        identitySubscription?.dispose()
    }



}