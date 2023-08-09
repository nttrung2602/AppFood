package com.trungdz.appfood.presentation.ui.account.profile

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.UserInfo
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.data.util.cache.ISharedPreference
import com.trungdz.appfood.databinding.FragmentProfileBinding
import com.trungdz.appfood.presentation.ui.MainActivity
import com.trungdz.appfood.presentation.viewmodel.account.profile.ProfileFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var iSharedPreference: ISharedPreference
    val viewModel: ProfileFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        setView()
        setEvent()
        setObserver()
    }

    private fun setView() {
        val userInfo = Gson().fromJson(iSharedPreference.getUserInfo(), UserInfo::class.java)
        binding.txtName.setText(userInfo.name)
        binding.txtPhone.setText(userInfo.phone)
        binding.txtEmail.text = userInfo.email
        binding.txtAddress.setText(userInfo.address)

    }

    private fun setEvent() {
        binding.editPhone.setOnClickListener {
            binding.txtPhone.isEnabled = true
            binding.txtPhone.requestFocus()
            binding.txtPhone.setSelection(binding.txtPhone.text.toString().length)
            binding.btnConfirmEditProfile.visibility = View.VISIBLE
        }

        binding.editName.setOnClickListener {
            binding.txtName.isEnabled = true
            binding.txtName.requestFocus()
            binding.txtName.setSelection(binding.txtName.text.toString().length)
            binding.btnConfirmEditProfile.visibility = View.VISIBLE
        }

        binding.editAddress.setOnClickListener {
            binding.txtAddress.isEnabled = true
            binding.txtAddress.requestFocus()
            binding.txtAddress.setSelection(binding.txtAddress.text.toString().length)
            binding.btnConfirmEditProfile.visibility = View.VISIBLE
        }

        binding.btnConfirmEditProfile.setOnClickListener {
            val alertDialog = AlertDialog.Builder(activity).create()
            alertDialog.setTitle("Thông báo")
            alertDialog.setMessage("Bạn muốn lưu chỉnh sửa?")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Đồng ý") { dialog, which ->
                // handle by viewmodel
                val phone = binding.txtPhone.text.toString()
                val name = binding.txtName.text.toString()
                val address = binding.txtAddress.text.toString()
                viewModel.updateProfile(name, phone, address)
                // ...
                dialog.dismiss()

            }
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Không") { dialog, which ->
                dialog.dismiss()
                findNavController().popBackStack()
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            }
            alertDialog.show()
            Log.d(
                "Trung-Profile",
                "${binding.txtPhone.text}, ${binding.txtEmail.text}, ${binding.txtAddress.text}"
            )
        }

        binding.backImage.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }

        binding.logOut.setOnClickListener {
            iSharedPreference.saveLoggedInStatus(false)
            (activity as MainActivity).setDisplayItemMenuBadge(false)

            findNavController().popBackStack()

//            activity?.recreate()
        }
    }

    private fun setObserver() {
        viewModel.messageResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    Toast.makeText(context, it.data?.message, Toast.LENGTH_SHORT).show()

                    val userInfo=Gson().fromJson(iSharedPreference.getUserInfo(),UserInfo::class.java)
                    userInfo.address=binding.txtAddress.text.toString()
                    userInfo.phone=binding.txtPhone.text.toString()
                    userInfo.name=binding.txtName.text.toString()

                    val userInfoJson=Gson().toJson(userInfo)
                    iSharedPreference.saveUserInfo(userInfoJson)

                    findNavController().popBackStack()
                    findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}