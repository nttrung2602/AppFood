package com.trungdz.appfood.presentation.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.chart.OrderOfChart
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.databinding.FragmentChartBinding
import com.trungdz.appfood.presentation.viewmodel.ChartFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ChartFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    // variable
    lateinit var binding: FragmentChartBinding
    private val viewModel: ChartFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChartBinding.bind(view)
        binding.barChart.requestFocus()
        binding.barChart.setNoDataText("Description that you want");

        viewModel.getChart()
        setEventListener()
        setObserver()
    }

    private fun setObserver() {
        viewModel.orderList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> Log.d("CharFragment", "is loading")
                is Resource.Success -> {
                    response.data?.let {
                        val orderList = it.orderList

                        barChart(orderList)
                        pieChart(orderList)
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

    private val labelsOfPieChart =
        mapOf(
            1 to "Burger",
            2 to "Đồ uống",
            3 to "Đồ ngọt",
            4 to "Pasta",
            5 to "Pizza",
            6 to "Khác"
        )

    private fun pieChart(myDataset: List<OrderOfChart>) {
        var totalQuantityItem = 0
        myDataset.forEach { it1 ->
            it1.Order_details.forEach { it2 ->
                totalQuantityItem += it2.quantity
            }
        }
        // PieEntry
        val pieEntries = ArrayList<PieEntry>()
        labelsOfPieChart.forEach { it1 ->
            var sumQuantityEachType = 0
            myDataset.forEach { it2 ->
                it2.Order_details.forEach { it3 ->
                    if (it1.key == it3.Item.id_type) {
                        sumQuantityEachType += it3.quantity
                    }
                }
            }
            pieEntries.add(
                PieEntry(
                    (sumQuantityEachType.toFloat() / totalQuantityItem.toFloat()),
                    it1.value
                )
            )
        }

        val colorPieChart = arrayListOf(
            Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 50, 111),
            Color.rgb(106, 167, 134), Color.rgb(53, 194, 209), Color.rgb(255, 102, 0)
        )

        val dataset = PieDataSet(pieEntries, null)
        dataset.colors = colorPieChart
        val data = PieData(dataset)
        data.apply {
            setValueFormatter(PercentFormatter(binding.pieChart))
            setValueTextSize(13f)
            setValueTextColor(Color.WHITE)
        }

        binding.pieChart.apply {
            setUsePercentValues(true)
            setData(data)
            invalidate()
            description.isEnabled = false
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            isDrawHoleEnabled = false
            setDrawEntryLabels(false)
            setTouchEnabled(false)
        }

    }

    val convertDayOfWeekAsNumberToText = mapOf<Int, String>(
        1 to "CN",
        2 to "T2",
        3 to "T3",
        4 to "T4",
        5 to "T5",
        6 to "T6",
        7 to "T7"
    )

    val xEntriesBarAndLabelDayOfWeekAsNumber =
        mapOf(7 to 1, 1 to 2, 2 to 3, 3 to 4, 4 to 5, 5 to 6, 6 to 7)

    //    private lateinit var barEntries: List<BarEntry>
    // display label and data on barChart
    private fun barChart(myDataset: List<OrderOfChart>) {
        val barEntries = ArrayList<BarEntry>()
        var dayOfWeek = -1
        var sumCost = 0

        xEntriesBarAndLabelDayOfWeekAsNumber.forEach { it ->
            sumCost = 0
            myDataset.forEach { it2 ->
                dayOfWeek = getDayOfWeek(it2.datetime)
                if (it.value == dayOfWeek) {
                    sumCost += it2.total
                }
            }

            (barEntries as ArrayList).add(BarEntry(it.key.toFloat(), sumCost.toFloat()))
        }

        val barDataset = BarDataSet(barEntries, "Thống kê chi tiêu")
        barDataset.apply {
            valueTextColor = Color.BLACK
            valueTextSize = 13f
            barBorderWidth = 1f
        }

        val barData = BarData(barDataset)

        binding.barChart.apply {
            axisRight.isEnabled = false  //not display value right YaxisRight
            axisRight.axisMinimum = 0f // remove extra space in Top of YAxis label with xAxis
            axisLeft.axisMinimum = 0f // remove extra space in Bottom of YAxis label with xAxis
        }

        val valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String? {
                return convertDayOfWeekAsNumberToText[xEntriesBarAndLabelDayOfWeekAsNumber[value.toInt()]]
            }
        }

        binding.barChart.xAxis.apply {
            granularity = 1f // duplicate label
            this.valueFormatter = valueFormatter // assign label into xAxis
            position = XAxis.XAxisPosition.BOTTOM // assign label below xAxis
            setDrawGridLines(false) // remove grid line
            setDrawAxisLine(false) // remove line xAxis
        }
        binding.barChart.apply {
            data = barData // pour data to draw chart
            invalidate() // prevent first click before draw chart
            description.isEnabled = false // hide description corner right_bottom barChart
            legend.isEnabled = false // hide legend(note) of bar entry in barChart
//           setTouchEnabled(false) // prevent click to chart or Zoom in/out
        }
    }

    private fun getDayOfWeek(date: String): Int {
        val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        // convert String to date
        val dateTime: Date = df1.parse(date)
        val cal = Calendar.getInstance()
        cal.time = dateTime
        return cal.get(Calendar.DAY_OF_WEEK)
    }

    private fun setEventListener() {
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
         * @return A new instance of fragment ChartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}