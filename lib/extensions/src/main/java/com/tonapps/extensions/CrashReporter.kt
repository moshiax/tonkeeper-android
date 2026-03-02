package com.tonapps.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

object CrashReporter {

    @Volatile
    private var appContext: Context? = null

    fun install(context: Context) {
        appContext = context.applicationContext
        val previousHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            copyToClipboard(throwable)
            previousHandler?.uncaughtException(thread, throwable)
        }
    }

    fun recordException(throwable: Throwable) {
        Log.e("CrashReporter", "recordException", throwable)
        copyToClipboard(throwable)
    }

    private fun copyToClipboard(throwable: Throwable) {
        runCatching {
            val stacktrace = StringWriter().also { throwable.printStackTrace(PrintWriter(it)) }.toString()
            val clipboard = appContext?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
            clipboard.setPrimaryClip(ClipData.newPlainText("Crash stacktrace", stacktrace))
        }
    }
}
