package ir.mytehran.batterymanager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.mytehran.batterymanager.R
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import ir.mytehran.batterymanager.adapter.BatteryUsageAdapter
import ir.mytehran.batterymanager.databinding.ActivityMainBinding
import ir.mytehran.batterymanager.databinding.ActivityUsageBatteryBinding
import ir.mytehran.batterymanager.model.BatteryModel
import ir.mytehran.batterymanager.utils.BatteryUsage
import kotlin.math.roundToInt

class UsageBatteryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsageBatteryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsageBatteryBinding.inflate(layoutInflater)
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

        val adapter = BatteryUsageAdapter(batteryPercentArray)
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
    }
}