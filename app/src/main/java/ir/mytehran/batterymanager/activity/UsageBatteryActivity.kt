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


        val adapter = BatteryUsageAdapter(this, batteryPercentArray, batteryUsage.getTotalTime())
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
    }
}