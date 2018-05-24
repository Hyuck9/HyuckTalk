package me.hyuck9.hyucktalk

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_sign_up.*
import me.hyuck9.hyucktalk.model.User

const val PICK_FROM_ALBUM = 10

class SignUpActivity : AppCompatActivity() {

    var splashBackground: String? = null

    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        /*val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        splashBackground = mFirebaseRemoteConfig.getString(getString(R.string.rc_color))
        // apply 이용하여 아래 코드로 간소화*/
        FirebaseRemoteConfig.getInstance().apply {
            splashBackground = getString(getString(R.string.rc_color))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor(splashBackground)
        }

        signup_a_iv_profile.setOnClickListener({
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, PICK_FROM_ALBUM)
        })

        signup_a_btn_signup.setBackgroundColor(Color.parseColor(splashBackground))
        signup_a_btn_signup.setOnClickListener({

            if ( signup_a_et_email.text.toString().isEmpty() ) {
                return@setOnClickListener
            }

            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(signup_a_et_email.text.toString(), signup_a_et_password.text.toString())
                    .addOnCompleteListener(this, { task: Task<AuthResult> ->
                        val uid = task.result.user.uid
                        FirebaseStorage.getInstance().getReference("userImages").child(uid).putFile(imageUri!!).addOnCompleteListener { taskSnapshot: Task<UploadTask.TaskSnapshot> ->
                            @Suppress("DEPRECATION")
                            val imageUrl = taskSnapshot.result.downloadUrl.toString()

                            val user = User(
                                    signup_a_et_name.text.toString(),
                                    imageUrl,
                                    uid
                            )
//                            user.userName = signup_a_et_name.text.toString()
//                            user.profileImageUrl = imageUrl

                            FirebaseDatabase.getInstance().getReference("users").child(uid).setValue(user)
                        }

                    })
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("SignUpActivity", "onActivityResult")
        if ( requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK ) {
            signup_a_iv_profile.setImageURI(data!!.data)  // 가운데 뷰를 바꿈
            imageUri = data.data                        // 이미지 경로 원본
            Log.d("SignUpActivity", "onActivityResult - imageUri : $imageUri")
        }
    }
}
