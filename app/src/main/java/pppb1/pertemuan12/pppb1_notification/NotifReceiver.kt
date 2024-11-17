package pppb1.pertemuan12.pppb1_notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast

class NotifReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val sharedPref = context?.getSharedPreferences("notif_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()

        when (action) {
            "ACTION_GANTENG" -> {
                val countGanteng = sharedPref?.getInt("count_ganteng", 0) ?: 0
                editor?.putInt("count_ganteng", countGanteng + 1)?.apply()
                Toast.makeText(context, "Anda telah melakukan vote ganteng", Toast.LENGTH_SHORT).show()
            }
            "ACTION_JELEK" -> {
                val countJelek = sharedPref?.getInt("count_jelek", 0) ?: 0
                editor?.putInt("count_jelek", countJelek + 1)?.apply()
                Toast.makeText(context, "Anda telah melakukan vote jelek", Toast.LENGTH_SHORT).show()
            }
        }
        MainActivity.instance?.updateCounters()
    }
}