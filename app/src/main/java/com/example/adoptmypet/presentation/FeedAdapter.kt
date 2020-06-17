package com.example.adoptmypet.presentation.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptmypet.databinding.ItemFeedBinding
import com.example.adoptmypet.models.Pet

class FeedAdapter(private val items: List<Pet>) : RecyclerView.Adapter<FeedAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return VH(
            ItemFeedBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    class VH(private val view: ItemFeedBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: Pet) {
            view.item = item
        }
    }
}
