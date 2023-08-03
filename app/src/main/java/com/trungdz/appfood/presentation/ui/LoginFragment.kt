package com.trungdz.appfood.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.modelrequest.LoginRequest
import com.trungdz.appfood.data.util.Utils
import com.trungdz.appfood.databinding.FragmentLoginBinding
import com.trungdz.appfood.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {
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

    // variable
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentLoginBinding.bind(view)
        setEventListener()
    }
    private fun setEventListener() {
        binding.btnLogin.setOnClickListener {

            val username = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()

            val validationResult = Utils.validationLoginRequest(username, password)

            if (validationResult.successful) {
                binding.loginProgress.loadingProgress.visibility = View.VISIBLE

                val loginRequest = LoginRequest(username, password)

                loginViewModel.login(loginRequest)

                loginViewModel.loggedIn.observe(viewLifecycleOwner) { status ->
                    if (status == true) {
                        binding.loginProgress.loadingProgress.visibility = View.GONE
                        findNavController().popBackStack()
                    } else {
                        binding.loginProgress.loadingProgress.visibility = View.GONE
                        Snackbar.make(
                            binding.btnLogin,
                            "${loginViewModel.error.value}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Snackbar.make(
                    binding.activityLogin,
                    "${validationResult.error}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnQMK.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}