package me.hyuck9.hyucktalk.ui.activity

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.WindowManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.activity_splash.*
import me.hyuck9.hyucktalk.BuildConfig
import me.hyuck9.hyucktalk.R


class SplashActivity : AppCompatActivity() {

    lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        /*mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.setConfigSettings(configSettings)

        // 인앱 기본값 설정
        mFirebaseRemoteConfig.setDefaults(R.xml.default_config)
        // run 함수 사용하여 아래 코드로 간소화 */
        FirebaseRemoteConfig.getInstance().run {
            mFirebaseRemoteConfig = this
            setConfigSettings(configSettings)
            setDefaults(R.xml.default_config)
        }


    }

    override fun onStart() {
        super.onStart()

        mFirebaseRemoteConfig.fetch(10)
                .addOnCompleteListener(this) {
                    Log.d("SplashActivity", "addOnCompleteListener")
                    if ( it.isSuccessful ) {
                        Log.d("SplashActivity", "addOnCompleteListener - isSuccessful")
                        mFirebaseRemoteConfig.activateFetched()
                    } else {
                        Log.d("SplashActivity", "addOnCompleteListener - failure - ${it.exception.toString()}")
                    }
                    displayMessage()
                }
    }

    private fun displayMessage() {
        val splashBackground = mFirebaseRemoteConfig.getString(getString(R.string.rc_color))
        val caps = mFirebaseRemoteConfig.getBoolean("splash_message_caps")
        val splashMessage = mFirebaseRemoteConfig.getString("splash_message")

        splash_a_linearLayout.setBackgroundColor(Color.parseColor(splashBackground))

        if ( caps ) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(splashMessage).setPositiveButton("확인"){ _, _ -> finish() }
            builder.create().show()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
