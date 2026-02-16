package deepankumarpn.github.io.expensetracker.utils

import deepankumarpn.github.io.expensetracker.domain.model.CurrencyType

/**
 * Interface for providing app-level information and configuration.
 */
interface AppInfo {
    /**
     * App version name (e.g., "1.0.0")
     */
    val versionName: String

    /**
     * App version code (e.g., 1)
     */
    val versionCode: Int

    /**
     * App package/bundle identifier
     */
    val packageName: String

    /**
     * Platform name (e.g., "Android", "iOS")
     */
    val platform: String

    /**
     * OS version
     */
    val osVersion: String

    /**
     * Device model/name
     */
    val deviceModel: String

    /**
     * Whether this is a debug build
     */
    val isDebug: Boolean

    /**
     * Get the default currency based on device locale.
     */
    fun getDefaultCurrency(): CurrencyType

    /**
     * Get the device's country code (e.g., "US", "IN", "GB").
     */
    fun getCountryCode(): String

    /**
     * Get the device's currency code (e.g., "USD", "INR", "GBP").
     */
    fun getCurrencyCode(): String

    /**
     * Get the advertising ID (GAID/IDFA) for the device.
     * Returns null if unavailable or if ad tracking is limited.
     */
    suspend fun getAdvertisingId(): String?

    /**
     * Get the Firebase Installation ID.
     * Returns null if Firebase is not initialized or unavailable.
     */
    suspend fun getInstallationId(): String?

    /**
     * Get the Android ID (SSAID) or equivalent device identifier.
     */
    suspend fun getAndroidId(): String

    /**
     * Get the launch session ID for the current app session.
     * This ID persists across app restarts until explicitly reset.
     */
    suspend fun getLaunchSessionId(): String
}

/**
 * Get the platform-specific app info.
 */
expect fun getAppInfo(): AppInfo
