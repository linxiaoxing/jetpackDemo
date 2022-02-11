package com.example.navigationcomponentstutorials.data

import com.example.navigationcomponentstutorials.api.DataResult
import com.example.navigationcomponentstutorials.api.Post

class PostsUseCase(private val postsRepository: PostsRepository) {

    suspend fun getPosts(): DataResult<List<Post>> {
        return postsRepository.getPostResult()
    }
}