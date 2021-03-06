package com.foodiedev.setrepetitiveexactalarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import com.foodiedev.setrepetitiveexactalarm.service.AlarmService
import com.foodiedev.setrepetitiveexactalarm.util.Constants
import io.karn.notify.Notify
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)
        when (intent.action) {
            Constants.ACTION_SET_EXACT -> {
                buildNotification(context, "Thông báo lịch hẹn một lần", convertDate(timeInMillis))
            }

            Constants.ACTION_SET_REPETITIVE_EXACT -> {
                setRepetitiveAlarm(AlarmService(context))
                buildNotification(context, "Thông báo lịch hẹn được lặp lại", convertDate(timeInMillis))
            }
        }
    }

    private fun buildNotification(context: Context, title: String, message: String) {
        Notify
            .with(context)
            .content {
                this.title = title
                text = "Thời gian kích hoạt lúc - $message"
            }
            .show()
    }

    private fun setRepetitiveAlarm(alarmService: AlarmService) {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(1)//set t/g lap lai 1 tuan
            Timber.d("Đặt báo thức cho tuần tới cùng thời gian - ${convertDate(this.timeInMillis)}")
        }
        alarmService.setRepetitiveAlarm(cal.timeInMillis)
    }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()

}