package com.tonapps.tonkeeper.core

import android.util.Log
import com.tonapps.wallet.data.settings.SafeModeState

object FirebaseHelper {

    fun secureModeEnabled(state: SafeModeState) {
        Log.d("AnalyticsEvents", "secure_mode_enabled: $state")
    }

    fun trc20Enabled(enabled: Boolean) {
        Log.d("AnalyticsEvents", "trc20_enabled: $enabled")
    }

    fun searchEngine(value: String) {
        Log.d("AnalyticsEvents", "search_engine: $value")
    }

    fun setTitleEmoji(emoji: String) {
        Log.d("AnalyticsEvents", "set_title_emoji: $emoji")
    }
}
