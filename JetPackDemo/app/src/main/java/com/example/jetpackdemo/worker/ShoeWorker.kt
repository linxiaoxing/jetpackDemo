package com.example.jetpackdemo.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.jetpackdemo.common.BaseConstant
import com.example.jetpackdemo.db.RepositoryProvider
import com.example.jetpackdemo.db.data.Shoe
import com.example.jetpackdemo.utils.AppPrefsUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

class ShoeWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val TAG by lazy {
        ShoeWorker::class.java.simpleName
    }

    // 指定Dispatchers
    override val coroutineContext: CoroutineDispatcher
        get() = Dispatchers.IO

    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open("shoes.json").use {
                JsonReader(it.reader()).use { reader ->
                    // KotlinのMapをJSON変換
                    // https://qiita.com/HanaleiMoon/items/c1b8aa26732ac120a230
                    val shoeType = object : TypeToken<List<Shoe>>() {}.type
                    val shoeList: List<Shoe> = Gson().fromJson(reader, shoeType)

                    val shoeDao = RepositoryProvider.providerShoeRepository(applicationContext)
                    shoeDao.insertShoes(shoeList)
                    for (i in 0..6) {
                        for (shoe in shoeList) {
                            shoe.id += shoeList.size
                        }
                        shoeDao.insertShoes(shoeList)
                    }
                    AppPrefsUtils.putBoolean(BaseConstant.IS_FIRST_LAUNCH, false)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }
}