package me.hyuck9.hyucktalk.ui.fragment


import android.app.ActivityOptions
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
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
import me.hyuck9.hyucktalk.adapter.PeopleAdapter
import me.hyuck9.hyucktalk.model.User
import me.hyuck9.hyucktalk.ui.activity.MessageActivity

/**
 * A simple [Fragment] subclass.
 *
 */
class PeopleFragment : Fragment() {

    private val users = mutableListOf<User>()
    private val peopleAdapter = PeopleAdapter(users) {
        Intent(activity, MessageActivity::class.java).let { intent ->
            intent.putExtra("destinationUid", it.uid)
            ActivityOptions.makeCustomAnimation(activity, R.anim.from_right, R.anim.to_left).let { option ->
                startActivity(intent, option.toBundle())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_people, container, false)
        /*val recyclerView = (view.findViewById(R.id.people_f_recyclerView) as RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(inflater.context)
        recyclerView.adapter = peopleAdapter
        // apply 사용하여 아래 코드로 간소화*/
        view.findViewById<RecyclerView>(R.id.people_f_recyclerView).apply {
            layoutManager = LinearLayoutManager(inflater.context)
            adapter = peopleAdapter
        }
        setupUsers()
        return view
    }

    /**
     * 친구 목록 셋팅 (본인 제외 모든 사용자)
     */
    private fun setupUsers() {
        val myUid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    users.clear()
                    dataSnapshot!!.children.forEach {
                        it.getValue(User::class.java).let { user ->
                            if ( user!!.uid.equals(myUid) ) return@forEach
                            users.add(user)
                        }

                    }
                    peopleAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError?) {
                }
            })
    }


}
