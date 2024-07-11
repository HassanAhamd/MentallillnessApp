package com.superior.mentallillness.helpers


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.text.SpannableString
import android.view.Gravity
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.superior.mentallillness.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


object Helper {


    fun showCustomAppAlert(title: String, imageRes: Int, activity: Activity) {
        var imgRes = R.drawable.ic_bell_alert
        if (imageRes != -1) {
            imgRes = imageRes
        }

        val layout = activity.layoutInflater.inflate(
            R.layout.app_alert_layout,
            activity.findViewById(R.id.toast_container)
        )
        val tvTitle = layout.findViewById<TextView>(R.id.titleToast)
        tvTitle.text = title
        val image = layout.findViewById<ImageView>(R.id.imageToast)
        image.setImageDrawable(ContextCompat.getDrawable(activity, imgRes))
        val dialog = AlertDialog.Builder(activity).setView(layout).create()
        val layoutParams = dialog.window!!.attributes
        layoutParams.y = 160 // top margin
        dialog.window!!.attributes = layoutParams
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window!!.setGravity(Gravity.TOP)
        dialog.show()
        val timer2 = Timer()
        timer2.schedule(object : TimerTask() {
            override fun run() {
                dialog.dismiss()
                timer2.cancel() //this will cancel the timer of the system
            }
        }, 2000)
    }
}

