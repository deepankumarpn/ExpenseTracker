package deepankumarpn.github.io.expensetracker.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.installations.FirebaseInstallations
import deepankumarpn.github.io.expensetracker.domain.model.CurrencyType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Currency
import java.util.Locale
import java.util.UUID

/**
 * Android implementation of AppInfo.
 */
class AndroidAppInfo(private val context: Context) : AppInfo {
    companion object {
        @Volatile
        private var instance: AndroidAppInfo? = null

        fun getInstance(context: Context): AndroidAppInfo {
            return instance ?: synchronized(this) {
                instance ?: AndroidAppInfo(context.applicationContext).also { instance = it }
            }
        }
    }

    private val dispatcher = Dispatchers.IO
    private var launchSessionId: String? = null
    override val versionName: String = "1.0.0"
    override val versionCode: Int = 1
    override val packageName: String = "deepankumarpn.github.io.expensetracker"
    override val platform: String = "Android"
    override val osVersion: String = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    override val deviceModel: String = "${Build.MANUFACTURER} ${Build.MODEL}"

    /**
     * Detects if this is a debug build by checking multiple indicators:
     * 1. Build.FINGERPRINT contains "test-keys" (debug/unofficial builds)
     * 2. Build.TYPE is "userdebug" or "eng" (engineering builds)
     * 3. Build.TAGS contains "test-keys" or "dev-keys"
     * 4. Application is debuggable (requires context, so not used here)
     */
    override val isDebug: Boolean =
        Build.FINGERPRINT.contains("test-keys", ignoreCase = true) ||
        Build.TYPE.equals("userdebug", ignoreCase = true) ||
        Build.TYPE.equals("eng", ignoreCase = true) ||
        Build.TAGS.contains("test-keys", ignoreCase = true) ||
        Build.TAGS.contains("dev-keys", ignoreCase = true)

    override fun getDefaultCurrency(): CurrencyType {
        return try {
            val currencyCode = getCurrencyCode()
            CurrencyType.fromCode(currencyCode)
        } catch (_: Exception) {
            CurrencyType.USD // Fallback to USD
        }
    }

    override fun getCountryCode(): String {
        return try {
            Locale.getDefault().country
        } catch (_: Exception) {
            "US" // Default to US if unable to get country
        }
    }

    override fun getCurrencyCode(): String {
        return try {
            val locale = Locale.getDefault()
            val currency = Currency.getInstance(locale)
            currency.currencyCode
        } catch (_: Exception) {
            // If unable to get currency from locale, try to map country code
            mapCountryToCurrency(getCountryCode())
        }
    }

