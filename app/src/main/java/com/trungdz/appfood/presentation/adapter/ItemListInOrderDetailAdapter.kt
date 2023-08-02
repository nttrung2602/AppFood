package com.trungdz.appfood.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.trungdz.appfood.data.model.ItemInOrder
import com.trungdz.appfood.databinding.ItemInOrderDetailBinding

class ItemListInOrderDetailAdapter(var itemList: List<ItemInOrder>, val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var orderConfirmed = true
    fun setOrderConfirmed() {
        orderConfirmed = false
    }

    fun setReviewed(position: Int) {
        itemList[position].reviewed = 1
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemInOrderDetailBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }


    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]

        if (holder is ItemViewHolder) {
            holder.bind(item)
        }
    }

    private inner class ItemViewHolder(val binding: ItemInOrderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemInOrder) {
            with(binding) {
                Picasso.get().load(item.image).into(itemImage)
                itemName.text = item.name
                itemQuantity.text = "X " + item.quantity.toString()
                itemPrice.text = String.format("%,2d", item.price) + " VND"
                itemTotalPrice.text = String.format("%,2d", item.quantity * item.price) + " VND"

                if (item.reviewed == 1 || !orderConfirmed) {
                    commentImage.visibility = View.GONE
                } else {
                    commentImage.visibility = View.VISIBLE
                }
                commentImage.setOnClickListener {
                    listener.onClickComment(item, adapterPosition)
                }
            }
        }
    }

    interface Listener {
        fun onClickComment(item: ItemInOrder, position: Int)
    }
}