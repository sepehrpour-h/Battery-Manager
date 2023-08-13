package ir.mytehran.batterymanager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.mytehran.batterymanager.databinding.ActivitySplashBinding
import java.util.Timer
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        var textArray = arrayOf(
            "Make Your Battery Powerful",
            "Make Your Battery Safe",
            "Make Your Battery Faster",
            "Make Your Battery Powerful",
            "Manage Your Phone battery",
            "Notify When Your Phone Is Full Charge"
        )

        var i = 1
        for (i in 1..6) {
            helpTextGenerator((i * 1000).toLong(), textArray[i-1])
        }

        Timer().schedule(timerTask {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 7000)

    }

    private fun helpTextGenerator(delayTime: Long, helpText: String) {
        Timer().schedule(timerTask {
            runOnUiThread(timerTask {
                binding.helpTxt.text = helpText
            })
        }, delayTime)
    }
}