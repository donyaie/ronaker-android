package com.ronaker.app.ui.profileIdentify


import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ContentRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.DocumentTypeEnum
import com.ronaker.app.ui.dialog.SelectDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class ProfileIdentifyViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository
)  : BaseViewModel() {



    var selectedDocument: DocumentTypeEnum? = null


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()

    val pickImage: MutableLiveData<Boolean> = MutableLiveData()
    val finish: MutableLiveData<Boolean> = MutableLiveData()


    val emptyImageVisibility: MutableLiveData<Int> = MutableLiveData()
    val imageVisibility: MutableLiveData<Int> = MutableLiveData()
    val identifyImage: MutableLiveData<String> = MutableLiveData()
    val uploadVisibility: MutableLiveData<Int> = MutableLiveData()


    val documentTitle: MutableLiveData<String> = MutableLiveData()


    private lateinit var mUri: Uri
    private var mImageSuid: String? = null


    private var uploadSubscription: Disposable? = null
    private var identitySubscription: Disposable? = null


    fun selectImage(uri: Uri) {

        if (uri.toString().isNotEmpty()) {
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

                mUri
            )

            .doOnSubscribe {
                loadingButton.value = true
            }
            .doOnTerminate {
                loadingButton.value = false
            }

            .subscribe { result ->
                if (result.isSuccess()) {


                    result.data?.suid?.let { mImageSuid = it }

                    result.data?.suid?.let { addIdentity(it) }

                } else {
                    errorMessage.value = result.error?.message
                }
            }


    }


    private fun addIdentity(imageSuid: String) {


        if (selectedDocument == null) {

            errorMessage.value = "Please select document type"

            return
        }

        identitySubscription?.dispose()


        identitySubscription = userRepository
            .addDocument(

                imageSuid,
                selectedDocument ?: DocumentTypeEnum.None
            )

            .doOnSubscribe {
                loadingButton.value = true
            }
            .doOnTerminate {
                loadingButton.value = false
            }

            .subscribe { result ->
                if (result.isAcceptable()) {
                    finish.value = true
                } else {
                    errorMessage.value = result.error?.message
                }
            }

    }


    override fun onCleared() {
        super.onCleared()
        uploadSubscription?.dispose()
        identitySubscription?.dispose()
    }

    fun selectItem(selectedItem: SelectDialog.SelectItem) {

        selectedDocument = DocumentTypeEnum.get(selectedItem.id)

        documentTitle.value = selectedItem.title
        if (selectedDocument == DocumentTypeEnum.None) {
            selectedDocument = null
            documentTitle.value = ""
        }


    }


}