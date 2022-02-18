package com.example.jetpackdemo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.jetpackdemo.common.BaseConstant
import com.example.jetpackdemo.db.dao.ShoeDao
import com.example.jetpackdemo.db.dao.UserDao
import com.example.jetpackdemo.db.data.FavouriteShoe
import com.example.jetpackdemo.db.data.Shoe
import com.example.jetpackdemo.db.data.User
import com.example.jetpackdemo.utils.AppPrefsUtils
import com.example.jetpackdemo.worker.ShoeWorker

/**
 * 数据库文件
// */
//@Volatileアノテーションとは？
//共有される変数に対して使用し、主にマルチスレッド処理で使用される。
//メモリとキャッシュの値に差異が出ないよう「フィールドの値がキャッシュされることを防止する」=「共有のメモリからしか値を取得出来ない」
//スレッドからアクセスされるたび、必ず、共有メモリ上の変数の値とスレッド上の値を一致させる。
//なので、複数スレッドからアクセスされる可能性がある場合、@Volatileとして宣言しておくと良い。
@Database(entities = [User::class, Shoe::class, FavouriteShoe::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase: RoomDatabase() {
    // 得到UserDao
    abstract fun userDao(): UserDao

    // 得到ShoeDao
    abstract fun shoeDao(): ShoeDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance?: buildDataBase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDataBase(context: Context): AppDataBase {
            return Room.databaseBuilder(context, AppDataBase::class.java, "jetPackDemo-detabase")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        val isFirstLaunch = AppPrefsUtils.getBoolean(BaseConstant.IS_FIRST_LAUNCH)
                        if(isFirstLaunch){
                            // 读取鞋的集合
                            val request = OneTimeWorkRequestBuilder<ShoeWorker>().build()
                            WorkManager.getInstance().enqueue(request)
                        }
                    }
                }).build()
        }
    }

}