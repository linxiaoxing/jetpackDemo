package com.example.navigationcomponentstutorials.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface PostApi {

    @GET("/posts")
    suspend fun getPosts(): List<Post>
}


object RetrofitFactory {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com"

    fun getPostApiCoroutines(): PostApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostApi::class.java)
    }


}