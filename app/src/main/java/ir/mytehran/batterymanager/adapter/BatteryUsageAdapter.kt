package ir.mytehran.batterymanager.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.mytehran.batterymanager.R
import ir.mytehran.batterymanager.model.BatteryModel
import kotlin.math.roundToInt

class BatteryUsageAdapter(
    private val battery: MutableList<BatteryModel>,
    private val totalTime: Long
) :
    RecyclerView.Adapter<BatteryUsageAdapter.ViewHolder>() {

    private var batteryFinalList: MutableList<BatteryModel> = ArrayList()

    init {
        batteryFinalList = calcBatteryUsage(battery)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BatteryUsageAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.item_battery_usage, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: BatteryUsageAdapter.ViewHolder, position: Int) {
        holder.txtPercent.text = batteryFinalList[position].percentUsage.toString() + "%"

        holder.txtTime.text =batteryFinalList[position].timeusage
           // "${batteryFinalList[position].packageName} : ${} : ${}"
    }

    override fun getItemCount(): Int {
        return batteryFinalList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtPercent: TextView = view.findViewById(R.id.txt_percent)
        var txtTime: TextView = view.findViewById(R.id.txt_time)
    }

    fun calcBatteryUsage(batteryPercentArray: MutableList<BatteryModel>): MutableList<BatteryModel> {

        val finalList: MutableList<BatteryModel> = ArrayList()
        var sortedList = batteryPercentArray
            .groupBy { it.packageName }
            .mapValues { entry -> entry.value.sumBy { it.percentUsage } }.toList()
            .sortedWith(compareBy { it.second }).reversed()

        for (item in sortedList) {
            val bm = BatteryModel()
            val timePerApp = item.second.toFloat() / 100 * totalTime.toFloat() / 1000 / 60
            val hour = timePerApp / 60
            val min = timePerApp % 60
            bm.packageName = item.first
            bm.percentUsage = item.second
            bm.timeusage = "${hour.roundToInt()} hour ${min.roundToInt()} minutes"
            finalList += bm
        }
        return finalList
    }

}