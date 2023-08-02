package com.trungdz.appfood.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.modelresponse.ItemInCart
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.databinding.EmptyCartLayoutBinding
import com.trungdz.appfood.databinding.FragmentCartBinding
import com.trungdz.appfood.presentation.adapter.ItemListInCartAdapter
import com.trungdz.appfood.presentation.viewmodel.CartFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class CartFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // removeObserver
        removeObserver()
        isEmptyCart = (activity as MainActivity).getItemMenuBadge() == 0
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    // Start - variable global
    lateinit var binding: FragmentCartBinding
    lateinit var itemListInCartAdapter: ItemListInCartAdapter
    private val viewModel: CartFragmentViewModel by viewModels()
    private var selectedItemPosition = -1
    var isEmptyCart = false

    // ------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartBinding.bind(view)
        if (isEmptyCart) {
            binding.emptyCartLayout.visibility = View.VISIBLE
        } else {
            viewModel.getAllItemInCart()
            setObserver()
            setEventListener()
        }
    }

    private fun setObserver() {
        viewModel.statusMessageUpdateItemInCart.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("CartFragment", "is loading")
                is Resource.Success -> {
                    Toast.makeText(
                        context,
                        "${response.data?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Error -> Toast.makeText(
                    context,
                    "${response.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.statusMessageDeleteOneItemInCart.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("CartFragment", "is loading")
                is Resource.Success -> {
                    if (selectedItemPosition != -1) {
                        itemListInCartAdapter.deleteItem(selectedItemPosition)

                        Toast.makeText(context, "${response.data?.message}", Toast.LENGTH_SHORT)
                            .show()
                        (activity as MainActivity).setItemMenuBadge(
                            itemListInCartAdapter.itemList.size
                        )
                        viewModel.finalTotalPrice()

                        selectedItemPosition = -1

                        // check emptyCart
                        if ((activity as MainActivity).getItemMenuBadge() == 0) {
                            findNavController().popBackStack(R.id.cartFragment, true)
                            findNavController().navigate(R.id.cartFragment)
                        }
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }
        viewModel.statusMessageDecreaseItemInCart.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("CartFragment", "is loading")
                is Resource.Success -> {
                    if (selectedItemPosition != -1) {
                        itemListInCartAdapter.decreaseQuantity(selectedItemPosition)

                        Toast.makeText(context, "${response.data?.message}", Toast.LENGTH_SHORT)
                            .show()

                        selectedItemPosition = -1
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.statusMessageIncreaseItemInCart.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("CartFragment", "is loading")
                is Resource.Success -> {
                    if (selectedItemPosition != -1) {
                        itemListInCartAdapter.increaseQuantity(selectedItemPosition)

                        Toast.makeText(context, "${response.data?.message}", Toast.LENGTH_SHORT)
                            .show()
                        // reset position
                        selectedItemPosition = -1
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.itemListInCart.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("CartFragment", "is loading")
                is Resource.Success -> {
                    response.data?.let {
                        val itemList = it.itemList

                        viewModel.finalTotalPrice()

                        itemListInCartAdapter = ItemListInCartAdapter(
                            itemList,
                            object : ItemListInCartAdapter.Listener {
                                override fun onClickDesQuantity(
                                    item: ItemInCart,
                                    position: Int,
                                ) {
                                    if (itemListInCartAdapter.checkValidCounterQuantityOfITem(
                                            position,
                                            ItemListInCartAdapter.Action.DECREASE_QUANTITY_ITEM
                                        )
                                    ) {
                                        selectedItemPosition = position
                                        itemListInCartAdapter.setUpdating(position)
                                        viewModel.decreaseNumItemInCart(item.id_item)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Số lượng đã đạt tối thiểu!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onClickIncQuantity(
                                    item: ItemInCart,
                                    position: Int,
                                ) {
                                    if (itemListInCartAdapter.checkValidCounterQuantityOfITem(
                                            position,
                                            ItemListInCartAdapter.Action.INCREASE_QUANTITY_ITEM
                                        )
                                    ) {
                                        selectedItemPosition = position
                                        itemListInCartAdapter.setUpdating(position)
                                        viewModel.increaseNumItemInCart(item.id_item)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Số lượng đã đạt tối đa!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onClickDelete(item: ItemInCart, position: Int) {
                                    selectedItemPosition = position
                                    viewModel.deleteOneItemInCart(item.id_item)
//                                    viewModel.finalTotalPrice()

                                }

                                override fun onClickItem(item: ItemInCart) {
                                    val bundle = bundleOf("id_item" to item.id_item)
                                    findNavController().navigate(
                                        R.id.itemDetailFragment,
                                        bundle
                                    )
                                }
                            })

                        val linearLayoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.recyclerViewItem.adapter = itemListInCartAdapter
                        binding.recyclerViewItem.layoutManager = linearLayoutManager
                        itemListInCartAdapter.notifyDataSetChanged()
                    }

                }
                is Resource.Error -> Log.d("CartFragment", "is error")
            }
        }
        viewModel.totalLiveData.observe(viewLifecycleOwner) { data ->
            binding.totalPrice.text = String.format("%,2d", data)
        }
        viewModel.priceDiscountLiveData.observe(viewLifecycleOwner) { data ->
            binding.saleOffPrice.text = String.format("%,2d", data)
        }
        viewModel.finalTotalPriceLiveData.observe(viewLifecycleOwner) { data ->
            binding.finalTotalPrice.text = String.format("%,2d", data)
        }
    }

    private fun setEventListener() {
        binding.btnCheckout.setOnClickListener {
            if (itemListInCartAdapter.itemList.isNotEmpty()) {
                val finalTotalPrice = viewModel.finalTotalPriceLiveData.value
                val bundle = bundleOf("finalTotalPrice" to finalTotalPrice)
                findNavController().navigate(R.id.checkoutFragment, bundle)
            }

        }
    }

    private fun removeObserver() {
        viewModel.statusMessageDeleteOneItemInCart.removeObservers(this)
        viewModel.statusMessageIncreaseItemInCart.removeObservers(this)
        viewModel.itemListInCart.removeObservers(this)
        viewModel.statusMessageDeleteOneItemInCart.removeObservers(this)
        viewModel.finalTotalPriceLiveData.removeObservers(this)
        viewModel.totalLiveData.removeObservers(this)
        viewModel.priceDiscountLiveData.removeObservers(this)
        viewModel.statusMessageUpdateItemInCart.removeObservers(this)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}