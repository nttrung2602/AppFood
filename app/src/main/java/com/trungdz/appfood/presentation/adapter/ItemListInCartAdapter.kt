package com.trungdz.appfood.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.trungdz.appfood.data.model.modelresponse.ItemInCart
import com.trungdz.appfood.databinding.ItemInCartBinding

class ItemListInCartAdapter(val itemList: List<ItemInCart>, val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    object Action {
        val INCREASE_QUANTITY_ITEM = 1
        val DECREASE_QUANTITY_ITEM = 2
    }


    var isUpdating = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemInCartBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is ItemViewHolder -> {
                holder.bind(item)
            }
        }
    }

    fun checkValidCounterQuantityOfITem(position: Int, action: Int): Boolean {
        val item = itemList[position]
        if (action == Action.INCREASE_QUANTITY_ITEM) {
            if (item.amount + 1 > item.quantity) {
                return false
            }
        } else if (action == Action.DECREASE_QUANTITY_ITEM) {
            if (item.amount - 1 == 0) {
                return false
            }
        }

        return true
    }

    fun deleteItem(position: Int) {
        (itemList as ArrayList).removeAt(position)
        notifyItemRemoved(position)
    }

    fun setUpdating(position: Int) {
        isUpdating = true
        notifyItemChanged(position)
    }

    fun increaseQuantity(position: Int) {
        var item = itemList[position]
        item.amount += 1
        isUpdating = false
        notifyItemChanged(position)
    }

    fun decreaseQuantity(position: Int) {
        var item = itemList[position]
        item.amount -= 1
        isUpdating = false
        notifyItemChanged(position)
    }

    private inner class ItemViewHolder(val binding: ItemInCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                listener.onClickItem(itemList[adapterPosition])
            }

            binding.imageDesQuantity.setOnClickListener {
                listener.onClickDesQuantity(itemList[adapterPosition], adapterPosition)
            }

            binding.imageIncQuantity.setOnClickListener {
                listener.onClickIncQuantity(itemList[adapterPosition], adapterPosition)
            }

            binding.imageDelete.setOnClickListener {
                listener.onClickDelete(itemList[adapterPosition], adapterPosition)
            }
        }

        fun bind(item: ItemInCart) {
            with(binding) {
                if (item.image != null)
                    Picasso.get().load(item.image).into(imgItem)
                itemName.text = item.name
                itemQuantity.text = "SL còn: ${item.quantity}"
                itemPrice.text = String.format("%,2d", item.price) + " đồng"
                counterQuantity.text = item.amount.toString()

                // setEnable button
                binding.imageIncQuantity.isEnabled = !isUpdating
                binding.imageDesQuantity.isEnabled = !isUpdating

            }

        }
    }

    interface Listener {
        fun onClickDesQuantity(item: ItemInCart, position: Int)
        fun onClickIncQuantity(item: ItemInCart, position: Int)
        fun onClickDelete(item: ItemInCart, position: Int)
        fun onClickItem(item: ItemInCart)
    }
}