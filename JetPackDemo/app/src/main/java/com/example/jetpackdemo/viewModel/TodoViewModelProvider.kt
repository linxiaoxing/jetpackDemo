package com.example.jetpackdemo.viewModel

import android.content.Context
import com.example.jetpackdemo.db.RepositoryProvider
import com.example.jetpackdemo.db.repository.ShoeRepository
import com.example.jetpackdemo.db.repository.UserRepository
import com.example.jetpackdemo.viewModel.factory.LoginModelFactory
import com.example.jetpackdemo.viewModel.factory.MeModelFactory
import com.example.jetpackdemo.viewModel.factory.RegisterModelFactory

/**
 * ViewModel提供者
 */
object TodoViewModelProvider {

    fun providerRegisterModel(context: Context): RegisterModelFactory {
        val repository: UserRepository = RepositoryProvider.providerUserRepository(context)
        return RegisterModelFactory(repository)
    }

    fun providerLoginModel(context: Context): LoginModelFactory {
        val repository: UserRepository = RepositoryProvider.providerUserRepository(context)
        return LoginModelFactory(repository, context)
    }

    fun providerShoeModel(context: Context): ShoeModelFactory {
        val repository: ShoeRepository = RepositoryProvider.providerShoeRepository(context)
        return ShoeModelFactory(repository)
    }

    fun providerMeModel(context: Context): MeModelFactory {
        val repository:UserRepository = RepositoryProvider.providerUserRepository(context)
        return MeModelFactory(repository)
    }
}