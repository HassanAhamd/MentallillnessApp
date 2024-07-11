package com.superior.mentallillness.activities

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.superior.mentallillness.R
import com.superior.mentallillness.databinding.ActivityForgetPasswordBinding
import com.superior.mentallillness.helpers.Helper

class ForgetPasswordActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityForgetPasswordBinding
    companion object{
        fun setSystemUiFlags(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Set system UI flags to make status bar icons dark
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        fun hideKeyboard(activity: Activity) {
            val view = activity.currentFocus
            if (view != null) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        var window = this.window
        window.statusBarColor = this.resources.getColor(R.color.white)
        setSystemUiFlags(this)
        initListners()
    }
    private fun initListners() {
mBinding.rlResetPassword.setOnClickListener {
    if (validateFields()){
        mBinding.svLoading.visibility = View.VISIBLE
        resetPassword(mBinding.editTextEmail.text.toString())
    }
}
    }
    fun validateFields(): Boolean {
        val field1 = mBinding.editTextEmail.text.toString().trim()

        if (field1.isEmpty()) {
            mBinding.editTextEmail.error = "Please enter email"
            mBinding.editTextEmail.requestFocus()
            return false
        }
        return true
    }

    fun resetPassword(email: String) {
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    mBinding.svLoading.visibility = View.GONE
                    Helper.showCustomAppAlert("Email send successfully.",R.drawable.ic_bell_alert,this)
                    Handler(Looper.getMainLooper()).postDelayed({
                        onBackPressed()
                    },3000)

                } else {
                    // Password reset failed
                    mBinding.svLoading.visibility = View.GONE
                    Helper.showCustomAppAlert("Error ${task.exception?.message}",R.drawable.ic_bell_alert,this)
                }
            }
    }
}