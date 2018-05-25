package me.hyuck9.hyucktalk.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_message.*
import me.hyuck9.hyucktalk.R
import me.hyuck9.hyucktalk.adapter.MessageAdapter
import me.hyuck9.hyucktalk.model.Chat

class MessageActivity : AppCompatActivity() {

    private var uid: String? = null
    private var destinationUid: String? = null
    private var chatRoomUid: String? = null
    private val chatRoomRef = FirebaseDatabase.getInstance().getReference("chat_rooms")

    private val comments = mutableListOf<Chat.Companion.Comment>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        uid = FirebaseAuth.getInstance().currentUser!!.uid
        destinationUid = intent.getStringExtra("destinationUid")

        message_a_btn_send.setOnClickListener { sendButtonClicked() }
        checkChatRoom()



    }

    private fun sendButtonClicked() {
        val chat = Chat().apply {
            users[uid!!] = true
            users[destinationUid!!] = true
        }

        if (chatRoomUid.isNullOrBlank()) {
            message_a_btn_send.isEnabled = false
            chatRoomRef.push().setValue(chat).addOnSuccessListener {
                checkChatRoom()
            }
        } else {
            val comment = Chat.Companion.Comment(uid, message_a_et_input.text.toString())
            chatRoomRef.child(chatRoomUid).child("comments").push().setValue(comment)
        }

    }

    private fun checkChatRoom() {
        chatRoomRef.orderByChild("users/$uid").equalTo(true).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                dataSnapshot!!.children.forEach {
                    val chat = it.getValue(Chat::class.java)
                    if ( chat!!.users.containsKey(destinationUid) ) {
                        chatRoomUid = it.key
                        message_a_btn_send.isEnabled = true

                        findViewById<RecyclerView>(R.id.message_a_recyclerView).apply {
                            layoutManager = LinearLayoutManager(this@MessageActivity)
                            adapter = MessageAdapter(chatRoomUid!!, comments)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError?) {
            }

        })
    }
}
