package com.superior.mentallillness

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

class MentalIllnessApp: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseApp.initializeApp(this)
    }
    companion object {
        private lateinit var instance: MentalIllnessApp
        fun getContext(): Context {
            return instance.applicationContext
        }
    }
}