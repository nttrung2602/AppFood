package com.trungdz.appfood.presentation.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.trungdz.appfood.R
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.data.util.Utils
import com.trungdz.appfood.data.util.cache.ISharedPreference
import com.trungdz.appfood.databinding.FragmentItemDetailBinding
import com.trungdz.appfood.presentation.viewmodel.ItemDetailFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ItemDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class   ItemDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        itemInWishlist = requireContext().resources.getDrawable(R.drawable.item_favorite_wishlist)
        itemNotExistingInWishlist =
            requireContext().resources.getDrawable(R.drawable.item_favorite_not_exist)
    }

    // variable
    private lateinit var itemInWishlist: Drawable
    private lateinit var itemNotExistingInWishlist: Drawable

    lateinit var binding: FragmentItemDetailBinding
    private val viewModel: ItemDetailFragmentViewModel by viewModels()

    @Inject
    lateinit var sharedPreference: ISharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val idItem = requireArguments().getInt("id_item")
        if (sharedPreference.getLoggedInStatus())
            viewModel.getAllItemInWishlist()
        viewModel.getDetailItem(idItem)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemDetailBinding.bind(view)
        setObserver()
        setClickEvent()
    }

    private fun setClickEvent() {
        with(binding) {
            comment.setOnClickListener {
                val idItem = requireArguments().getInt("id_item")
                val bundle = bundleOf("id_item" to idItem)
                findNavController().navigate(R.id.reviewFragment, bundle)
            }

            counterQuantity.doOnTextChanged { text, _, _, _ ->
                if (text.toString() != "") {
                    if (text.toString()[0] == '0') {
                        val newText = text.toString().drop(1)
                        binding.counterQuantity.setText(newText)
                    } else if (text.toString().toInt() > viewModel.quantity || text.toString()
                            .toInt() < 0
                    ) {
                        val newText = viewModel.quantity.toString()
                        binding.counterQuantity.setText(newText)
                        binding.counterQuantity.setSelection(newText.length)
                    }
                } else {
                    val newText = "1"
                    binding.counterQuantity.setText(newText)
                    binding.counterQuantity.setSelection(newText.length)

                }
            }

            imageIncQuantity.setOnClickListener {
                counterQuantity.clearFocus()
                val currentCounter = binding.counterQuantity.text.toString().toInt()
                if (currentCounter < viewModel.quantity) {
                    binding.counterQuantity.setText("${currentCounter + 1}")
                }
            }

            imageDesQuantity.setOnClickListener {
                counterQuantity.clearFocus()
                val currentCounter = binding.counterQuantity.text.toString().toInt()

                if (currentCounter != 1) {
                    binding.counterQuantity.setText("${currentCounter - 1}")

                }
            }

            buttonAddToCart.setOnClickListener {
                // Call API
                if (sharedPreference.getLoggedInStatus()) {
                    val idItem = requireArguments().getInt("id_item")
                    Log.d("CounterQuantity","${binding.counterQuantity.text.toString().toInt()}")
                    viewModel.addToCart(idItem, binding.counterQuantity.text.toString().toInt())
                } else {
                    Utils.alertDialogLogin(requireContext(), findNavController())
                }
            }

            backImage.setOnClickListener {
                findNavController().popBackStack()
            }

            imgFavourite.setOnClickListener {
                if (!sharedPreference.getLoggedInStatus()) {
                    Utils.alertDialogLogin(requireContext(), findNavController())
                } else {
                    // remove item from wishlist
                    if (binding.imgFavourite.background == itemInWishlist) {
                        binding.imgFavourite.background = itemNotExistingInWishlist
                    } else { // add item from wishlist
                        binding.imgFavourite.background = itemInWishlist
                    }

                    val idItem = requireArguments().getInt("id_item")
                    viewModel.updateToWishList(idItem)
                }
            }
        }
    }

    private fun setObserver() {
        viewModel.statusAddToCart.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d("ItemDetailFragment", "is loading")
                }

                is Resource.Success -> {
                    Toast.makeText(context, "${response.data?.message}", Toast.LENGTH_SHORT).show()
                }

                is Resource.Error -> {
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()

                }
            }
        }
        viewModel.statusUpdateToWishList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("ItemDetail", "is Loading")
                is Resource.Success -> {
                    Toast.makeText(context, "${response.data?.message}", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> Toast.makeText(
                    context,
                    "${response.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.itemDetailL.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d("ItemDetail", "is Loading")
                }

                is Resource.Success -> {
                    Log.d("ItemDetail", "is Success")
                    val itemDetail = response.data?.let { it[0] }

                    with(binding) {
                        if (itemDetail != null) {
                            Picasso.get().load(itemDetail.image).into(imageView)
                            tvItemName.text = itemDetail.name
                            appCompatRatingBar.rating = itemDetail.rating?.toFloat() ?: 0f
                            tvItemQuantity.text = itemDetail.quantity.toString()
                            tvItemDescription.text = itemDetail.description
                            tvItemIngredient.text = itemDetail.ingredient
                            tvItemType.text = itemDetail.name_type
                            tvItemEnergy.text = itemDetail.energy.toString()
                            tvItemPrice.text = String.format("%,d", itemDetail.price)
                            commentQuantity.text = itemDetail.countComment.toString()
                        }
                    }
                }

                is Resource.Error -> {
                    Log.d("ItemDetail", "is Error")
                }
            }
        }

        viewModel.listItemInWishlist.observe(viewLifecycleOwner) { response ->
            val listItemInWishlist = response.data
            val idItem = requireArguments().getInt("id_item")

            if (listItemInWishlist != null) {
                for (item in listItemInWishlist) {
                    if (item.id_item == idItem && sharedPreference.getLoggedInStatus()) {
                        binding.imgFavourite.background = itemInWishlist
                    }
                }
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ItemDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}