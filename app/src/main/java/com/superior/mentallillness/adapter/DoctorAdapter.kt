package com.superior.mentallillness.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.superior.mentallillness.R
import com.superior.mentallillness.model.DoctorModal
import de.hdodenhof.circleimageview.CircleImageView

class DoctorAdapter(private val context: Context, private val doctorList: List<DoctorModal>, private val onItemClick: (DoctorModal) -> Unit) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctorList[position]
        holder.bind(doctor)
    }

    override fun getItemCount(): Int {
        return doctorList.size
    }

    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDocName: TextView = itemView.findViewById(R.id.tvDocName)
        private val tvDocSpec: TextView = itemView.findViewById(R.id.tvDocSpec)
        private val ivDocPic: CircleImageView = itemView.findViewById(R.id.ivDocPic)

        fun bind(doctor: DoctorModal) {
            tvDocName.text = doctor.name
            tvDocSpec.text = doctor.specialization
            Glide.with(context).load(R.drawable.ic_profile).into(ivDocPic)
            itemView.setOnClickListener {
                onItemClick.invoke(doctor)
            }
        }
    }
}