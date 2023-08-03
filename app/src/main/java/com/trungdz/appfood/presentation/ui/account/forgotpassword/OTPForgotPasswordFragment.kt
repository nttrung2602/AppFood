package com.trungdz.appfood.presentation.ui.account.forgotpassword

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.trungdz.appfood.R
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.databinding.FragmentOtpForgotPasswordBinding
import com.trungdz.appfood.presentation.viewmodel.account.OTPForgotPasswordFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OTPForgotPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class OTPForgotPasswordFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_otp_forgot_password, container, false)
    }

    lateinit var binding: FragmentOtpForgotPasswordBinding
    private val viewModel: OTPForgotPasswordFragmentViewModel by viewModels()
    var selectedEditTextPos = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOtpForgotPasswordBinding.bind(view)

        setEvent()
        setEventEditext()
        showKeyboard(binding.edt1)

    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable?) {

            if (s!!.isNotEmpty()) {
                if (selectedEditTextPos == 0) {
                    selectedEditTextPos = 1
                    showKeyboard(binding.edt2)
                } else if (selectedEditTextPos == 1) {
                    selectedEditTextPos = 2
                    showKeyboard(binding.edt3)
                } else if (selectedEditTextPos == 2) {
                    selectedEditTextPos = 3
                    showKeyboard(binding.edt4)
                } else if (selectedEditTextPos == 3) {
                    selectedEditTextPos = 4
                    showKeyboard(binding.edt5)
                } else if (selectedEditTextPos == 4) {
                    selectedEditTextPos = 5
                    showKeyboard(binding.edt6)
                }
            }
        }
    }

    private fun setEventEditext() {
        binding.edt1.addTextChangedListener(textWatcher)
        binding.edt2.addTextChangedListener(textWatcher)
        binding.edt3.addTextChangedListener(textWatcher)
        binding.edt4.addTextChangedListener(textWatcher)
        binding.edt5.addTextChangedListener(textWatcher)
        binding.edt6.addTextChangedListener(textWatcher)

        binding.edt2.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (selectedEditTextPos == 1) {
                    selectedEditTextPos = 0
                    showKeyboard(binding.edt1)
                }
            }
            false
        }
        binding.edt3.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (selectedEditTextPos == 2) {
                    selectedEditTextPos = 1
                    showKeyboard(binding.edt2)
                }
            }
            false
        }

        binding.edt4.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (selectedEditTextPos == 3) {
                    selectedEditTextPos = 2
                    showKeyboard(binding.edt3)
                }
            }
            false
        }

        binding.edt5.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (selectedEditTextPos == 4) {
                    selectedEditTextPos = 3
                    showKeyboard(binding.edt4)
                }
            }
            false
        }
        binding.edt6.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (selectedEditTextPos == 5) {
                    selectedEditTextPos = 4
                    showKeyboard(binding.edt5)
                }
            }
            false
        }
    }

    private fun showKeyboard(edt: EditText) {
        edt.requestFocus()

        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setEvent() {
        binding.btnTT.setOnClickListener {
            // viết code lấy bundle chứa dữ liệu USERNAME Ở ĐÂY
            // ...
            with(binding) {
                val text =
                    edt1.text.toString() + edt2.text.toString() + edt3.text.toString() + edt4.text.toString() + edt5.text.toString() + edt6.text.toString()
                if (text.length == 6) {
//                    val username=requireArguments().getString("username")
//                    viewModel.verifyOTP(username!!,text)
                    Log.d("VerifyID", "$text")
                } else {
                    Toast.makeText(context, "Yêu cầu nhập đủ mã xác thực!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.btnThoat.setOnClickListener {
            findNavController().popBackStack(R.id.loginFragment, false)
        }
    }

    private fun setObserver() {
        viewModel.messageResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_OTPForgotPasswordFragment2_to_newForgotPasswordFragment2)
                }
                is Resource.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment OTPForgotPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OTPForgotPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}