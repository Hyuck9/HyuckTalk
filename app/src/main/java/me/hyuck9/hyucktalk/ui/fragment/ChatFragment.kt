package me.hyuck9.hyucktalk.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import me.hyuck9.hyucktalk.R
import me.hyuck9.hyucktalk.adapter.ChatAdapter
import me.hyuck9.hyucktalk.model.Chat

class ChatFragment : Fragment() {

    private val chatAdapter = ChatAdapter()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_chat, container, false)

        view.findViewById<RecyclerView>(R.id.chat_f_recyclerView).apply {
            layoutManager = LinearLayoutManager(inflater.context)
            adapter = chatAdapter
        }
        setUpChats()
        return view
    }

    private fun setUpChats() {

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().getReference("chat_rooms").orderByChild("users/$uid").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                chatAdapter.clearItem()
                dataSnapshot!!.children.forEach {
                    chatAdapter.addItem(it.getValue(Chat::class.java)!!)
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
            }

        })
    }
}