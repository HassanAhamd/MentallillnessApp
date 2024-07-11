package com.superior.mentallillness.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.superior.mentallillness.R
import com.superior.mentallillness.activities.MainActivity
import com.superior.mentallillness.activities.SignInActivity
import com.superior.mentallillness.databinding.FragmentHomeBinding
import com.superior.mentallillness.databinding.FragmentProfileBinding
import com.superior.mentallillness.helpers.Helper
import com.superior.mentallillness.model.User

class ProfileFragment : Fragment() {

    private lateinit var mBinding: FragmentProfileBinding
    var PICK_IMAGE_REQUEST = 101
    var READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 102

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileBinding.inflate(layoutInflater)

        mBinding.rlLogout.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            Helper.showCustomAppAlert("Logout Successful", R.drawable.ic_bell_alert,requireActivity())
            Handler(Looper.getMainLooper()).postDelayed({
                requireActivity().startActivity(Intent(requireActivity(),SignInActivity::class.java))
                requireActivity().finishAffinity()
            },2000)
        }

        mBinding.profileImage.setOnClickListener{
            if (hasReadExternalStoragePermission()) {
                pickImageFromGallery()
            }else{
                requestReadExternalStoragePermission()
            }
        }

        loadDataFromFirebase()
        var image = getSavedImageUri()
        if (image != null){
            Glide
                .with(this)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .into(mBinding.profileImage);
        }

        return mBinding.root
    }
    private fun hasReadExternalStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arrayOf(Manifest.permission.READ_MEDIA_IMAGES) else arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to pick an image
                pickImageFromGallery()
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun loadDataFromFirebase() {
        mBinding.svLoading.visibility = View.VISIBLE
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        userId?.let { uid ->
            // Create a query to get the user with the specified UID
            val userQuery: Query = usersRef.child(uid)

            // Add a ValueEventListener to listen for changes in the data
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Check if the snapshot has data
                    if (snapshot.exists()) {
                        mBinding.svLoading.visibility = View.GONE
                        // Retrieve user data
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            mBinding.editTextUsername.setText(it.username)
                            mBinding.editTextEmail.setText(it.email)
                            mBinding.editTextPhone.setText(it.phone)
                            mBinding.editTextGender.setText(it.gender)
                        }


                    } else {
                        // User does not exist or there's no data for the specified UID
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    mBinding.svLoading.visibility = View.GONE
                }
            })
        }
    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data

            // Save the selected image URI to SharedPreferences
            saveImageUriToSharedPreferences(selectedImageUri.toString())
        }
    }

    private fun saveImageUriToSharedPreferences(uri: String) {
        val sharedPreferences = requireActivity().getPreferences(MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("selected_image_uri", uri)
        editor.apply()
        Glide
            .with(this)
            .load(uri)
            .centerCrop()
            .placeholder(R.drawable.ic_profile)
            .into(mBinding.profileImage);
    }
    private fun getSavedImageUri(): String? {
        val sharedPreferences = requireActivity().getPreferences(MODE_PRIVATE)
        return sharedPreferences.getString("selected_image_uri", null)
    }
}