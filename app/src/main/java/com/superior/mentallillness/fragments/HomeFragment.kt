package com.superior.mentallillness.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.superior.mentallillness.R
import com.superior.mentallillness.activities.ChatActivity
import com.superior.mentallillness.activities.DoctorListActivity
import com.superior.mentallillness.activities.SignInActivity
import com.superior.mentallillness.databinding.FragmentHomeBinding
import com.superior.mentallillness.databinding.FragmentProfileBinding
import com.superior.mentallillness.helpers.Helper


class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(layoutInflater)



        mBinding.rlChat.setOnClickListener {
          startActivity(Intent(requireActivity(),ChatActivity::class.java))
        }
        mBinding.rlDoctors.setOnClickListener{
            startActivity(Intent(requireActivity(),DoctorListActivity::class.java))
        }
        mBinding.rlExcercise.setOnClickListener{
            searchYouTube("Best Exercise for mental peace")
        }


        return mBinding.root
    }
    fun searchYouTube(query: String) {
        val intent = Intent(Intent.ACTION_SEARCH)
        intent.putExtra("query", query)
        intent.setPackage("com.google.android.youtube") // Optional, to ensure it opens in the YouTube app if available

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            // Handle the case where YouTube app is not installed
            // You can open the search in a web browser as an alternative
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=$query"))
            startActivity(webIntent)
        }
    }


}