package com.example.navigationcomponentstutorials.data

import com.example.navigationcomponentstutorials.api.DataResult
import com.example.navigationcomponentstutorials.api.Post
import com.example.navigationcomponentstutorials.api.PostApi

class PostsRepository(private val postApi: PostApi) {

    suspend fun getPostResult(): DataResult<List<Post>> {

        // Using List<Post>
        return try {
            DataResult.Success(postApi.getPosts())
        } catch (error: Exception) {
            DataResult.Error(error)
        }
    }

}