package com.example.jetpackdemo.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.jetpackdemo.db.data.User

/**
 * 用户的Dao
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE user_account = :account AND user_pwd = :pwd")
    fun login(account: String, pwd: String): LiveData<User?>

    @Query("SELECT * FROM user WHERE id=:id")
    fun findUserById(id: Long): LiveData<User>

    @Query("SELECT * FROM user")
    fun getAllUsers(): List<User>

    @Insert
    fun insertUser(user: User): Long

    @Delete
    fun deleteUser(user: User)

    @Update
    fun updateUser(user: User)
}