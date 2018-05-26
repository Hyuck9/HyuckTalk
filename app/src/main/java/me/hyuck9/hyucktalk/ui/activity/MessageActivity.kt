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
import me.hyuck9.hyucktalk.model.User

class MessageActivity : AppCompatActivity() {

    private var uid: String? = null
    private var destinationUid: String? = null
    private var chatRoomUid: String? = null
    private val chatRoomRef = FirebaseDatabase.getInstance().getReference("chat_rooms")

    private val messageAdapter: MessageAdapter = MessageAdapter()
    private lateinit var messageRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        uid = FirebaseAuth.getInstance().currentUser!!.uid
        destinationUid = intent.getStringExtra("destinationUid")

        message_a_btn_send.setOnClickListener { sendButtonClicked() }
        checkChatRoom()

    }

    /**
     * 전송 버튼 이벤트
     */
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
            chatRoomRef.child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener {
                message_a_et_input.setText("")
            }
        }

    }

    /**
     * 기존 채팅방이 있는지 체크 후에 채팅방 들어감
     */
    private fun checkChatRoom() {
        chatRoomRef.orderByChild("users/$uid").equalTo(true).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                dataSnapshot!!.children.forEach {
                    val chat = it.getValue(Chat::class.java)
                    if ( chat!!.users.containsKey(destinationUid) ) {
                        chatRoomUid = it.key
                        message_a_btn_send.isEnabled = true

                        messageRecyclerView = findViewById<RecyclerView>(R.id.message_a_recyclerView).apply {
                            layoutManager = LinearLayoutManager(this@MessageActivity)
                            adapter = messageAdapter
                        }
                        getFriendInfo()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError?) {
            }

        })
    }

    /**
     * 상대방 정보 획득
     */
    private fun getFriendInfo() {
        FirebaseDatabase.getInstance().getReference("users").child(destinationUid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                messageAdapter.setFriend(dataSnapshot!!.getValue(User::class.java)!!)
                getMessageList(chatRoomUid!!)
            }

            override fun onCancelled(p0: DatabaseError?) {
            }

        })
    }

    /**
     * 채팅 메시지 정보 획득
     */
    private fun getMessageList(chatRoomUid: String) {
        FirebaseDatabase.getInstance().getReference("chat_rooms").child(chatRoomUid).child("comments").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                messageAdapter.clearItem()

                dataSnapshot!!.children.forEach {
                    messageAdapter.addItem(it.getValue(Chat.Companion.Comment::class.java)!!)

                }

                // 스크롤 맨 아래로
                messageRecyclerView.scrollToPosition(messageAdapter.itemCount -1)

            }

            override fun onCancelled(p0: DatabaseError?) {
            }

        })
    }
}
