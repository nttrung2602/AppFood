package com.trungdz.appfood.presentation.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.trungdz.appfood.data.model.Item
import com.trungdz.appfood.data.util.Constants
import com.trungdz.appfood.databinding.ItemFoodAndDrinkBinding
import com.trungdz.appfood.databinding.ProgressBarPagenationLoadingBinding

class ListItemAdapter(val listItem: List<Item?>, val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.VIEW_TYPE_LOADING -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ProgressBarPagenationLoadingBinding.inflate(inflater, parent, false)

                LoadingViewHolder(binding)
            }
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemFoodAndDrinkBinding.inflate(inflater, parent, false)
                ItemViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listItem[position]

        if (holder is ItemViewHolder) {
            if (item != null) {
                holder.bind(item)
            }
        }
    }


    fun addData(listData: List<Item?>) {
        when (listItem) {
            is ArrayList -> {
                listItem.addAll(listData)
                notifyDataSetChanged()
            }
        }
    }

    fun addLoadingView() {
        when (listItem) {
            is ArrayList -> {
                listItem.add(null)
                notifyItemInserted(listItem.size - 1)
            }
        }
    }

    fun removeLoadingView() {
        if (listItem.isNotEmpty()) {
            when (listItem) {
                is ArrayList -> {
                    listItem.removeAt(listItem.size - 1)
                    notifyItemRemoved(listItem.size)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (listItem[position] == null) {
            Constants.VIEW_TYPE_LOADING
        } else Constants.VIEW_TYPE_ITEM
    }

    inner class LoadingViewHolder(val binding: ProgressBarPagenationLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ItemViewHolder(val binding: ItemFoodAndDrinkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding) {
                addToCart.setOnClickListener {
//                    listener.onClickButtonCart(listItem[adapterPosition])
                    listItem[adapterPosition]?.let { listener.onClickButtonCart(it) }
                }

                itemView.setOnClickListener {
//                    listener.onClickItem(listItem[adapterPosition])
                        listItem[adapterPosition]?.let { listener.onClickItem(it) }
                    // open item detail screen
                    //...
                }
            }
        }

        fun bind(item: Item) {
            val itemImg = item.image
            val itemName = item.name
            val itemQuantity = item.quantity.toString()
            val itemRating = if (item.rating == null) 0f else item.rating.toFloat()
            val itemPrice = String.format("%,d", item.price)

            with(binding) {
                if (itemImg != null)
                    Picasso.get().load(itemImg).into(itemImage)
                tvItemName.text = itemName
                tvItemQuantity.text = itemQuantity
                ratingBar.apply {
                    rating = itemRating
                }
                tvItemPrice.text = itemPrice
            }
        }
    }

    interface Listener {
        fun onClickButtonCart(item: Item)
        fun onClickItem(item: Item)
    }
}