    private fun mapCountryToCurrency(countryCode: String): String {
        return when (countryCode.uppercase()) {
            // Major currencies
            "US" -> "USD"
            "GB" -> "GBP"
            "JP" -> "JPY"
            "CN" -> "CNY"

            // Eurozone countries (19 countries)
            "AT", "BE", "CY", "EE", "FI", "FR", "DE", "GR", "IE",
            "IT", "LV", "LT", "LU", "MT", "NL", "PT", "SK", "SI", "ES" -> "EUR"

            // Asia Pacific
            "IN" -> "INR"
            "AU" -> "AUD"
            "NZ" -> "NZD"
            "SG" -> "SGD"
            "HK" -> "HKD"
            "KR" -> "KRW"
            "TW" -> "TWD"
            "TH" -> "THB"
            "MY" -> "MYR"
            "ID" -> "IDR"
            "PH" -> "PHP"
            "VN" -> "VND"
            "PK" -> "PKR"
            "BD" -> "BDT"
            "LK" -> "LKR"
            "NP" -> "NPR"
            "MM" -> "MMK"
            "KH" -> "KHR"
            "LA" -> "LAK"
            "BN" -> "BND"
            "MO" -> "MOP"
            "AF" -> "AFN"
            "MV" -> "MVR"
            "MN" -> "MNT"

            // Americas - North
            "CA" -> "CAD"
            "MX" -> "MXN"
            "GT" -> "GTQ"
            "HN" -> "HNL"
            "NI" -> "NIO"
            "CR" -> "CRC"
            "PA" -> "PAB"
            "BZ" -> "BZD"

            // Americas - Caribbean
            "CU" -> "CUP"
            "DO" -> "DOP"
            "JM" -> "JMD"
            "TT" -> "TTD"
            "BS" -> "BSD"
            "BB" -> "BBD"
            "AG", "DM", "GD", "KN", "LC", "VC" -> "XCD" // East Caribbean Dollar

            // Americas - South
            "BR" -> "BRL"
            "AR" -> "ARS"
            "CL" -> "CLP"
            "CO" -> "COP"
            "PE" -> "PEN"
            "VE" -> "VEF"
            "UY" -> "UYU"
            "PY" -> "PYG"
            "BO" -> "BOB"

            // Europe (non-Eurozone)
            "CH" -> "CHF"
            "SE" -> "SEK"
            "NO" -> "NOK"
            "DK" -> "DKK"
            "IS" -> "ISK"
            "PL" -> "PLN"
            "CZ" -> "CZK"
            "HU" -> "HUF"
            "RO" -> "RON"
            "BG" -> "BGN"
            "HR" -> "HRK"
            "RS" -> "RSD"
            "RU" -> "RUB"
            "UA" -> "UAH"
            "BY" -> "BYN"
            "TR" -> "TRY"
            "GE" -> "GEL"
            "AM" -> "AMD"
            "AZ" -> "AZN"
            "MD" -> "MDL"
            "BA" -> "BAM"
            "MK" -> "MKD"
            "AL" -> "ALL"

            // Middle East
            "AE" -> "AED"
            "SA" -> "SAR"
            "QA" -> "QAR"
            "OM" -> "OMR"
            "KW" -> "KWD"
            "BH" -> "BHD"
            "JO" -> "JOD"
            "LB" -> "LBP"
            "SY" -> "SYP"
            "IQ" -> "IQD"
            "YE" -> "YER"
            "IL" -> "ILS"
            "IR" -> "IRR"

            // Africa - North
            "EG" -> "EGP"
            "MA" -> "MAD"
            "TN" -> "TND"
            "DZ" -> "DZD"
            "LY" -> "LYD"
            "SD" -> "SDG"
            "SS" -> "SSP"

            // Africa - Sub-Saharan
            "ZA" -> "ZAR"
            "NG" -> "NGN"
            "KE" -> "KES"
            "GH" -> "GHS"
            "TZ" -> "TZS"
            "UG" -> "UGX"
            "ET" -> "ETB"
            "MU" -> "MUR"
            "SC" -> "SCR"
            "MW" -> "MWK"
            "ZM" -> "ZMW"
            "BW" -> "BWP"
            "NA" -> "NAD"
            "SZ" -> "SZL"
            "LS" -> "LSL"
            "AO" -> "AOA"
            "MZ" -> "MZN"
            "RW" -> "RWF"
            "BI" -> "BIF"
            "DJ" -> "DJF"
            "SO" -> "SOS"
            "GM" -> "GMD"
            "SL" -> "SLL"
            "GN" -> "GNF"
            "LR" -> "LRD"
            "CV" -> "CVE"

            // West African CFA Franc zone
            "BJ", "BF", "CI", "GW", "ML", "NE", "SN", "TG" -> "XOF"

            // Central African CFA Franc zone
            "CM", "CF", "TD", "CG", "GQ", "GA" -> "XAF"

            // Oceania
            "FJ" -> "FJD"
            "PG" -> "PGK"
            "WS" -> "WST"
            "TO" -> "TOP"
            "VU" -> "VUV"
            "SB" -> "SBD"
            "NC", "PF", "WF" -> "XPF" // CFP Franc

            // Central Asia
            "KZ" -> "KZT"
            "UZ" -> "UZS"
            "TJ" -> "TJS"
            "KG" -> "KGS"
            "TM" -> "TMT"

            else -> "USD" // Default to USD for unknown countries
        }
    }

    // ============================================
    // IDENTITY IDs
    // ============================================

    override suspend fun getAdvertisingId(): String? = withContext(dispatcher) {
        try {
            val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
            if (adInfo.isLimitAdTrackingEnabled) "limited" else adInfo.id
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getInstallationId(): String? = try {
        FirebaseInstallations.getInstance().id.await()
    } catch (e: Exception) {
        null
    }

    override suspend fun getAndroidId(): String = withContext(dispatcher) {
        Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown"
    }

    override suspend fun getLaunchSessionId(): String {
        if (launchSessionId == null) {
            launchSessionId = UUID.randomUUID().toString()
        }
        return launchSessionId!!
    }
}

// Platform-specific context holder
private object ContextHolder {
    lateinit var applicationContext: Context
}

fun initializeAppInfo(context: Context) {
    ContextHolder.applicationContext = context.applicationContext
}

actual fun getAppInfo(): AppInfo {
    return AndroidAppInfo.getInstance(ContextHolder.applicationContext)
}
