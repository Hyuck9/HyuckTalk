package me.hyuck9.hyucktalk.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_message.view.*
import me.hyuck9.hyucktalk.R
import me.hyuck9.hyucktalk.model.Chat

class MessageAdapter(chatRoomUid: String, private val comments : MutableList<Chat.Companion.Comment>) : RecyclerView.Adapter<MessageAdapter.Holder>() {

    init {
        FirebaseDatabase.getInstance().getReference("chat_rooms").child(chatRoomUid).child("comments").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                comments.clear()

                dataSnapshot!!.children.forEach {
                    comments.add(it.getValue(Chat.Companion.Comment::class.java)!!)
                }
                notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError?) {
            }

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val retView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message, parent, false)
        return Holder(retView)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        comments[position].let {comment ->
            with(holder) {
                tvMessage.text = comment.message
            }
        }
    }

    override fun getItemCount(): Int = comments.size


    inner class Holder(view: View?) : RecyclerView.ViewHolder(view) {
        val tvMessage = itemView.message_i_tv_message!!
    }

}