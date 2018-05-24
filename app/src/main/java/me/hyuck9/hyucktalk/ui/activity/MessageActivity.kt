package me.hyuck9.hyucktalk.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_message.*
import me.hyuck9.hyucktalk.R
import me.hyuck9.hyucktalk.model.Chat

class MessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val destinationUid = intent.getStringExtra("destinationUid")

        message_a_btn_send.setOnClickListener {
            val chat = Chat(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    destinationUid
            )

            FirebaseDatabase.getInstance().getReference("chat_rooms").push().setValue(chat)

        }
    }
}
