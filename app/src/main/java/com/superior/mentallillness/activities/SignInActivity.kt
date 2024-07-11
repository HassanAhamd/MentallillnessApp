package com.superior.mentallillness.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.google.firebase.auth.FirebaseAuth
import com.superior.mentallillness.R
import com.superior.mentallillness.activities.ForgetPasswordActivity.Companion.hideKeyboard
import com.superior.mentallillness.databinding.ActivitySignInBinding
import com.superior.mentallillness.helpers.Helper


class SignInActivity : AppCompatActivity() {
    lateinit var mBinding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        var window = this.window
        window.statusBarColor = this.resources.getColor(R.color.white)
        ForgetPasswordActivity.setSystemUiFlags(this)
        initListners()
    }
    private fun initListners() {
        mBinding.rlLogin.setOnClickListener {
            if (validateFields()) {
                hideKeyboard(this)
                mBinding.svLoading.visibility = View.VISIBLE
                login(
                    mBinding.editTextEmail.text?.trim().toString(),
                    mBinding.editTextPassword.text.trim().toString()
                )
            }
        }
        mBinding.tvNewUser.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        mBinding.movePlus.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        mBinding.moveView.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        mBinding.tvForgetPass.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }
    }

    fun validateFields(): Boolean {
        val field1 = mBinding.editTextEmail.text.toString().trim()
        val field2 = mBinding.editTextPassword.text.toString().trim()

        if (field1.isEmpty()) {
            mBinding.editTextEmail.error = "Please enter email"
            mBinding.editTextEmail.requestFocus()
            return false
        }

        if (field2.isEmpty()) {
            mBinding.editTextPassword.error = "Please enter password"
            mBinding.editTextPassword.requestFocus()
            return false
        }

        return true
    }

    fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login success, update UI or perform other actions
                    mBinding.svLoading.visibility = View.GONE
                    Helper.showCustomAppAlert("Login Successful.",R.drawable.ic_bell_alert,this)
//                    Toast.makeText(
//                        MentalIllnessApp.getContext(),
//                        "Login Successful.",
//                        Toast.LENGTH_SHORT
//                    ).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },3000)

                } else {
                    // If login fails, display a message to the user.
                    // You can also get the exception using task.exception
                    mBinding.svLoading.visibility = View.GONE
                    Helper.showCustomAppAlert("Login failed. ${task.exception?.message}",R.drawable.ic_bell_alert,this)
//                    Toast.makeText(
//                        MentalIllnessApp.getContext(),
//                        "Login failed. ${task.exception?.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
    }
}