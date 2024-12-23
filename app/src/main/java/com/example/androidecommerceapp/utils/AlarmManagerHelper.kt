package com.example.androidecommerceapp.utils



import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

class AlarmManagerHelper(private val context: Context) {

    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setDailyAlarm(timeInMillis: Long) {
        // Create an Intent for the AlarmReceiver
        val intent = Intent(context, AlarmReceiver::class.java)

        // Create a PendingIntent to be triggered by the alarm
        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Set the alarm to fire at the specified time every day
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }

    // Cancel the existing alarm if needed
    fun cancelAlarm() {
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(alarmIntent)
    }

    // set alarm time to fire
    fun getAlarmTimeInMillis(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 12) // 6 PM
            set(Calendar.MINUTE, 13)
            set(Calendar.SECOND, 0)
        }
        return calendar.timeInMillis
    }
}
