package com.trungdz.appfood.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.ItemReview
import com.trungdz.appfood.databinding.ItemReviewBinding

class ListReviewsAdapter(val listReviews: List<ItemReview>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(inflater, parent, false)

        return ItemReviewViewHolder(binding)
    }

    override fun getItemCount(): Int = listReviews.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemReview = listReviews[position]

        if (holder is ItemReviewViewHolder) {
            holder.bind(itemReview)
        }
    }

    inner class ItemReviewViewHolder(val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemReview: ItemReview) {
            val image = itemReview.image
            val name = itemReview.name
            val time = itemReview.datetime
            val comment = itemReview.comment
            val rating = itemReview.rating

            with(binding) {
                if (image != null)
                    Picasso.get().load(image).error(R.drawable.user).into(userImage)
                userName.text = name
                commentTime.text = time
                userComment.text = "\"${comment}\""
                ratingBar.rating = rating.toFloat()
            }
        }
    }
}