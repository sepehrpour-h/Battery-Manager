package ir.mytehran.batterymanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.util.Timer
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {

    private lateinit var helpTxt: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        helpTxt = findViewById<TextView>(R.id.help_txt)

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
                helpTxt.text = helpText
            })
        }, delayTime)
    }
}