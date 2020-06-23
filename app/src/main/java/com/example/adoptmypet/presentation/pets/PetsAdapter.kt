package com.example.adoptmypet.presentation.pets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptmypet.databinding.ItemPetBinding
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.utils.GlideUtil

class PetsAdapter(private val items: List<Pet>, private val clickAdapter: PetItemInterface) :
    RecyclerView.Adapter<PetsAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return VH(
            ItemPetBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], clickAdapter)
    }

    class VH(
        private val view: ItemPetBinding
    ) : RecyclerView.ViewHolder(view.root) {
        fun bind(
            item: Pet,
            clickAdapter: PetItemInterface
        ) {
            GlideUtil.loadImage(view = view.ivAnimal, id = item.petId)
            view.item = item
            view.btnErase.setOnClickListener {
                clickAdapter.onEraseClicked(item)
            }
            view.btnCandidates.setOnClickListener {
                clickAdapter.onSeeCandidatesClicked(item)
            }
        }
    }

    interface PetItemInterface {
        fun onEraseClicked(item: Pet)
        fun onSeeCandidatesClicked(item: Pet)
    }
}