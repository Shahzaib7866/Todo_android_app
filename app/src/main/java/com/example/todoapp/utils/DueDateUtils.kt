package com.example.todoapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DueDateUtils {

    // MaterialDatePicker UTC midnight deta hai -> apne local din ki shuruaat
    fun pickerToLocalStart(utcMillis: Long): Long {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = utcMillis }
        return Calendar.getInstance().apply {
            clear()
            set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH), utc.get(Calendar.DAY_OF_MONTH))
        }.timeInMillis
    }

    // Edit karte waqt saved date ko picker ke UTC format mein wapas dena
    fun localToPicker(localMillis: Long): Long {
        val local = Calendar.getInstance().apply { timeInMillis = localMillis }
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(local.get(Calendar.YEAR), local.get(Calendar.MONTH), local.get(Calendar.DAY_OF_MONTH))
        }.timeInMillis
    }

    fun format(millis: Long): String =
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(millis))
}