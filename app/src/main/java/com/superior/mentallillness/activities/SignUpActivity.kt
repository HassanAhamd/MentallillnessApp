package com.superior.mentallillness.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.superior.mentallillness.R
import com.superior.mentallillness.databinding.ActivitySignUpBinding
import com.superior.mentallillness.helpers.Helper
import com.superior.mentallillness.model.User

class SignUpActivity : AppCompatActivity() {
    lateinit var mBinding: ActivitySignUpBinding
    private lateinit var database: FirebaseDatabase
    var GENDER = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        var window = this.window
        window.statusBarColor = this.resources.getColor(R.color.primaryColor)
        database = FirebaseDatabase.getInstance()
        initListners()
    }

    private fun initListners() {
        mBinding.rlRegister.setOnClickListener {
            if (validateFields()) {
                mBinding.svLoading.visibility = View.VISIBLE
                signUp(
                    mBinding.editTextEmail.text.toString(),
                    mBinding.editTextPassword.text.toString()
                )
            }
        }
        mBinding.tvAlreadyAccount.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finishAffinity()
        }
        mBinding.ivMoveBack.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finishAffinity()
        }
        mBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioMale -> {
                    // Handle Male selection
                    GENDER = "Male"
                }

                R.id.radioFemale -> {
                    // Handle Female selection
                    GENDER = "Female"
                }

                R.id.radioOther -> {
                    // Handle Other selection
                    GENDER = "Other"
                }
            }
        }
    }
    fun validateFields(): Boolean {
        val field1 = mBinding.editTextName.text.toString().trim()
        val field2 = mBinding.editTextEmail.text.toString().trim()
        val field3 = mBinding.editTextMobile.text.toString().trim()
        val field4 = mBinding.editTextPassword.text.toString().trim()

        if (field1.isEmpty()) {
            mBinding.editTextName.error = "Please enter name"
            return false
        }
        if (field2.isEmpty()) {
            mBinding.editTextEmail.error = "Please enter email"
            return false
        }

        if (field3.isEmpty()) {
            mBinding.editTextMobile.error = "Please enter phone no"
            return false
        }
        if (field4.isEmpty()) {
            mBinding.editTextPassword.error = "Please enter password"
            return false
        }
        if (GENDER == "") {
            Helper.showCustomAppAlert( "Please select your gender",R.drawable.ic_bell_alert,this)
//            Toast.makeText(
//                MentalIllnessApp.getContext(),
//                "Please select your gender",
//                Toast.LENGTH_SHORT
//            ).show()
            return false
        }

        return true
    }
    fun signUp(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success, update UI or perform other actions


//                    mBinding.svLoading.visibility = View.GONE
//                    Toast.makeText(
//                        MentalIllnessApp.getContext(), "User signed up successfully.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    startActivity(Intent(this,SignInActivity::class.java))
//                    finishAffinity()

                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    val userId = firebaseUser?.uid ?: ""
                    saveUserInfo(userId, mBinding.editTextName.text.toString(), mBinding.editTextEmail.text.toString(),mBinding.editTextMobile.text.toString(),GENDER)
                } else {
                    // If sign up fails, display a message to the user.
                    // You can also get the exception using task.exception
                    mBinding.svLoading.visibility = View.GONE
                    Helper.showCustomAppAlert("SignUp failed. ${task.exception?.message}",R.drawable.ic_bell_alert,this)
//                    Toast.makeText(
//                        MentalIllnessApp.getContext(),
//                        "SignUp failed. ${task.exception?.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
    }

    private fun saveUserInfo(
        userId: String,
        username: String,
        email: String,
        phone: String,
        GENDER: String
    ) {
        val user = User(userId, username, email,phone,GENDER)
        val usersRef = database.reference.child("users")
        usersRef.child(userId).setValue(user)
            .addOnSuccessListener {
                // User information saved successfully
                mBinding.svLoading.visibility = View.GONE
                Helper.showCustomAppAlert("User signed up successfully.",R.drawable.ic_bell_alert,this)
//                Toast.makeText(
//                    MentalIllnessApp.getContext(), "User signed up successfully.",
//                    Toast.LENGTH_SHORT
//                ).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this,SignInActivity::class.java))
                    finishAffinity()
                },3000)


            }
            .addOnFailureListener {
                // Failed to save user information
                mBinding.svLoading.visibility = View.GONE
                Helper.showCustomAppAlert("Failed to save user information.",R.drawable.ic_bell_alert,this)
//                Toast.makeText(
//                    MentalIllnessApp.getContext(), "Failed to save user information.",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
    }
}