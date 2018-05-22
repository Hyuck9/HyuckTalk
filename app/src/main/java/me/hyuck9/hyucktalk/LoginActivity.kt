package me.hyuck9.hyucktalk

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseAuth.signOut()

        val splashBackground = mFirebaseRemoteConfig.getString(getString(R.string.rc_color))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor(splashBackground)
        }


        login_btn_login.setBackgroundColor(Color.parseColor(splashBackground))
        login_btn_signup.setBackgroundColor(Color.parseColor(splashBackground))


        login_btn_login.setOnClickListener {
            loginEvent()
        }
        login_btn_signup.setOnClickListener({
            startActivity(Intent(this, SignUpActivity::class.java))
        })

        // 로그인 인터페이스 리스너
        mAuthStateListener = FirebaseAuth.AuthStateListener {
            val user = mFirebaseAuth.currentUser
            if ( user != null ) {
                // 로그인
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                // 로그아웃
            }
        }
    }

    private fun loginEvent() {
        var id = login_et_id.text.toString()
        var pw = login_et_password.text.toString()

        if ( id.isEmpty() || pw.isEmpty() ) {
            Toast.makeText(this, "아이디와 비밀번호를 입력해 주세요.", Toast.LENGTH_LONG).show()
            return
        }
        mFirebaseAuth.signInWithEmailAndPassword(id, pw).addOnCompleteListener {
            if ( !it.isSuccessful ) {
                // 로그인 실패
                Toast.makeText(this, it.exception!!.message , Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAuth.addAuthStateListener(mAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener)
    }
}
