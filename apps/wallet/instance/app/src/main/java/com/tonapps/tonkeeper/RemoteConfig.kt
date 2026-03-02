package com.tonapps.tonkeeper

import android.content.Context
import com.tonapps.wallet.api.API

class RemoteConfig(context: Context, private val api: API) {

    fun fetchAndActivate() = Unit

    val inAppUpdateAvailable: Boolean
        get() = false

    val nativeOnrmapEnabled: Boolean
        get() = true

    val isCountryPickerDisable: Boolean
        get() = false

    val hardcodedCountryCode: String?
        get() = null

    val isDappsDisable: Boolean
        get() = true

    val isOnboardingStoriesEnabled: Boolean
        get() = true
}
