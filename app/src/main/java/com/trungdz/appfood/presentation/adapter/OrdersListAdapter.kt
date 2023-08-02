package com.trungdz.appfood.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trungdz.appfood.data.model.OrderItem
import com.trungdz.appfood.databinding.ItemOrderBinding

class OrdersListAdapter(var orderList: List<OrderItem>, val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = orderList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = orderList[position]
        if (holder is ItemViewHolder) {
            holder.bind(item)
        }
    }

    private inner class ItemViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onClick(orderList[adapterPosition], adapterPosition)
            }
        }

        fun bind(item: OrderItem) {
            binding.orderCode.text = "#${item.id_order}"
            when (item.status) {
                0 -> binding.orderStatus.text = "Chờ xác nhận"
                1 -> binding.orderStatus.text = "Đã xác nhận"
                2 -> binding.orderStatus.text = "Đã hủy"
            }
            binding.orderTime.text = item.datetime
        }
    }

    interface Listener {
        fun onClick(item: OrderItem, position: Int)
    }

}