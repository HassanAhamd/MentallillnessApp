package com.superior.mentallillness.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.superior.mentallillness.R
import com.superior.mentallillness.activities.ForgetPasswordActivity.Companion.setSystemUiFlags
import com.superior.mentallillness.databinding.ActivityMainBinding
import com.superior.mentallillness.fragments.HomeFragment
import com.superior.mentallillness.fragments.ProfileFragment
import com.superior.mentallillness.helpers.Helper

class MainActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityMainBinding
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        var window = this.window
        window.statusBarColor = this.resources.getColor(R.color.white);
        setSystemUiFlags(this)



        initListners()
        loadFragment()
    }

    private fun loadFragment() {
        val homeFragment = HomeFragment()
        currentFragment = homeFragment
        // Add the fragment to the 'fragment_container' FrameLayout
        supportFragmentManager.beginTransaction()
            .add(R.id.container, homeFragment, null)
            .commit()
    }

    private fun initListners() {
        mBinding.bottomBar.setOnItemSelectedListener {
            if (it == 0){
                if (currentFragment != HomeFragment()){
                    val homeFragment = HomeFragment()
                    currentFragment = homeFragment
                    // Add the fragment to the 'fragment_container' FrameLayout
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, homeFragment, null)
                        .commit()
                }
            }else if (it == 1){
                if (currentFragment != ProfileFragment()){
                    val profileFragment = ProfileFragment()
                    currentFragment = profileFragment
                    // Add the fragment to the 'fragment_container' FrameLayout
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, profileFragment, null)
                        .commit()
                }
            }
        }


//     mBinding.rlLogout.setOnClickListener {
//         mBinding.svLoading.visibility = View.VISIBLE
//         logoutUser()
//     }
    }

    //    private void showConfirmationDialog(Activity activity) {
    //
    //        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
    //
    //        View view = LayoutInflater.from(BaseApplication.getContext()).inflate(R.layout.exit_dialog, null);
    //
    //        TextView tvMessage = view.findViewById(R.id.dialog_description);
    //        TextView tvTitle = view.findViewById(R.id.dialog_title);
    //        FrameLayout footerView = view.findViewById(R.id.footerView);
    //
    //
    //        tvMessage.setText("Are you sure you want to exit the app?");
    //        tvTitle.setText("Sure?");
    //
    //        Button btnYes = view.findViewById(R.id.btn_yes);
    //        Button btnNo = view.findViewById(R.id.btn_no);
    //
    //        AdsManager.getInstance().loadNative(footerView, getLayoutInflater(), R.layout.ad_app_install_banner);
    //
    //        btnYes.setOnClickListener(v -> {
    //            super.onBackPressed();
    //
    //        });
    //
    //        btnNo.setOnClickListener(v -> {
    //            dialog.dismiss();
    //        });
    //
    //        builder.setView(view);
    //
    //        dialog = builder.create();
    //        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    //        if (!isFinishing())
    //            dialog.show();
    //
    //    }
    private fun prepareExitDialog() {
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)
        var exitDialog = dialogBuilder.create()
        exitDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        exitDialog.setView(dialogView)
        var btnNo = dialogView.findViewById<TextView>(R.id.btnNo)
      var BtnYes  = dialogView.findViewById<TextView>(R.id.btnYes)
        btnNo.setOnClickListener {
            exitDialog.dismiss()
        }
        BtnYes.setOnClickListener {
            finish()
        }
        exitDialog.show()
    }

    override fun onBackPressed() {
        prepareExitDialog()
    }
}