package com.eastking.spins

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.game_fragment.*
import java.text.NumberFormat
import java.util.concurrent.ThreadLocalRandom

class GameFragment(private val contextMainActivity: Context) : Fragment() {
    private var balance = 1500
    private var bet = 50
    private var jackpot = 0
    private var listItems = mutableListOf<Int>()
    private var progress = 0
    private var level = 1
    private var positionOne = ThreadLocalRandom.current().nextInt(1, 10)
    private var positionTwo = ThreadLocalRandom.current().nextInt(10, 20)
    private var positionThree = ThreadLocalRandom.current().nextInt(15, 30)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.game_fragment, container, false)

    override fun onPause() {
        super.onPause()
        setPreferences()
    }

    override fun onStart() {
        super.onStart()
        val listOne = setList()
        val listTwo: MutableList<Int> = listOne.shuffled() as MutableList<Int>
        val listThree: MutableList<Int> = listOne.shuffled() as MutableList<Int>

        getPreferences()
        if (jackpot == 0) {
            jackpot = ThreadLocalRandom.current().nextInt(5000000, 9000000)
        }

        jackpotText.text = NumberFormat.getInstance().format(jackpot).toString()
        balanceText.text = (balance - bet).toString()
        betText.text = bet.toString()
        progressBar.progress = progress
        levelText.text = level.toString()

        setAdapter(recyclerOne, 1, listOne)
        setAdapter(recyclerTwo, 2, listTwo)
        setAdapter(recyclerThree, 3, listThree)
        (recyclerOne.adapter as SlotListAdapter).updateList(listOne)
        (recyclerTwo.adapter as SlotListAdapter).updateList(listTwo)
        (recyclerThree.adapter as SlotListAdapter).updateList(listThree)

        plus.setOnClickListener {
            if (bet <= 90) {
                bet += 10
                betText.text = bet.toString()
                balanceText.text = (balance - bet).toString()
            }
        }

        minus.setOnClickListener {
            if (bet >= 20) {
                bet -= 10
                betText.text = bet.toString()
                balanceText.text = (balance - bet).toString()
            }
        }

        scrollButton.setOnTouchListener { view: View?, motionEvent: MotionEvent? ->
            val action = motionEvent!!.action
            if (action == MotionEvent.ACTION_DOWN) {
                shiningStar.visibility = View.VISIBLE
            }
            if (action == MotionEvent.ACTION_UP) {
                shiningStar.visibility = View.INVISIBLE
            }
            false
        }
        scrollButton.setOnClickListener {
            val i1 = ThreadLocalRandom.current().nextInt(1, 200)
            val i2 = ThreadLocalRandom.current().nextInt(1, 200)
            val i3 = ThreadLocalRandom.current().nextInt(1, 200)
            positionOne += i1 + 10
            positionTwo += i2 + 20
            positionThree += i3 + 30
            recyclerOne.smoothScrollToPosition(positionOne)
            Thread(Runnable {
                kotlin.run {
                    Thread.sleep(500)
                    recyclerTwo.smoothScrollToPosition(positionTwo)
                }
            }).start()

            Thread(Runnable {
                kotlin.run {
                    Thread.sleep(1000)
                    recyclerThree.smoothScrollToPosition(positionThree)
                }
            }).start()
        }
    }

    private fun checkItem(position: Int, list: MutableList<Int>) {
        var item = 0
        item = if (position == 0) {
            list[list.size - 1]
        } else list[position - 1]
        listItems.add(item)
        if (listItems.size == 3) {
            Thread.sleep(100)
            if (listItems[0] == R.drawable.mcfo_pic2_lucky_tree_000 && listItems[1] == R.drawable.mcfo_pic2_lucky_tree_000 && listItems[2] == R.drawable.mcfo_pic2_lucky_tree_000) {
                upLevelProgress()
                balance += jackpot / 2
                (contextMainActivity as MainActivity).addFragment(R.layout.win_fragment, balance)
            } else if (listItems[0] == listItems[1] && listItems[1] == listItems[2]) {
                upLevelProgress()
                balance += bet * 3
                (contextMainActivity as MainActivity).addFragment(R.layout.win_fragment, balance)
            } else if (listItems[0] == listItems[1] || listItems[1] == listItems[2] || listItems[0] == listItems[2]) {
                upLevelProgress()
                balance += bet * 2
                (contextMainActivity as MainActivity).addFragment(R.layout.win_fragment, balance)
            } else {
                balance -= bet
                jackpot += bet
                (contextMainActivity as MainActivity).addFragment(R.layout.lose_fragment, balance)
            }
            balanceText.text = (balance - bet).toString()
            jackpotText.text = jackpot.toString()
            listItems.clear()
        }
    }

    private fun getPreferences() {
        val sharePreferences =
            contextMainActivity.getSharedPreferences("save", Context.MODE_PRIVATE)
        if (sharePreferences.contains("level")) {
            level = sharePreferences.getInt("level", 1)
            progress = sharePreferences.getInt("progress", 0)
            jackpot = sharePreferences.getInt(
                "jackpot",
                ThreadLocalRandom.current().nextInt(5000000, 9000000)
            )
            balance = sharePreferences.getInt("balance", 1500)
            bet = sharePreferences.getInt("bet", 50)
        }
    }

    private fun setPreferences() {
        val sharePreferences =
            contextMainActivity.getSharedPreferences("save", Context.MODE_PRIVATE)
        val edit = sharePreferences.edit()
        edit.putInt("level", levelText.text.toString().toInt())
        edit.putInt("progress", progress)
        edit.putInt("jackpot", jackpot)
        edit.putInt("balance", balance)
        edit.putInt("bet", bet)
        edit.apply()
    }

    private fun upLevelProgress() {
        progress = progressBar.progress
        progress += 25
        if (progress == 100) {
            progress = 0
            level = levelText.text.toString().toInt()
            levelText.text = (level + 1).toString()
        }
        progressBar.progress = progress
    }

    private fun setList(): MutableList<Int> {
        val list = mutableListOf<Int>()
        list.add(R.drawable.mcfo_pic2_lucky_tree_000)
        list.add(R.drawable.mcfo_pic4_pot_000)
        list.add(R.drawable.tree)
        list.add(R.drawable.mcfo_royal_k_000)
        list.add(R.drawable.mcfo_royal_10_000)
        list.add(R.drawable.mcfo_royal_9_000)
        list.add(R.drawable.mcfo_royal_a_000)
        list.add(R.drawable.mcfo_royal_j_000)
        list.add(R.drawable.mcfo_royal_q_000)
        list.add(R.drawable.mcfo_pic1_lucky_cat_000)
        list.add(R.drawable.wl_mr_chen_landing_000)
        list.add(R.drawable.p3_oranges_on_lucky_coin_000)
        return list
    }

    private fun setAdapter(recycler: RecyclerView, id: Int, list: MutableList<Int>) {

        recycler.apply {
            adapter = SlotListAdapter()
            layoutManager = object : LinearLayoutManager(context, RecyclerView.VERTICAL, false) {
                override fun onScrollStateChanged(state: Int) {
                    super.onScrollStateChanged(state)
                    if (state == 0) checkItem(checkRecycler(id) % list.size, list)
                }
            }
            recycler.setOnTouchListener { v, event ->
                val action = event.action
                if (action == MotionEvent.ACTION_DOWN) {
                    recycler.smoothScrollToPosition(checkRecycler(id))
                }
                if (action == MotionEvent.ACTION_UP) {
                    recycler.smoothScrollToPosition(checkRecycler(id))
                }
                true
            }
            scrollToPosition(checkRecycler(id))
        }
    }

    private fun checkRecycler(id: Int): Int {
        var position = 0
        when (id) {
            1 -> position = positionOne
            2 -> position = positionTwo
            3 -> position = positionThree
        }
        return position
    }
}