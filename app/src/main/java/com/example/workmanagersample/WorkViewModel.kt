package com.example.workmanagersample

import android.app.Application
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.workmanagersample.workers.PeriodicWork

class WorkViewModel(application: Application) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)
    internal val outputWorkInfos: LiveData<List<WorkInfo>>

    init {
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    internal fun applyPeriodicWork(second: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val periodicWorkRequest =
                OneTimeWorkRequestBuilder<PeriodicWork>()
            OneTimeWorkRequest.Builder(PeriodicWork::class.java).addTag(TAG_OUTPUT).build()
            // Create charging constraint
            var continuation = workManager.beginUniqueWork("PeriodicWork", ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from(PeriodicWork::class.java))
            continuation.enqueue()
        }
    }

    internal fun cancelWork() {
        workManager.cancelAllWork()
    }

    class WorkViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(WorkViewModel::class.java)) {
                WorkViewModel(application) as T
            } else {
                throw IllegalArgumentException("unknown ViewModel class")
            }
        }
    }
}