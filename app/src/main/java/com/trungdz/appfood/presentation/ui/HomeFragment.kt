package com.trungdz.appfood.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.Item
import com.trungdz.appfood.data.model.Types
import com.trungdz.appfood.data.model.UserInfo
import com.trungdz.appfood.data.util.Constants
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.data.util.Utils
import com.trungdz.appfood.data.util.cache.ISharedPreference
import com.trungdz.appfood.databinding.FragmentHomeBinding
import com.trungdz.appfood.presentation.adapter.ListItemAdapter
import com.trungdz.appfood.presentation.adapter.ListTypeAdapter
import com.trungdz.appfood.presentation.adapter.TypeWrapper
import com.trungdz.appfood.presentation.customScrollRecyclerView.RecyclerViewLoadMoreScroll
import com.trungdz.appfood.presentation.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // save state
    private var isReplaced =
        false  // prevent Livedata return value automatically when fragment onCreateView && save oldList
    private var oldSelectedType = 0

//    private var verticalOffsetAppBar=-1

    // variable
    private lateinit var binding: FragmentHomeBinding
    val viewModel: HomeFragmentViewModel by viewModels()

    private lateinit var listTypes: List<Types>
    private var listTypeAdapter: ListTypeAdapter? = null

    private lateinit var listItem: List<Item>
    private var listItemAdapter: ListItemAdapter? = null

    @Inject
    lateinit var sharedPreferences: ISharedPreference
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var layoutManager: GridLayoutManager


    // ===========
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
        // remove all observers of livedata if existing
        removeObservers()


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        if (sharedPreferences.getLoggedInStatus()) {
            viewModel.getAllItemInCart()
        }

        if (listItemAdapter != null && listTypeAdapter != null) {
            binding.recyclerViewItem.adapter = listItemAdapter
            binding.recyclerViewType.adapter = listTypeAdapter
            listTypeAdapter!!.selectedPosition = viewModel.currentTypePosition
        } else {
            viewModel.refresh()
        }
        setObserver()
        setEventListener()
        setRVLayoutManagerAndScrollListenerForListItems()

    }

    private fun setEventListener() {
        // swiperefresh
        binding.seachView.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        binding.swipeRefresh.setOnRefreshListener {
            listItemAdapter = null
            listTypeAdapter = null
            viewModel.refresh()
        }

        binding.floatingButtonScrollToUp.setOnClickListener {
            binding.recyclerViewItem.scrollToPosition(0)
        }

        binding.appbar.addOnOffsetChangedListener { _, verticalOffset ->
            binding.swipeRefresh.isEnabled =
                verticalOffset == 0 && ((binding.appbar.height - binding.appbar.bottom) == 0) // equal true if appbar is fully expanded
        }

        binding.btnTTCN.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.btnDangNhap.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }


    private fun setRVLayoutManagerAndScrollListenerForListItems() {
        layoutManager = GridLayoutManager(context, 2)
        scrollListener = object : RecyclerViewLoadMoreScroll(layoutManager) {
            override fun loadMoreItems() {
                if ((viewModel.currentPage <= viewModel.totalPage)) {
                    if ((viewModel.currentTypePosition == 0 || viewModel.currentTypePosition == null)) {
                        viewModel.getAllItem()
                    } else {
                        viewModel.getAllItemByIdType(viewModel.currentTypePosition)
                    }
                    listItemAdapter?.addLoadingView()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    binding.floatingButtonScrollToUp.visibility = View.VISIBLE
                } else {
                    binding.floatingButtonScrollToUp.visibility = View.GONE
                }
            }


        }
        binding.recyclerViewItem.addOnScrollListener(scrollListener)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (listItemAdapter?.getItemViewType(position)) {
                    Constants.VIEW_TYPE_ITEM -> 1
                    Constants.VIEW_TYPE_LOADING -> 2
                    else -> -1
                }
            }
        }
        binding.recyclerViewItem.setHasFixedSize(true)
        binding.recyclerViewItem.layoutManager = layoutManager
    }

    private fun setObserver() {
        viewModel.itemListInCart.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("HomeFragmentListItem", " is loading")
                is Resource.Success -> {

                    val countItemInCart =
                        if (response.data != null) response.data.itemList.size else 0

                    (activity as MainActivity).setItemMenuBadge(countItemInCart)
                }

                is Resource.Error -> Toast.makeText(
                    context,
                    "${response.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
        viewModel.statusMessageAddToCart.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("HomeFragmentListItem", " is loading")

                is Resource.Success -> {
                    // trigger update item badge by call method getAllItemInCart()
                    viewModel.getAllItemInCart()

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
        viewModel.isRefreshing.observe(viewLifecycleOwner) { response ->
            when (response) {
                true -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                else -> {
                    oldSelectedType = viewModel.currentTypePosition

                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
        viewModel.listItem.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d("HomeFragmentListItem", " is loading")
                }

                is Resource.Success -> {
                    Log.d("HomeFragmentListItem", " is success")
                    try {
                        listItem = response.data!!.itemList
                        // && other type
                        if (listItemAdapter == null || oldSelectedType != viewModel.currentTypePosition) {
                            oldSelectedType = viewModel.currentTypePosition
                            listItemAdapter =
                                ListItemAdapter(listItem, object : ListItemAdapter.Listener {
                                    override fun onClickButtonCart(item: Item) {
                                        if (sharedPreferences.getLoggedInStatus()) {
                                            viewModel.addToCart(item.id_item)
                                        } else {
                                            Utils.alertDialogLogin(
                                                requireContext(),
                                                findNavController()
                                            )
                                        }
                                    }

                                    override fun onClickItem(item: Item) {
                                        // Navigate
                                        if (listItemAdapter != null) {
                                            val bundle = bundleOf("id_item" to item.id_item)
                                            findNavController().navigate(
                                                R.id.action_homeFragment_to_itemDetailFragment,
                                                bundle
                                            )
                                        } else Toast.makeText(
                                            context,
                                            "Đang refresh",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                            binding.recyclerViewItem.adapter = listItemAdapter
                            listItemAdapter?.notifyDataSetChanged()
                        } else {
                            if (!isReplaced) {
                                listItemAdapter?.removeLoadingView()
                                listItemAdapter?.addData(listItem)
                                scrollListener.setLoaded()
                            } else {
                                isReplaced = false
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Lỗi hiển thị danh sách", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Error -> {
                    Log.d("HomeFragment", " error")
                }
            }
        }

        viewModel.listTypes.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Resource.Loading -> {
                    Log.d("HomeFragment", " is loading")
                }

                is Resource.Success -> {
                    Log.d("HomeFragment", " is success")
                    listTypes = ArrayList<Types>()
                    listTypes =
                        listTypes.plus(response.data as ArrayList<Types>) as ArrayList<Types>

                    val newList = listTypes.map { TypeWrapper(it) }.toMutableList().apply {
                        add(0, TypeWrapper(null))
                    }
                    if (listTypeAdapter == null) {
                        listTypeAdapter =
                            ListTypeAdapter(newList, object : ListTypeAdapter.Listener {
                                override fun onClick(types: TypeWrapper) {
                                    if (types.type?.id_type == 0 || types.type?.id_type == null) {
                                        viewModel.resetPageNumber()
                                        viewModel.getAllItem()
                                        viewModel.currentTypePosition = 0
                                    } else {
                                        viewModel.resetPageNumber()
                                        viewModel.getAllItemByIdType(types.type.id_type)
                                        viewModel.currentTypePosition =
                                            types.type.id_type
                                    }
                                }
                            })

                        binding.recyclerViewType.adapter = listTypeAdapter
                        listTypeAdapter!!.notifyDataSetChanged()
                    }
                }

                is Resource.Error -> {
                    Log.d("HomeFragment", " error")
                }
            }
        }

    }

    private fun removeObservers() {
        viewModel.listTypes.removeObservers(this)
        viewModel.listItem.removeObservers(this)
        viewModel.isRefreshing.removeObservers(this)
        viewModel.statusMessageAddToCart.removeObservers(this)
        viewModel.itemListInCart.removeObservers(this)
    }

    override fun onStart() {
        super.onStart()
        if (sharedPreferences.getLoggedInStatus()) {
            val gson = Gson()
            val userInfo = gson.fromJson(sharedPreferences.getUserInfo(), UserInfo::class.java)
            if (userInfo.image != null)
                Picasso.get().load(userInfo.image).into(binding.userImage) // use later
            binding.userName.text = "Xin chào, ${userInfo.name}"
            binding.imageView3.visibility = View.VISIBLE
            binding.btnTTCN.visibility=View.VISIBLE
            binding.btnDangNhap.visibility=View.GONE
            (activity as MainActivity).setDisplayItemMenuBadge(true)

        } else {
            binding.btnDangNhap.visibility=View.VISIBLE
            binding.imageView3.visibility = View.GONE
            binding.btnTTCN.visibility=View.GONE
            binding.userName.text = "Bạn chưa đăng nhập!"
        }

    }

    fun alertDialogLogin() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Yêu cầu đăng nhập!")
        builder.setMessage("Đăng nhập để tiếp tục thao tác")
        builder.setCancelable(false)
        builder.setPositiveButton("Đăng nhập") { dialog, _ ->
            findNavController().navigate(R.id.loginFragment)
        }
        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isReplaced = true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}