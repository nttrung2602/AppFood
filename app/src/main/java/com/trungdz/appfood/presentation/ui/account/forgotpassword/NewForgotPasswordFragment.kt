package com.trungdz.appfood.presentation.ui.account.forgotpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.trungdz.appfood.R
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.databinding.FragmentNewForgotPasswordBinding
import com.trungdz.appfood.presentation.viewmodel.account.NewForgotPasswordFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewForgotPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NewForgotPasswordFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_new_forgot_password, container, false)
    }

    lateinit var binding: FragmentNewForgotPasswordBinding
    val viewModel:NewForgotPasswordFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewForgotPasswordBinding.bind(view)

        setEvent()
        setObserver()
    }

    private fun setEvent() {
        binding.btnTT.setOnClickListener {
            val newPassword=binding.edtNewPass.text.toString()
            val repeatPassword=binding.edtPass.text.toString()
            if(newPassword.isNotEmpty() && repeatPassword.isNotEmpty()){
                val username=requireArguments().getString("username")

                viewModel.newPassword(username!!,newPassword,newPassword)


            }else{
                if(newPassword.isEmpty()){
                    binding.edtNewPass.error="Không được bỏ trống"
                }else{
                    binding.edtPass.error="Không được bỏ trống"
                }
            }
        }

        binding.btnThoat.setOnClickListener {
            findNavController().popBackStack(R.id.loginFragment,false)
        }
    }

    private fun setObserver(){
        viewModel.messageResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{}
                is Resource.Success->{
                    Toast.makeText(context,it.data?.message,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack(R.id.loginFragment,false)
                }
                is Resource.Error->{
                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment NewForgotPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewForgotPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}