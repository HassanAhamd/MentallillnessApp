package com.superior.mentallillness.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.superior.mentallillness.R
import com.superior.mentallillness.databinding.ActivityDoctorDetailsBinding
import com.superior.mentallillness.databinding.ActivityDoctorListBinding
import com.superior.mentallillness.helpers.Helper
import com.superior.mentallillness.model.DoctorModal

class DoctorDetailsActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityDoctorDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDoctorDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        var window = this.window
        window.statusBarColor = this.resources.getColor(R.color.white);
        ForgetPasswordActivity.setSystemUiFlags(this)

        val selectedDoctor = intent.getSerializableExtra("selectedDoctor") as? DoctorModal
        if (selectedDoctor != null) {
            Glide.with(this).load(R.drawable.ic_profile).into(mBinding.ivDocPic)
            mBinding.tvDocName.setText(selectedDoctor.name)
            mBinding.tvDocGender.setText(selectedDoctor.gender)
            mBinding.tvDocPhone.setText(selectedDoctor.phone)
            mBinding.tvDocSpec.setText(selectedDoctor.specialization)
        }
        initListners(selectedDoctor!!)
    }

    private fun initListners(selectedDoctor: DoctorModal) {
        mBinding.rlCall.setOnClickListener {
            openDialer(selectedDoctor.phone)
        }
    }

    fun openDialer(phoneNumber: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$phoneNumber")

        if (dialIntent.resolveActivity(packageManager) != null) {
            startActivity(dialIntent)
        } else {
           Helper.showCustomAppAlert("Something went wrong. Please try again",R.drawable.ic_bell_alert,this)
        }

    }
}