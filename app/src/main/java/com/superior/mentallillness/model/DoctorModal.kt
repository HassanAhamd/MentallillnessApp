package com.superior.mentallillness.model

import android.graphics.drawable.Drawable
import java.io.Serial
import java.io.Serializable

data class DoctorModal(val id: Int, val name: String, val specialization: String, var phone: String ,var gender: String):
    Serializable
