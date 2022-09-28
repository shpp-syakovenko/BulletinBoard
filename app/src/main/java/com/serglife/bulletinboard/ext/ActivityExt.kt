package com.serglife.bulletinboard.ext

import android.app.Activity
import android.graphics.Color
import android.view.WindowManager
import androidx.core.view.WindowCompat

fun Activity.makeTransparentStatusBar() {
    window.apply {
        WindowCompat.setDecorFitsSystemWindows(this, false)
        statusBarColor = Color.TRANSPARENT
        navigationBarColor = Color.TRANSPARENT
    }
}

fun Activity?.keepScreenOn(on: Boolean) {
    this?.window?.apply {
        if (on) {
            addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}
