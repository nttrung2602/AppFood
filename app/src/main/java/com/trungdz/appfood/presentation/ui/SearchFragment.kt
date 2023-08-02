package com.trungdz.appfood.presentation.ui

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.Item
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.databinding.FragmentSearchBinding
import com.trungdz.appfood.presentation.adapter.ItemListInSearchAdapter
import com.trungdz.appfood.presentation.customScrollRecyclerView.RecyclerViewLoadMoreScroll
import com.trungdz.appfood.presentation.viewmodel.SearchFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    // variable
    lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchFragmentViewModel by viewModels()
    lateinit var itemListInSearchAdapter: ItemListInSearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        setObservers()
        setEvent()
        setRVLayoutManagerAndScrollListenerForListItems()
        binding.edittextSearch.requestFocus()
    }

    private fun setObservers() {
        viewModel.itemList.observe(viewLifecycleOwner) { response ->
            response.peekContent().let {
                when (it) {
                    is Resource.Loading -> Log.d("SearchFragment", "AAAA")
                    is Resource.Success -> {
                        it.data.let { itemList ->
                            itemList?.let {
                                val itemList = itemList.itemList
                                if (!response.hasBeenHandled) {
                                    response.setHandled()
                                    itemListInSearchAdapter = ItemListInSearchAdapter(itemList,
                                        object : ItemListInSearchAdapter.Listener {
                                            override fun onClickItem(item: Item, position: Int) {
                                                // Do s.t
                                                val bundle = bundleOf("id_item" to item.id_item)
                                                findNavController().navigate(
                                                    R.id.itemDetailFragment,
                                                    bundle
                                                )
                                            }
                                        })
                                    binding.recyclerViewItem.adapter = itemListInSearchAdapter
                                } else {
                                    Log.d("SearchFragmentDAyy", "1")
                                    itemListInSearchAdapter.itemList = itemList
                                    binding.recyclerViewItem.adapter = itemListInSearchAdapter
                                }
                            }
                        }
                    }
                    is Resource.Error -> Toast.makeText(
                        context,
                        "${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    private fun setRVLayoutManagerAndScrollListenerForListItems() {
        val layoutManager =
            LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        binding.recyclerViewItem.layoutManager = layoutManager

        val scrollListener = object : RecyclerViewLoadMoreScroll(layoutManager) {
            override fun loadMoreItems() {
                if (viewModel.nextPage()) {
                    val name = binding.edittextSearch.text.toString()
                    viewModel.loadMoreItem(name)
                }
            }
        }
        binding.recyclerViewItem.addOnScrollListener(scrollListener)
    }

    // prevent automatically run doOnTextChanged() when onCreate again
    private var previousTextLength = -1

    private fun setEvent() {
        binding.backImage.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.edittextSearch.doOnTextChanged { text, _, _, _ ->
            text?.length?.let {
                if (previousTextLength != it) {
                    previousTextLength = it
                    if (it == 0) {
                        viewModel.resetPage()
                        (itemListInSearchAdapter.itemList as ArrayList).clear()
                        itemListInSearchAdapter.notifyDataSetChanged()
                    } else {
                        viewModel.resetPage()
                        viewModel.getAllItemByName(text.toString())
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
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}