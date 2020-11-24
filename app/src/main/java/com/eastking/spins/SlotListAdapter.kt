package com.eastking.spins

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.slot_list_adapter.view.*

class SlotListAdapter() : RecyclerView.Adapter<SlotListAdapter.SlotListViewHolder>(){
    private var list = mutableListOf<Int>()
    class SlotListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)/*, ViewHolderWithDetails<OrderClass>*/{
        fun bind(image : Int, position: Int){
            itemView.apply {
                imageView.isClickable = false
                imageView.setImageDrawable(resources.getDrawable(image))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slot_list_adapter, parent, false)
        return SlotListViewHolder(view)
    }

    override fun getItemCount() = Int.MAX_VALUE

    fun updateList(ordersList: MutableList<Int>) {
        list.apply {
            clear()
            addAll(ordersList)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SlotListViewHolder, position: Int) {
        val actualPosition = position % list.size
        holder.bind(list[actualPosition], actualPosition)
    }
}