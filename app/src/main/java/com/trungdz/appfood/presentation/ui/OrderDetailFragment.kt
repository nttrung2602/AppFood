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
import com.google.gson.Gson
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.UserInfo
import com.trungdz.appfood.data.model.ItemInOrder
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.data.util.cache.ISharedPreference
import com.trungdz.appfood.databinding.FragmentOrderDetailBinding
import com.trungdz.appfood.presentation.adapter.ItemListInOrderDetailAdapter
import com.trungdz.appfood.presentation.customReviewDialog.ReviewDialog
import com.trungdz.appfood.presentation.viewmodel.OrderDetailFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class OrderDetailFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_order_detail, container, false)
    }

    // variable
    lateinit var binding: FragmentOrderDetailBinding
    private val viewModel: OrderDetailFragmentViewModel by viewModels()
    private lateinit var itemListInOrderDetailAdapter: ItemListInOrderDetailAdapter

    @Inject
    lateinit var sharedPreference: ISharedPreference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderDetailBinding.bind(view)

        val idOrder = requireArguments().getInt("id_order")
        viewModel.getAllItemInOrder(idOrder)
        setObservers()
        setEventListener()
    }

    private fun setObservers() {
        viewModel.orderDetail.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("OrderDetailFragment", "AAAA")
                is Resource.Success -> {
                    response.data?.let {
                        with(binding) {
                            orderTotal.text =
                                "Tổng tiền: " + String.format("%,2d", it.total) + " VND"
                            val idOrder = requireArguments().getInt("id_order")

                            orderCode.text = "#" + idOrder.toString()
                            when (it.status) {
                                0 -> {
                                    orderStatus.text = "Chờ xác nhận"
                                    btnCancel.visibility=View.VISIBLE
                                }
                                1 -> orderStatus.text = "Đã xác nhận"
                                2 -> orderStatus.text = "Hủy"
                            }
                            orderTime.text = it.datetime
                            // address
                            val userInfo = Gson().fromJson(
                                sharedPreference.getUserInfo(),
                                UserInfo::class.java
                            )
                            orderAddress.text = userInfo.address

                        }

                        val itemList = it.itemList

                        itemListInOrderDetailAdapter = ItemListInOrderDetailAdapter(itemList,
                            object : ItemListInOrderDetailAdapter.Listener {
                                override fun onClickComment(item: ItemInOrder, position: Int) {
                                    //
                                    val fm = fragmentManager
                                    if (fm != null) {
                                        ReviewDialog.Static.newInstance(
                                            item, object : ReviewDialog.Listener {
                                                override fun onClickConfirm(isReviewed: Boolean) {
                                                    if (isReviewed) {
                                                        itemListInOrderDetailAdapter.setReviewed(
                                                            position
                                                        )
                                                    }
                                                }
                                            }
                                        ).show(fm, "review_dialog")
                                    }
                                }
                            })

                        val linearLayoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.recyclerViewItemOrder.layoutManager = linearLayoutManager
                        binding.recyclerViewItemOrder.adapter = itemListInOrderDetailAdapter
                        when (it.status) {
                            0, 2 -> itemListInOrderDetailAdapter.setOrderConfirmed()
                        }
                        itemListInOrderDetailAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> Toast.makeText(
                    context,
                    "${response.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.messageResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{}
                is Resource.Success->{
                    Toast.makeText(context,it.data?.message,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is Resource.Error->{
                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setEventListener() {
        binding.backImage.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnCancel.setOnClickListener {
            val idOrder=requireArguments().getInt("id_order")
            viewModel.cancelOrder(idOrder)
        }
    }

    private fun removeObservers() {
        viewModel.orderDetail.removeObservers(this)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}