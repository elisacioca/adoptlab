package com.example.adoptmypet.presentation.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptmypet.databinding.ItemFeedBinding
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.utils.GlideUtil


class FeedAdapter(private val items: List<Pet>, private val clickAdapter: FeedItemInterface) :
    RecyclerView.Adapter<FeedAdapter.VH>() {

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
        holder.bind(items[position], clickAdapter)
    }

    class VH(
        private val view: ItemFeedBinding
    ) : RecyclerView.ViewHolder(view.root) {
        fun bind(
            item: Pet,
            clickAdapter: FeedItemInterface
        ) {
            GlideUtil.loadImage(view = view.ivAnimal, id = item.petId)
            view.item = item
            view.btnReadMore.setOnClickListener {
                clickAdapter.onReadMoreClicked(item)
            }
            view.btnAdopt.setOnClickListener {
                clickAdapter.onAdoptClicked(item)
            }
        }
    }

    interface FeedItemInterface {
        fun onReadMoreClicked(item: Pet)
        fun onAdoptClicked(item: Pet)
    }
}
