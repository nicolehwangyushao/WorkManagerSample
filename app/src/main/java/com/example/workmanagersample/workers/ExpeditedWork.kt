package com.example.workmanagersample.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ExpeditedWork(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        makeStatusNotification("Expedited Work", applicationContext)
        return Result.success()
    }
}