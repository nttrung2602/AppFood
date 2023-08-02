package com.trungdz.appfood.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.ItemReview
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.databinding.FragmentReviewBinding
import com.trungdz.appfood.presentation.adapter.ListReviewsAdapter
import com.trungdz.appfood.presentation.viewmodel.ReviewFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ReviewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // variable
    private val reviewActivityViewModel: ReviewFragmentViewModel by viewModels()
    lateinit var listReviewsAdapter: ListReviewsAdapter
    lateinit var binding: FragmentReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentReviewBinding.bind(view)

        val idItem=requireArguments().getInt("id_item")
        reviewActivityViewModel.getAllReviewByItem(idItem)
        reviewActivityViewModel.listReviews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d("ReviewActivity", "is loading")
                }

                is Resource.Success -> {
                    Log.d("ReviewActivity", "is success")

                    val listReviews: List<ItemReview> = response.data as ArrayList<ItemReview>
                    listReviewsAdapter = ListReviewsAdapter(listReviews)
                    binding.recyclerView.adapter = listReviewsAdapter
                    listReviewsAdapter.notifyDataSetChanged()
                }

                is Resource.Error -> {
                    Log.d("ReviewActivity", "is error")
                }
            }
        }

        binding.backImage.setOnClickListener {
            findNavController().popBackStack()
        }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReviewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}