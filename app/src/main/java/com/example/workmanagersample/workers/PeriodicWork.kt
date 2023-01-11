package com.example.workmanagersample.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class PeriodicWork(ctx: Context, params: WorkerParameters) : Worker(ctx, params){
    override fun doWork(): Result {
        val appContext = applicationContext
        makeStatusNotification("Blurring image", appContext)
    }
}