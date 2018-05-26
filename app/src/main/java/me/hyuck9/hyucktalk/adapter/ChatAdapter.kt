package me.hyuck9.hyucktalk.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_chat.view.*
import me.hyuck9.hyucktalk.R
import me.hyuck9.hyucktalk.model.Chat

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.Holder>() {

    /**
     * 메세지 정보
     */
    private val chats = mutableListOf<Chat>()

    /**
     * 자신의 uid
     */
    private val uid: String = FirebaseAuth.getInstance().currentUser!!.uid

    fun addItem(chat: Chat) {
        chats.add(chat)
        notifyDataSetChanged()
    }

    fun clearItem() {
        chats.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val retView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat, parent, false)
        return Holder(retView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        chats[position].let { chat ->
            with(holder) {
                tvTitle.text = "테스트"
            }
        }
    }

    override fun getItemCount(): Int = chats.size

    inner class Holder(view: View?) : RecyclerView.ViewHolder(view) {
        val iv = itemView.chat_i_imageView!!
        val tvTitle = itemView.chat_i_tv_title!!
    }
}