package com.trungdz.appfood.data.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.trungdz.appfood.R
import com.trungdz.appfood.data.model.ValidationResult

object Utils {
    fun validationLoginRequest(username: String, password: String): ValidationResult {
        if (username.isBlank() && password.isBlank()) return ValidationResult(
            false,
            "Username and password can't be blank"
        )
        if (username.isBlank()) return ValidationResult(false, "Username can't be blank ")
        if (password.isBlank()) return ValidationResult(false, "Password can't be blank")
        return ValidationResult(true);
    }

    fun alertDialogLogin(context: Context, navController:NavController) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Yêu cầu đăng nhập!")
        builder.setMessage("Đăng nhập để tiếp tục thao tác")
        builder.setCancelable(false)
        builder.setPositiveButton("Đăng nhập") { dialog, _ ->
            navController.navigate(R.id.loginFragment)
        }
        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}