package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.backendoperations

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.MainActivity
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.R
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource.ContactRepository
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource.TimeAndBatteryRepository
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.Contact
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.TimeAndBattery

val channelID = "humaninactivitymonitoring.backendoperations.channel"

/**
 * This function creates the channel in which the notifications with this type will be placed.
 *
 * @param channelId is the id of the channel for the notifications.
 * @param context is the context of the application to allow the function to operate well.
 */
fun createNotificationChannel(channelId: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "User Notifications"
        val descriptionText = "These are the user notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

//@Composable
/**
 * This function will display the notification to the user. It controls its appearance, its content,
 * and its functionality.
 *
 * @param context is the context of the application to allow the function to operate well.
 */
fun displayNotification(context: Context) {
    createNotificationChannel(channelID, context)
    val intent = Intent(context, MainActivity::class.java)
    val yesActionIntent = Intent(context, YesActionClass::class.java)
    val yesActionPendingIntent: PendingIntent = PendingIntent.getActivity(
        context, 0, yesActionIntent, PendingIntent.FLAG_MUTABLE
    )
    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        context, 0, intent, PendingIntent.FLAG_MUTABLE
    )
    val yesAction: NotificationCompat.Action =
        NotificationCompat.Action.Builder(0, "Yes", yesActionPendingIntent).build()
    val noAction: NotificationCompat.Action =
        NotificationCompat.Action.Builder(0, "No", pendingIntent).build()

    val notificationId = 884
    val notification = NotificationCompat.Builder(context, channelID)
        .setContentTitle("ALERT")
        .setContentText("Is this an emergency?")
        .setSmallIcon(R.mipmap.ic_launcher_foreground)
        .setTimeoutAfter(10 * 60000)
        .addAction(yesAction)
        .addAction(noAction)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setOngoing(true)
        .setOnlyAlertOnce(true)

    /*val reminderId = 520
    val repository = ContactRepository(Application())
    val contactList: List<Contact> =
        repository.getContactsSync()
    if (contactList.isEmpty()) {
        val reminderNotification = NotificationCompat.Builder(context, channelID)
            .setContentTitle("Reminder")
            .setContentText("There needs to be contacts for full functionality")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setWhen(1000)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setTimeoutAfter(10 * 60000)

        with(NotificationManagerCompat.from(context)) {
            notify(reminderId, reminderNotification.build())
        }
    }*/

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, notification.build())
    }
}