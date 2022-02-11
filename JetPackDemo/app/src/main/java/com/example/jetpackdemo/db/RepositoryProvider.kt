package com.example.jetpackdemo.db

import android.content.Context
import androidx.paging.PagingSource
import com.example.jetpackdemo.db.dao.ShoeDao
import com.example.jetpackdemo.db.data.Shoe
import com.example.jetpackdemo.db.repository.ShoeRepository

object RepositoryProvider {

    /**
     * 得到用户仓库
     */
//    fun providerUserRepository(context: Context): UserRepository {
//        return UserRepository.getInstance(AppDataBase.getInstance(context).userDao())
//    }

    /**
     * 得到鞋的本地仓库
     */
    fun providerShoeRepository(context: Context): ShoeRepository {
        return ShoeRepository.getInstance(AppDataBase.getInstance(context).shoeDao())
    }
}