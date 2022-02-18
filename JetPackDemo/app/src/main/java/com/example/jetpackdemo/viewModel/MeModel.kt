package com.example.jetpackdemo.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.jetpackdemo.common.BaseConstant
import com.example.jetpackdemo.common.BaseConstant.IMAGE_MANIPULATION_WORK_NAME
import com.example.jetpackdemo.common.BaseConstant.KEY_IMAGE_URI
import com.example.jetpackdemo.common.BaseConstant.TAG_OUTPUT
import com.example.jetpackdemo.db.data.User
import com.example.jetpackdemo.db.repository.UserRepository
import com.example.jetpackdemo.utils.AppPrefsUtils
import com.example.jetpackdemo.worker.BlurWorker
import com.example.jetpackdemo.worker.CleanUpWorker
import com.example.jetpackdemo.worker.SaveImageToFileWorker
import kotlinx.coroutines.launch

class MeModel(private val userRepository: UserRepository) : ViewModel() {

    private var imageUri: Uri? = null
    private var outPutUri: Uri? = null
    val outPutWorkInfos: LiveData<List<WorkInfo>>
    private val workManager = WorkManager.getInstance()
    val user = userRepository.findUserById(AppPrefsUtils.getLong(BaseConstant.SP_USER_ID)) ?: MutableLiveData<User>().apply {

    }

    init {
        outPutWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    internal fun applyBlur(blurLevel: Int) {
        // 清除缓存的照片
        /*var continuation = workManager
            .beginWith(OneTimeWorkRequest.from(CleanUpWorker::class.java))*/
        // 多任务按顺序执行
        /*workManager.beginUniqueWork(
            IMAGE_MANIPULATION_WORK_NAME, // 任务名称
            ExistingWorkPolicy.REPLACE, // 任务相同的执行策略 分为REPLACE，KEEP，APPEND
            mutableListOf(
                OneTimeWorkRequest.from(CleanUpWorker::class.java)
            ))
            .then(OneTimeWorkRequestBuilder<BlurWorker>().setInputData(createInputDataForUri()).build())
            .then(OneTimeWorkRequestBuilder<SaveImageToFileWorker>().build())
            .enqueue()*/

        var continuation = workManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanUpWorker::class.java)
            )

        for (i in 0 until blurLevel) {
            val builder = OneTimeWorkRequestBuilder<BlurWorker>()
            if (i == 0) {
                builder.setInputData(createInputDataForUri())
            }
            continuation = continuation.then(builder.build())
        }

        // 构建约束条件
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true) // 非电池低电量
            .setRequiredNetworkType(NetworkType.CONNECTED) // 网络连接的情况
            .setRequiresStorageNotLow(true) // 存储空间足
            .build()

        // 储存照片
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .build()
        continuation = continuation.then(save)

        continuation.enqueue()
    }


    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else
            null
    }

    fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    /**
     * setter函数
     */
    internal fun setImageUri(uri: String?) {
        imageUri = uriOrNull(uri)
    }

    internal fun setOutputUri(uri: String?) {
        outPutUri = uriOrNull(uri)
        val value = user.value
        value?.headImage = uri!!
        if (value != null) {
            viewModelScope.launch {
                userRepository.updateUser(value)
            }
        }
    }
}