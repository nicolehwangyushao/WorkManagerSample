package com.example.workmanagersample.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class OneTimeWork(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        makeStatusNotification("One time Work", applicationContext)
        return Result.success()
    }

}