package com.trungdz.appfood.presentation.customReviewDialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.modelrequest.CreateReviewRequest
import com.trungdz.appfood.data.model.ItemInOrder
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.databinding.LayoutReviewItemBinding
import com.trungdz.appfood.presentation.viewmodel.ReviewDialogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewDialog(val listener: Listener) : DialogFragment() {

    object Static {
        fun newInstance(item: ItemInOrder,listener:Listener): ReviewDialog {
            val reviewDialog = ReviewDialog(listener)

            val itemJson = Gson().toJson(item)
            val args = bundleOf("itemJson" to itemJson)

            reviewDialog.arguments = args
            return reviewDialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        this.isCancelable = false
        return inflater.inflate(R.layout.layout_review_item, container)
    }

    lateinit var binding: LayoutReviewItemBinding
    private val viewModel: ReviewDialogViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LayoutReviewItemBinding.bind(view)

        val itemJson = requireArguments().getString("itemJson")
        val item = Gson().fromJson(itemJson, ItemInOrder::class.java)

        binding.itemName.text = "Đánh giá ${item.name}"
        setEventListener()
        setObservers()
    }

    private fun setObservers() {
        viewModel.statusMessageCreateReview.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("ReviewDialog", "is loading")
                is Resource.Success -> {
                    Toast.makeText(context, "${response.data?.message}", Toast.LENGTH_SHORT).show()
                    listener.onClickConfirm(true)
                    dismiss()
                }
                is Resource.Error -> Toast.makeText(
                    context,
                    "${response.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun removeObservers() {
        viewModel.statusMessageCreateReview.removeObservers(this)
    }

    private fun setEventListener() {
        binding.btnConfirm.setOnClickListener {
            val rating = binding.ratingBar2.rating.toInt()
            val comment = binding.editTextComment.text.toString()
            val reviewRequest = CreateReviewRequest(rating, comment)
//            val image=null
            val itemJson = requireArguments().getString("itemJson")
            val item = Gson().fromJson(itemJson, ItemInOrder::class.java)
            viewModel.createReview(item.id_item, item.id_order, reviewRequest)
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    interface Listener{
        fun onClickConfirm(isReviewed:Boolean)
    }
}