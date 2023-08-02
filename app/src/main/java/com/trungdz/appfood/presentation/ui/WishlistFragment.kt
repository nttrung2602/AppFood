package com.trungdz.appfood.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.ItemWishlist
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.databinding.FragmentWishlistBinding
import com.trungdz.appfood.presentation.adapter.WishlistAdapter
import com.trungdz.appfood.presentation.viewmodel.WishlistFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WishlistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class WishlistFragment : Fragment() {
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

    lateinit var binding: FragmentWishlistBinding
    private val viewModel: WishlistFragmentViewModel by viewModels()

    private lateinit var wishlistAdapter: WishlistAdapter
    private var selectedPositionItem: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWishlistBinding.bind(view)

        viewModel.getAllItemInWishList()
        setObserver()
    }

    private fun setObserver() {
        viewModel.wishlistList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("WishlistFragment", "is loading")
                is Resource.Success -> {
                    Log.d("WishlistFragment", "is success")
                    var itemWishlistList = response.data as ArrayList<ItemWishlist>
                    wishlistAdapter =
                        WishlistAdapter(itemWishlistList, object : WishlistAdapter.Listener {
                            override fun onClickFavorite(item: ItemWishlist, pos: Int) {
                                selectedPositionItem = pos
                                viewModel.updateToItemInWishlist(item.id_item)
                            }

                            override fun onClickItem(item: ItemWishlist) {
                                // navigate Detail ItemInOrder
                                val bundle = bundleOf("id_item" to item.id_item)

                                findNavController().navigate(R.id.itemDetailFragment, bundle)
                            }
                        })

                    // set recyclerView
                    val linearLayoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.recyclerView.adapter = wishlistAdapter
                    binding.recyclerView.layoutManager = linearLayoutManager
                    wishlistAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> Log.d("WishlistFragment", "is loading")
            }
        }

        viewModel.statusUpdateToWishlist.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("WishlistFragment", "is loading")
                is Resource.Success -> {
                    if (selectedPositionItem != -1) {
                        wishlistAdapter.removeItem(selectedPositionItem)
                        Toast.makeText(context, "${response.data?.message}", Toast.LENGTH_SHORT).show()
                        selectedPositionItem = -1
                    }
                }
                is Resource.Error -> Toast.makeText(
                    context,
                    "${response.message}",
                    Toast.LENGTH_SHORT
                ).show()
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
         * @return A new instance of fragment WishlistFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WishlistFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}