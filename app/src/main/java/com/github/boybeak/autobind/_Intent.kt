package com.github.boybeak.autobind

import android.content.Context
import android.content.Intent

inline fun Intent.startActivity(context: Context) {
    context.startActivity(this)
}