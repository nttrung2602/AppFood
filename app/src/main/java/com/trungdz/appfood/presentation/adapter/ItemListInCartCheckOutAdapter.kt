package com.trungdz.appfood.presentation.adapter

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trungdz.appfood.data.model.modelresponse.ItemInCart
import com.trungdz.appfood.databinding.ItemInCartCheckoutBinding

class ItemListInCartCheckOutAdapter(var itemList: List<ItemInCart>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemInCartCheckoutBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]

        if (holder is ItemViewHolder) {
            holder.bind(item)
        }
    }

    private inner class ItemViewHolder(val binding: ItemInCartCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemInCart) {
            binding.itemName.text = item.name
            binding.itemPrice.text = String.format("%,2d", item.price)
            binding.itemQuantity.text = item.amount.toString()
        }
    }
}