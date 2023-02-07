package com.example.a7minworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minworkout.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val items: ArrayList<String>):RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemHistoryRowBinding):RecyclerView.ViewHolder(binding.root){
        val clHistoryItemMain = binding.clHistoryItemMain
        val tvItemHistoryNo = binding.tvItemHistoryNo
        val tvItemHistory = binding.tvItemHistory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun getItemCount(): Int {
        return  items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date: String = items.get(position)
        holder.tvItemHistoryNo.text = (position+1).toString()
        holder.tvItemHistory.text = date

        if (position%2 == 0) {
            holder.clHistoryItemMain.setBackgroundColor(
                Color.parseColor( "#B4B4B4")
            )
        } else {
            holder.clHistoryItemMain.setBackgroundColor(
                Color.parseColor( "#FFFFFF")
            )
        }
    }
}