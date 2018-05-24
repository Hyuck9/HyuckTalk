package me.hyuck9.hyucktalk

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.hyuck9.hyucktalk.fragment.PeopleFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager.beginTransaction().replace(R.id.main_a_frameLayout, PeopleFragment()).commit()
    }
}
