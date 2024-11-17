package pppb1.pertemuan12.pppb1_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pppb1.pertemuan12.pppb1_notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val channelId = "NOTIF_VOTE"
    private val notifId = 90

    companion object {
        var instance: MainActivity? = null
    }

    fun updateCounters(){
        val sharedPref = getSharedPreferences("notif_prefs", Context.MODE_PRIVATE)
        val countGanteng = sharedPref.getInt("count_ganteng", 0)
        val countJelek = sharedPref.getInt("count_jelek", 0)

        binding.counterGanteng.text = countGanteng.toString()
        binding.counterJelek.text = countJelek.toString()
    }

    override fun onResume() {
        super.onResume()
        updateCounters()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instance = this

        val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        binding.btnNotif.setOnClickListener {
            val notifImg = BitmapFactory.decodeResource(resources, R.drawable.wonwoo)
            val appIcon = BitmapFactory.decodeResource(resources, R.drawable.app_icon)

            val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                0
            }

            val intentGanteng = Intent(this, NotifReceiver::class.java).apply { action = "ACTION_GANTENG" }
            val intentJelek = Intent(this, NotifReceiver::class.java).apply { action = "ACTION_JELEK" }

            val pendingIntentGanteng = PendingIntent.getBroadcast(this, 0, intentGanteng, flag)
            val pendingIntentJelek = PendingIntent.getBroadcast(this, 1, intentJelek, flag)

            val builder = NotificationCompat.Builder(this, channelId)
                .setLargeIcon(appIcon)
                .setSmallIcon(R.drawable.baseline_notifications)
                .setContentTitle("Counter")
                .setContentText("Counter Vote Wonwoo")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(notifImg))
                .addAction(R.drawable.baseline_thumb_up, "Ganteng", pendingIntentGanteng)
                .addAction(R.drawable.baseline_thumb_down, "Jelek", pendingIntentJelek)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notifChannel = NotificationChannel(channelId, "Notification", NotificationManager.IMPORTANCE_DEFAULT)
                notifManager.createNotificationChannel(notifChannel)
            }
            notifManager.notify(notifId, builder.build())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}