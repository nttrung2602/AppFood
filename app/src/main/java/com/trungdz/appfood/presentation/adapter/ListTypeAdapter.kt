package com.trungdz.appfood.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.Types
import com.trungdz.appfood.databinding.ItemCategoryAllBinding
import com.trungdz.appfood.databinding.ItemCategoryBinding

class TypeWrapper(
    val isItemAll: Boolean,
    val type: Types?,
    @DrawableRes val iconResId: Int
) {
    constructor(type: Types?) : this(
        isItemAll = type == null,
        type = type,
        iconResId = when (type?.id_type) {
            1 -> R.drawable.burger
            2 -> R.drawable.drink
            3 -> R.drawable.cupcake
            4 -> R.drawable.pasta
            5 -> R.drawable.pizza
            else -> R.drawable.other
        }
    )
}

class ListTypeAdapter(
    var listType: List<TypeWrapper>,
    private val listener: Listener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectedPosition = 0

    override fun getItemCount() = listType.size

    override fun getItemViewType(position: Int): Int {
        return if (listType[position].isItemAll) {
            ViewHolderType.ItemAll.ordinal
        } else {
            ViewHolderType.Normal.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewHolderType.ItemAll.ordinal -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoryAllBinding.inflate(inflater, parent, false)

                ItemAllViewHolder(binding)
            }

            ViewHolderType.Normal.ordinal -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoryBinding.inflate(inflater, parent, false)

                NormalViewHolder(binding)
            }

            else -> error("")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listType[position]

        if (holder is BaseViewHolder) {
            holder.bind(item)
        }
    }

    private fun onItemClick(item: TypeWrapper, position: Int) {
        val oldSelectedPosition = selectedPosition
        selectedPosition = position

        notifyItemChanged(oldSelectedPosition)
        notifyItemChanged(selectedPosition)

        listener.onClick(item)
    }

    private abstract inner class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                onItemClick(listType[adapterPosition], adapterPosition)
            }
        }

        @CallSuper
        open fun bind(type: TypeWrapper) {
            val setBackgroundColorItemIsSelected =
                itemView.context.resources.getDrawable(R.drawable.item_category_border_selected)
            val setBackgroundColorItemNotIsSelected =
                itemView.context.resources.getDrawable(R.drawable.item_category_border)

            when (selectedPosition) {
                adapterPosition -> {
                    itemView.background =setBackgroundColorItemIsSelected
                    itemView.isClickable=false
                }
                else -> {
                    itemView.background =setBackgroundColorItemNotIsSelected
                    itemView.isClickable=true
                }
            }
        }
    }

    private inner class ItemAllViewHolder(binding: ItemCategoryAllBinding) :
        BaseViewHolder(binding.root)

    private inner class NormalViewHolder(private val binding: ItemCategoryBinding) :
        BaseViewHolder(binding.root) {

        override fun bind(type: TypeWrapper) {
            super.bind(type)

            binding.typeName.text = type.type?.name

            Picasso.get().load(type.iconResId).into(binding.typeImage)

//            Glide.with(binding.root).load(type.iconResId).into(binding.typeImage)
        }
    }

    enum class ViewHolderType {
        ItemAll,
        Normal
    }

    interface Listener {
        fun onClick(types: TypeWrapper)
    }
}
