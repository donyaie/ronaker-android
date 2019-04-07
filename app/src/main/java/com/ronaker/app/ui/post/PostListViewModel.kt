package com.ronaker.app.ui.post

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.model.Post
import com.ronaker.app.base.NetworkError
import com.ronaker.app.data.PostRepository
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class PostListViewModel: BaseViewModel(){

    @Inject
    lateinit var postRepository: PostRepository




    val postListAdapter: PostListAdapter = PostListAdapter()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage:MutableLiveData<String> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadPosts() }

    private lateinit var subscription: Disposable

    init{

        loadPosts()
    }

    private fun loadPosts(){

        subscription=postRepository
            .getPosts()

            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe {
                    result -> if (result.isSuccess()){
                onRetrievePostListSuccess(result.data)
            }else{
                onRetrievePostListError(result.error) }}





    }

    private fun onRetrievePostListStart(){
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish(){
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(postList:List<Post>?){

        if (postList != null) {
            postListAdapter.updatePostList(postList)
        }

    }

    private fun onRetrievePostListError(error: NetworkError?){

        errorMessage.value = error?.detail

    }


    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

}