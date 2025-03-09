package com.example.imagefilterandroid.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast

fun Context.displayToast(message: String?){
    if (!message.isNullOrBlank()) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    } else {
        Log.e("ToastError", "Attempted to show empty Toast message") // Optional: Log the issue
    }
}


fun View.show(){
    this.visibility = View.VISIBLE
}