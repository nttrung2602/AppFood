package com.trungdz.appfood.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.trungdz.appfood.R
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.databinding.FragmentCheckoutBinding
import com.trungdz.appfood.presentation.adapter.ItemListInCartCheckOutAdapter
import com.trungdz.appfood.presentation.viewmodel.CheckOutFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class CheckoutFragment : Fragment() {
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
        removeObservers()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    // Variable
    private val viewModel: CheckOutFragmentViewModel by viewModels()
    lateinit var binding: FragmentCheckoutBinding
    lateinit var itemListInCartCheckOutAdapter: ItemListInCartCheckOutAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCheckoutBinding.bind(view)

        // bundle
        val finalTotalPrice = requireArguments().getInt("finalTotalPrice")
        binding.totalPrice.text = String.format("%,2d", finalTotalPrice)

        if (oldSelectedPayment != -1) {
            checkSelectedPayment(oldSelectedPayment)
        }

        viewModel.getAllItemInCart()
        setObserver()
        setEventListener()
    }

    private fun setObserver() {
        viewModel.statusMessageCheckout.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("CheckoutFragment", "is loading")
                is Resource.Success -> {
                    Toast.makeText(context, "${response.data?.message}", Toast.LENGTH_SHORT)
                        .show()

                    (activity as MainActivity).setItemMenuBadge(0)
                    findNavController().popBackStack(R.id.cartFragment, true)
                    findNavController().navigate(R.id.cartFragment)
                }
                is Resource.Error -> Toast.makeText(
                    context,
                    "${response.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        viewModel.itemListInCart.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("CheckoutFragment", "is loading")
                is Resource.Success -> {
                    response.data?.let {
                        val itemList = response.data.itemList
                        if (itemList.isNotEmpty()) {
                            itemListInCartCheckOutAdapter = ItemListInCartCheckOutAdapter(itemList)

                            val linearLayoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            binding.recyclerViewItemList.layoutManager = linearLayoutManager
                            binding.recyclerViewItemList.adapter = itemListInCartCheckOutAdapter
                            itemListInCartCheckOutAdapter.notifyDataSetChanged()
                        }
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

    private var oldSelectedPayment = -1

    private fun setEventListener() {
        binding.backImage.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.payment1.setOnClickListener {

            Log.d("CheckoutFragmentClick1", "AA")
            uncheckSelectedPayment(oldSelectedPayment)
            oldSelectedPayment = 1
            checkSelectedPayment(oldSelectedPayment)

        }

        binding.payment2.setOnClickListener {

            uncheckSelectedPayment(oldSelectedPayment)
            oldSelectedPayment = 2
            checkSelectedPayment(oldSelectedPayment)

        }

        binding.payment3.setOnClickListener {

            uncheckSelectedPayment(oldSelectedPayment)
            oldSelectedPayment = 3
            checkSelectedPayment(oldSelectedPayment)

        }

        binding.btnConfirmCheckout.setOnClickListener {
            if (oldSelectedPayment != -1) {
                val idPayment = oldSelectedPayment
                val description = binding.edtNote.text.toString()
                viewModel.checkout(idPayment, description)
            } else {
                Toast.makeText(context, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun checkSelectedPayment(oldSelectedPayment: Int) {
        when (oldSelectedPayment) {
            1 -> {
                binding.payment1.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.baseline_check_24,
                    0
                )
            }
            2 -> {
                binding.payment2.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.baseline_check_24,
                    0
                )
            }
            3 -> {
                binding.payment3.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.baseline_check_24,
                    0
                )
            }
        }
    }

    private fun uncheckSelectedPayment(oldSelectedPayment: Int) {
        when (oldSelectedPayment) {
            1 -> {
                binding.payment1.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_wallet_24,
                    0,
                    0,
                    0
                )
            }
            2 -> {
                binding.payment2.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_wallet_24,
                    0,
                    0,
                    0
                )
            }
            3 -> {
                binding.payment3.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.baseline_wallet_24,
                    0,
                    0,
                    0
                )
            }
        }
    }

    private fun removeObservers() {
        viewModel.itemListInCart.removeObservers(this)
        viewModel.statusMessageCheckout.removeObservers(this)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CheckoutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CheckoutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}