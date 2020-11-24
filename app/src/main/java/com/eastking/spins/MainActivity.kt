package com.eastking.spins

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.gameActivityFragment, GameFragment(this))
            .commit()
    }

    fun addFragment(layout: Int, balance: Int){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.gameActivityFragment, ResultFragment(layout, balance))
            .addToBackStack("result")
            .commit()
    }
}