package com.superior.mentallillness.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.superior.mentallillness.R

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        var window = this.window
        window.statusBarColor = this.resources.getColor(R.color.primaryColor);
        goToMain()
    }

    private fun goToMain() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (FirebaseAuth.getInstance().currentUser != null) {
                val mainIntent = Intent(this@StartActivity, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }else{
                val mainIntent = Intent(this@StartActivity, SignInActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }, 3000) //3 sec
    }
}