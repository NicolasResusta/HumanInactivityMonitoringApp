package uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.backendoperations

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.view.Display
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.datasource.TimeAndBatteryRepository
import uk.ac.aber.dcs.cs31620.humaninactivitymonitoring.model.TimeAndBattery
import kotlin.math.absoluteValue

/**
 * This class is in charge of starting the timer when the phone is locked and will also trigger
 * notification once the timer is finished. It will also stop and restart the timer once the phone
 * is unlocked again.
 */
class ServiceClass : Service() {

    val time : Long = timerTime()


    private val timer = object : CountDownTimer(time, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            Log.d("Seconds remaining ", "${millisUntilFinished / 1000}")
        }

        override fun onFinish() {
            displayNotification(this@ServiceClass)
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(checkIfScreenIsOff(this)){
        timer.start()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}

/**
 * This function converts the time the user has set as "limit for locked screen time" into
 * milliseconds to allow for the timer object within the Service class from above to work.
 *
 * @return the time as an absolute value in Long format to allow the time to work with it.
 */
private fun timerTime() : Long {
    val repository = TimeAndBatteryRepository(Application())
    val timeAndBatteryList: List<TimeAndBattery> =
        repository.getAllTimeAndBatterySettingsSync()
    var timerTime = timeAndBatteryList[timeAndBatteryList.lastIndex].time.toLong()

    when (timeAndBatteryList[timeAndBatteryList.lastIndex].timeType) {
        "seconds" -> {
            timerTime *= 1000
        }
        "minutes" -> {
            timerTime *= 60000
        }
        "hours" -> {
            timerTime *= 3600000
        }
        "days" -> {
            timerTime *= 86400000
        }
    }

    return timerTime.absoluteValue
}

/**
 * This function checks whether the user's phone screen is off/locked using a DisplayManager object.
 * This is used so that the timer is started when true, and stopped and reset when false.
 *
 * @param context is the context of the application to allow the function to operate well.
 * @return true if screen is off, false if screen is on.
 */
fun checkIfScreenIsOff(context : Context) : Boolean {
    val displayManager : DisplayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    for (display in displayManager.displays) {
        if (display.state != Display.STATE_OFF) {
            return false
        }
    }
    return true
}