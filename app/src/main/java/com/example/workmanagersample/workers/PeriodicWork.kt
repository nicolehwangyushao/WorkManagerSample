package com.example.workmanagersample.workers

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters

class PeriodicWork(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    @RequiresApi(Build.VERSION_CODES.S)
    override suspend  fun doWork(): Result {
        setForeground(getForegroundInfo())
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return makeStatusNotification("Periodic Work", applicationContext)
    }
}