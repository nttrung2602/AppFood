package com.trungdz.appfood.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.trungdz.appfood.data.model.Item
import com.trungdz.appfood.databinding.ItemInSearchBinding

class ItemListInSearchAdapter(var itemList: List<Item>, val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemInSearchBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]

        if (holder is ItemViewHolder) {
            holder.bind(item)
        }
    }

    inner class ItemViewHolder(val binding: ItemInSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                listener.onClickItem(itemList[adapterPosition], adapterPosition)
            }
        }

        fun bind(item: Item) {
            binding.itemName.text = item.name
            binding.itemPrice.text = String.format("%,2d", item.price) + " đồng"
            if (item.image != null) {
                Picasso.get().load(item.image).into(binding.imageView6)
            }
        }
    }

    interface Listener {
        fun onClickItem(item: Item, position: Int)
    }
}