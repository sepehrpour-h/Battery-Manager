package ir.mytehran.batterymanager.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ir.mytehran.batterymanager.utils.BatteryUsage
import ir.mytehran.batterymanager.databinding.ActivityMainBinding
import ir.mytehran.batterymanager.model.BatteryModel
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val batteryUsage = BatteryUsage(this)


        val batteryPercentArray: MutableList<BatteryModel> = ArrayList()
        for (item in batteryUsage.getUsageStateList()) {
            if (item.totalTimeInForeground > 0) {
                val bm = BatteryModel()
                bm.packageName = item.packageName
                bm.percentUsage =
                    (item.totalTimeInForeground.toFloat() / batteryUsage.getTotalTime()
                        .toFloat() * 100).toInt()
                batteryPercentArray += bm
            }
        }
        var sortedList = batteryPercentArray
            .groupBy { it.packageName }
            .mapValues { entry -> entry.value.sumBy { it.percentUsage } }.toList()
            .sortedWith(compareBy { it.second }).reversed()

        for (item in sortedList) {
            val timePerApp = item.second.toFloat() / 100 * batteryUsage.getTotalTime() / 1000 / 60
            val hour = timePerApp / 60
            val min = timePerApp % 60
            Log.e("3636", "${item.first}  :  ${item.second} time usage is : ${hour.roundToInt()} : ${min.roundToInt()} ")
        }



        registerReceiver(batteryInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    private var batteryInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)

            if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == 0) {
                binding.txtPlug.text = "plug-out"
            } else {
                binding.txtPlug.text = "plug-in"
            }

            binding.txtTemp.text =
                (intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10).toString() + " °C"
            binding.txtVoltage.text =
                (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000).toString() + " volt"
            binding.txtTechnology.text = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)


            binding.circularProgressBar.progressMax = 100f
            binding.circularProgressBar.setProgressWithAnimation(batteryLevel.toFloat())
            binding.txtCharge.text = batteryLevel.toString() + "%"
        }
    }
}