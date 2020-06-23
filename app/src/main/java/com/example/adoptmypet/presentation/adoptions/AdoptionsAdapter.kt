package com.example.adoptmypet.presentation.adoptions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptmypet.databinding.ItemAdoptionBinding
import com.example.adoptmypet.models.Adoption
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.utils.GlideUtil

class AdoptionsAdapter(private val items: List<Adoption>, private val clickAdapter: AdoptionItemInterface) :
    RecyclerView.Adapter<AdoptionsAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return VH(
            ItemAdoptionBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], clickAdapter)
    }

    class VH(
        private val view: ItemAdoptionBinding
    ) : RecyclerView.ViewHolder(view.root) {
        fun bind(
            item: Adoption,
            clickAdapter: AdoptionItemInterface
        ) {
            view.item = item
            view.buttonDetails.setOnClickListener {
                clickAdapter.onDetailsItemClicked(item)
            }
        }
    }

    interface AdoptionItemInterface {
        fun onDetailsItemClicked(item: Adoption)
    }
}