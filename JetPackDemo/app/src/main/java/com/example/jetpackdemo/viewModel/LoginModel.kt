package com.example.jetpackdemo.viewModel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jetpackdemo.common.BaseApplication
import com.example.jetpackdemo.common.listener.SimpleWatcher
import com.example.jetpackdemo.db.RepositoryProvider
import com.example.jetpackdemo.db.data.Shoe
import com.example.jetpackdemo.db.data.User
import com.example.jetpackdemo.db.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

class LoginModel constructor(
    private val repository: UserRepository
) : ViewModel() {

    val n = MutableLiveData("")
    val p = MutableLiveData("")
    val enable = MutableLiveData(false)

    /**
     * 用户名改变回调的函数
     */
    fun onNameChanged(s: CharSequence) {
        n.value = s.toString()
        judgeEnable()
    }

    private fun judgeEnable() {
        enable.value = n.value!!.isNotEmpty() && p.value!!.isNotEmpty()
    }

    /**
     * 密码改变的回调函数
     */
    fun onPwdChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        //p.set(s.toString())
        p.value = s.toString()
        judgeEnable()
    }

    // SimpleWatcher 是简化了的TextWatcher
    // SimpleWatcher 是简化了的TextWatcher
    val nameWatcher = object : SimpleWatcher() {
        override fun afterTextChanged(s: Editable) {
            super.afterTextChanged(s)

            n.value = s.toString()
            //n.set(s.toString())
            judgeEnable()
        }
    }

    val pwdWatcher = object : SimpleWatcher() {
        override fun afterTextChanged(s: Editable) {
            super.afterTextChanged(s)
            //p.set(s.toString())
            p.value = s.toString()
            judgeEnable()
        }
    }

    fun login(): LiveData<User?>? {
        val pwd = p.value!!
        val account = n.value!!
        //val pwd = p.get()!!
        //val account = n.get()!!
        return repository.login(account, pwd)
    }

    /**
     * 第一次启动的时候调用
     */
    fun onFirstLaunch(): String {
        val context = BaseApplication.context
        context.assets.open("shoes.json").use {
            JsonReader(it.reader()).use { reader ->
                val shoeType = object : TypeToken<List<Shoe>>() {}.type
                val shoeList: List<Shoe> = Gson().fromJson(reader, shoeType)

                val shoeDao = RepositoryProvider.providerShoeRepository(context)
                shoeDao.insertShoes(shoeList)
                for (i in 0..2) {
                    for (shoe in shoeList) {
                        shoe.id += shoeList.size
                    }
                    shoeDao.insertShoes(shoeList)
                }
            }
        }
        return "初始化数据成功！"
    }
}