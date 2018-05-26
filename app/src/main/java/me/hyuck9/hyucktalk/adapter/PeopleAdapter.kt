package me.hyuck9.hyucktalk.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_friend.view.*
import me.hyuck9.hyucktalk.R
import me.hyuck9.hyucktalk.model.User

class PeopleAdapter(private val onClick: ((User) -> Unit) ) : RecyclerView.Adapter<PeopleAdapter.Holder>() {

    /**
     * 메세지 정보
     */
    private val users = mutableListOf<User>()

    fun addItem(user: User) {
        users.add(user)
        notifyDataSetChanged()
    }

    fun clearItem() {
        users.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val retView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_friend, parent, false)
        return Holder(retView)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
//        holder.bindUsers(users[position])
        users[position].let {user ->
            with(holder) {
                Glide.with(itemView.context)
                        .load(user.profileImageUrl)
                        .apply(RequestOptions().circleCrop())
                        .into(tvFriendProfile)
                tvFriendId.text = user.userName
                itemView.setOnClickListener { onClick(user) }
            }
        }
    }

    override fun getItemCount(): Int = users.size


    inner class Holder(view: View?) : RecyclerView.ViewHolder(view) {
        val tvFriendProfile = itemView.friend_i_imageView!!
        val tvFriendId = itemView.friend_i_textView!!
//        fun bindUsers(user: User) {
//            with(user) {
//                itemView.friend_i_textView.text = user.userName
//            }
//        }
    }

}