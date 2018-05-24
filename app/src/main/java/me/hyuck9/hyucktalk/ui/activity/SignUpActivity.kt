package me.hyuck9.hyucktalk.ui.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_sign_up.*
import me.hyuck9.hyucktalk.R
import me.hyuck9.hyucktalk.model.User

class SignUpActivity : AppCompatActivity() {

    companion object {
        const val PICK_FROM_ALBUM = 10
    }

    var splashBackground: String? = null

    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        FirebaseRemoteConfig.getInstance().apply {
            splashBackground = getString(getString(R.string.rc_color))
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor(splashBackground)
//        }

        signup_a_iv_profile.setOnClickListener{ profileImageClicked() }

        signup_a_btn_signup.apply {
            setOnClickListener { signUpButtonClicked() }
            setBackgroundColor(Color.parseColor(splashBackground))
        }

    }

    /**
     * 프로필 이미지 등록 버튼 이벤트
     */
    private fun profileImageClicked() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }

    /**
     * 회원가입 버튼 이벤트
     */
    private fun signUpButtonClicked() {
        val name = signup_a_et_name.text.toString()
        val email = signup_a_et_email.text.toString()
        val password = signup_a_et_password.text.toString()

        if ( name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() ) {
            imageUri?.let {
                createUser(email, password, name)
            }
        }
    }

    /**
     * Firebase Authentication에 User Email 등록
     * Firebase Storage에 User Profile Image 등록
     * Firebase Database에 User 정보 등록
     */
    private fun createUser(email: String, password: String, name: String) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    val uid = task.result.user.uid
                    FirebaseStorage.getInstance()
                            .getReference("userImages")
                            .child(uid)
                            .putFile(imageUri!!)
                            .addOnCompleteListener { taskSnapshot: Task<UploadTask.TaskSnapshot> ->
                                @Suppress("DEPRECATION")
                                val imageUrl = taskSnapshot.result.downloadUrl.toString()

                                val user = User(name, imageUrl, uid)

                                FirebaseDatabase.getInstance().getReference("users").child(uid).setValue(user).addOnCompleteListener {
                                    this@SignUpActivity.finish()
                                }
                            }

                }
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
