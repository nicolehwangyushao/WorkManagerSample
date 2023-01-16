package com.example.workmanagersample

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.workmanagersample.workers.ExpeditedWork
import com.example.workmanagersample.workers.OneTimeWork
import com.example.workmanagersample.workers.PeriodicWork
import java.util.concurrent.TimeUnit

class WorkViewModel(application: Application) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)
    internal val outputWorkInfos: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)

    internal fun applyPeriodicWork(minute: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val periodicWorkRequest =
                PeriodicWorkRequest.Builder(PeriodicWork::class.java, minute, TimeUnit.MINUTES)
                    .addTag(TAG_OUTPUT).build()
            workManager.enqueueUniquePeriodicWork(
                "PeriodicWork",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }
    }

    @SuppressLint("RestrictedApi")
    internal fun applyOneTimeWork(second: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val oneTimeWorkRequest =
                OneTimeWorkRequest.Builder(OneTimeWork::class.java)
                    .setInitialDelay(second, TimeUnit.SECONDS)
                    .addTag(TAG_OUTPUT).build()
            workManager.enqueueUniqueWork(
                "OneTimeWork",
                ExistingWorkPolicy.KEEP,
                oneTimeWorkRequest
            )
        }
    }

    internal fun applyExpeditedWorkRequest() {
        val expeditedWorkRequest = OneTimeWorkRequest.Builder(ExpeditedWork::class.java)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag(TAG_OUTPUT).build()
        workManager.enqueueUniqueWork(
            "ExpeditedWork",
            ExistingWorkPolicy.KEEP,
            expeditedWorkRequest
        )
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