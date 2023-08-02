package com.trungdz.appfood.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.trungdz.appfood.data.model.ItemWishlist
import com.trungdz.appfood.databinding.ItemWishlistBinding

class WishlistAdapter(
    var wishlistList: List<ItemWishlist>,
    val listener: WishlistAdapter.Listener,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun removeItem(position: Int) {
        (wishlistList as ArrayList<ItemWishlist>).removeAt(position)
        notifyItemRemoved(position)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWishlistBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = wishlistList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = wishlistList[position]
        if (holder is ItemViewHolder) {
            holder.bind(item)
        }
    }

    inner class ItemViewHolder(val binding: ItemWishlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onClickItem(wishlistList[adapterPosition])
            }

            binding.imgFavourite.setOnClickListener {
                listener.onClickFavorite(wishlistList[adapterPosition], adapterPosition)
            }
        }

        fun bind(item: ItemWishlist) {
            Picasso.get().load(item.image).into(binding.imgItem)
            binding.itemName.text = item.name
            binding.itemPrice.text = "${String.format("%,2d",item.price)} đồng"
        }
    }

    interface Listener {
        fun onClickFavorite(item: ItemWishlist, position: Int)
        fun onClickItem(item: ItemWishlist)
    }
}