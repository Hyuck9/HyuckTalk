package me.hyuck9.hyucktalk.ui.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.hyuck9.hyucktalk.R
import me.hyuck9.hyucktalk.R.id.action_chat
import me.hyuck9.hyucktalk.R.id.action_people
import me.hyuck9.hyucktalk.ui.fragment.ChatFragment
import me.hyuck9.hyucktalk.ui.fragment.PeopleFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_a_bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                action_people -> {
                    fragmentManager.beginTransaction().replace(R.id.main_a_frameLayout, PeopleFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                action_chat -> {
                    fragmentManager.beginTransaction().replace(R.id.main_a_frameLayout, ChatFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }

            return@setOnNavigationItemSelectedListener false

        }
    }

}
