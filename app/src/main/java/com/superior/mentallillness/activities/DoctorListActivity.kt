package com.superior.mentallillness.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.superior.mentallillness.R
import com.superior.mentallillness.adapter.DoctorAdapter
import com.superior.mentallillness.databinding.ActivityDoctorListBinding
import com.superior.mentallillness.model.DoctorModal

class DoctorListActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityDoctorListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDoctorListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        var window = this.window
        window.statusBarColor = this.resources.getColor(R.color.white);
        ForgetPasswordActivity.setSystemUiFlags(this)
        val doctorList = listOf(
            DoctorModal(1, "Dr. Smith", "Psychiatrist from UK with 7-Years of Experience", "123-456-7890","MALE"),
            DoctorModal(2, "Dr. Johnson", "Psychiatrist from UAE with 3-Years of Experience", "987-654-3210","MALE"),
            DoctorModal(3, "Dr. Williams", "Psychiatrist from PK with 5-Years of Experience", "456-789-0123","MALE"),
            DoctorModal(4, "Dr. Davis", "Psychiatrist from IND with 6-Years of Experience ", "789-012-3456","FEMALE"),
            DoctorModal(5, "Dr. Miller", "Psychiatrist from UK with 4-Years of Experience", "234-567-8901","MALE"),
            DoctorModal(6, "Dr. Wilson", "Psychiatrist from AUS with 3-Years of Experience", "567-890-1234","MALE"),
            DoctorModal(7, "Dr. Brown", "Psychiatrist from PK with 12-Years of Experience", "012-345-6789","MALE"),
            DoctorModal(8, "Dr. Jones", "Psychiatrist from UK with 5-Years of Experience", "345-678-9012","MALE"),
        )
        intiAdapter(doctorList)
    }

    private fun intiAdapter(doctorList: List<DoctorModal>) {
        val adapter = DoctorAdapter(this, doctorList) { selectedDoctor ->
            // Handle item click, for example, start another activity
            val intent = Intent(this, DoctorDetailsActivity::class.java)
            intent.putExtra("selectedDoctor", selectedDoctor)
            startActivity(intent)
        }

        mBinding.rvDocs.layoutManager = LinearLayoutManager(this)
        mBinding.rvDocs.adapter = adapter
    }
}