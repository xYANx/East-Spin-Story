package com.eastking.spins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.win_fragment.balanceText
import kotlinx.android.synthetic.main.win_fragment.replay

class ResultFragment(private val layout: Int, private val balance: Int) : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(layout, container, false)

    override fun onStart() {
        super.onStart()
        if(layout == R.layout.win_fragment){balanceText.text = balance.toString()} else balanceText.text = balance.toString()
        replay.setOnClickListener{
            activity!!.onBackPressed()
        }
    }
}