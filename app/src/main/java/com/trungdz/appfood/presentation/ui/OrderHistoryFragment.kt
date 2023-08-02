package com.trungdz.appfood.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.OrderItem
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.databinding.FragmentOrderHistoryBinding
import com.trungdz.appfood.presentation.adapter.OrdersListAdapter
import com.trungdz.appfood.presentation.viewmodel.OrderHistoryFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class OrderHistoryFragment : Fragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_history, container, false)
    }

    // variable
    lateinit var binding: FragmentOrderHistoryBinding
    private val viewModel: OrderHistoryFragmentViewModel by viewModels()
    lateinit var ordersListAdapter: OrdersListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderHistoryBinding.bind(view)


        viewModel.getAllOrder()
        setObservers()
        setEventListener()
    }

    private fun setObservers() {
        viewModel.totalCostAllOrder.observe(viewLifecycleOwner) { response ->
            binding.orderTotalCost.text = String.format("%,2d", response) + " VND"
        }

        viewModel.totalNumOrder.observe(viewLifecycleOwner) { response ->
            binding.orderQuantity.text = String.format("%,2d", response)
        }

        viewModel.orderList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("OrderHistoryFragment", "AAA")
                is Resource.Success -> {
                    viewModel.totalNumOrder()
                    viewModel.totalCostAllOrder()

                    val orderList = response.data as ArrayList<OrderItem>

                    ordersListAdapter =
                        OrdersListAdapter(orderList, object : OrdersListAdapter.Listener {
                            override fun onClick(item: OrderItem, position: Int) {
                                val bundle = bundleOf("id_order" to item.id_order)
                                findNavController().navigate(R.id.orderDetailFragment, bundle)
                            }
                        })

                    val linearLayoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.recyclerViewOrderList.adapter = ordersListAdapter
                    binding.recyclerViewOrderList.layoutManager = linearLayoutManager
                    ordersListAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> Toast.makeText(
                    context,
                    "${response.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun setEventListener() {
        binding.btnRefresh.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.orderHistoryFragment)
        }
        binding.btnChart.setOnClickListener {
            findNavController().navigate(R.id.chartFragment)
        }
    }

    private fun removeObservers() {
        viewModel.orderList.removeObservers(this)
        viewModel.totalNumOrder.removeObservers(this)
        viewModel.totalCostAllOrder.removeObservers(this)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderHistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}