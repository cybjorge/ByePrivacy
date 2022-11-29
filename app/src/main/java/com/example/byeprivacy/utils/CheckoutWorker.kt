package com.example.byeprivacy.utils

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.byeprivacy.CheckinBar.checkedBar
import com.example.byeprivacy.CheckinBar.checkedbool
import com.example.byeprivacy.R
import com.example.byeprivacy.data.api.RestApi
import com.example.byeprivacy.data.api.barRequest


class CheckoutWorker(val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            1, createNotification()
        )
    }

    override suspend fun doWork(): Result {
        val response =
            RestApi.create(appContext).barMessage(barRequest("", "", "", 0.0, 0.0))
        checkedBar=""
        checkedbool = false
        return if (response.isSuccessful) Result.success() else Result.failure()
    }

    private fun createNotification(): Notification {
        val builder =
            NotificationCompat.Builder(appContext, "mobv2022").apply {
                setContentTitle("MOBV 2022")
                setContentText("Exiting bar ...")
                setSmallIcon(R.drawable.ic_baseline_location_on_24)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

        return builder.build()
    }


}