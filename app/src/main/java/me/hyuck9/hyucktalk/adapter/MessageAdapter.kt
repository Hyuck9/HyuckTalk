package me.hyuck9.hyucktalk.adapter

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_message.view.*
import me.hyuck9.hyucktalk.R
import me.hyuck9.hyucktalk.model.Chat
import me.hyuck9.hyucktalk.model.User

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.Holder>() {

    /**
     * 상대방 User 정보
     */
    private lateinit var friend: User

    /**
     * 메세지 정보
     */
    private val comments = mutableListOf<Chat.Companion.Comment>()

    /**
     * 자신의 uid
     */
    private  var uid: String = FirebaseAuth.getInstance().currentUser!!.uid

    fun setFriend(user: User) {
        friend = user
    }

    fun addItem(comment: Chat.Companion.Comment) {
        comments.add(comment)
        notifyDataSetChanged()
    }

    fun clearItem() {
        comments.clear()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val retView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message, parent, false)
        return Holder(retView)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        comments[position].let {comment ->
            with(holder) {

                if ( comment.uid.equals(uid) ) {
                    // 내가 보낸 메세지
                    tvMessage.setBackgroundResource(R.drawable.right_bubble)
                    linearLayoutDestination.visibility = View.INVISIBLE
                    linearLayoutMain.gravity = Gravity.END

                } else {
                    // 상대방이 보낸 메세지
                    Glide.with(itemView.context)
                            .load(friend.profileImageUrl)
                            .apply(RequestOptions().circleCrop())
                            .into(ivProfile)
                    tvName.text = friend.userName
                    linearLayoutDestination.visibility = View.VISIBLE
                    tvMessage.setBackgroundResource(R.drawable.left_bubble)
                    linearLayoutMain.gravity = Gravity.START
                }

                // 메세지 내용 및 사이즈 셋팅
                tvMessage.text = comment.message
                tvMessage.textSize = 25f

            }
        }
    }

    override fun getItemCount(): Int = comments.size


    inner class Holder(view: View?) : RecyclerView.ViewHolder(view) {
        val tvMessage = itemView.message_i_tv_message!!
        val tvName = itemView.message_i_tv_name!!
        val ivProfile = itemView.message_i_iv_profile!!
        val linearLayoutDestination = itemView.message_i_linearLayout_destination!!
        val linearLayoutMain = itemView.message_i_linearLayout_main!!
    }

}