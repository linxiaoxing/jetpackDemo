package com.example.navigationcomponentstutorials.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationcomponentstutorials.api.Post
import com.example.navigationcomponentstutorials.api.RetrofitFactory
import com.example.navigationcomponentstutorials.api.Status
import com.example.navigationcomponentstutorials.data.PostsRepository
import com.example.navigationcomponentstutorials.data.PostsUseCase
import com.example.navigationcomponentstutorials.data.ViewState
import com.example.navigationcomponentstutorials.fragment.util.Event
import kotlinx.coroutines.launch

class PostsCoroutineViewModel : ViewModel() {

    private val _goToDetailScreen = MutableLiveData<Event<Post>>()
    val goToDetailScreen: LiveData<Event<Post>>
        get() = _goToDetailScreen

    private val _postViewState = MutableLiveData<ViewState<List<Post>>>()
    val postViewState: LiveData<ViewState<List<Post>>>
        get() = _postViewState

    private val postsUseCase by lazy {
        PostsUseCase(
            PostsRepository(
                RetrofitFactory.getPostApiCoroutines()
            )
        )
    }

    init {
        _postViewState.value = ViewState(
            Status.SUCCESS,
            data = listOf(Post(id=1, userId=1, title="sunt aut facere repellat provident occaecati excepturi optio reprehenderit", body="quia et suscipi"))
        )
    }

    /**
     * Every thing in this function works in thread of [viewModelScope] other than network action
     * [viewModelScope] uses [MainCoroutineDispatcher.Main.immediate]
     */
    fun getPosts() {

        viewModelScope.launch {

            // Set current state to LOADING
            _postViewState.value =
                ViewState(
                    Status.LOADING
                )

            // 🔥🔥 Get result from network, invoked in Retrofit's enque function thread
            val result = postsUseCase.getPosts()

            // Check and assign result to UI
            if (result.status == Status.SUCCESS) {
                _postViewState.value =
                    ViewState(
                        Status.SUCCESS,
                        data = result.data
                    )
            } else {
                _postViewState.value =
                    ViewState(
                        Status.ERROR,
                        error = result.error
                    )
            }
        }
    }

    fun onClick(post: Post) {
        _goToDetailScreen.value = Event(post)
    }
}