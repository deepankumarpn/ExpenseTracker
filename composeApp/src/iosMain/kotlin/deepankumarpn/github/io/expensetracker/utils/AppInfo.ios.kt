package deepankumarpn.github.io.expensetracker.utils

import deepankumarpn.github.io.expensetracker.domain.model.CurrencyType
import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.NSUUID
import platform.Foundation.currentLocale
import platform.Foundation.countryCode
import platform.Foundation.currencyCode
import platform.UIKit.UIDevice
import platform.UIKit.identifierForVendor

/**
 * iOS implementation of AppInfo.
 */
class IosAppInfo : AppInfo {
    private val bundle = NSBundle.mainBundle
    private var launchSessionId: String? = null

    override val versionName: String
        get() = bundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "1.0.0"

    override val versionCode: Int
        get() = (bundle.objectForInfoDictionaryKey("CFBundleVersion") as? String)?.toIntOrNull() ?: 1

    override val packageName: String
        get() = bundle.bundleIdentifier ?: "deepankumarpn.github.io.expensetracker"

    override val platform: String = "iOS"

    override val osVersion: String
        get() = "${UIDevice.currentDevice.systemName} ${UIDevice.currentDevice.systemVersion}"

    override val deviceModel: String
        get() = UIDevice.currentDevice.model

    override val isDebug: Boolean
        get() {
            // Check if running on simulator (debug builds typically run on simulator)
            val isSimulator = UIDevice.currentDevice.model.contains("Simulator")

            // Check if the app has embedded.mobileprovision (debug/development builds)
            val provisioningPath = bundle.pathForResource("embedded", "mobileprovision")
            val hasProvisioningProfile = provisioningPath != null

            return isSimulator || hasProvisioningProfile
        }

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
            NSLocale.currentLocale.countryCode ?: "US"
        } catch (_: Exception) {
            "US" // Default to US if unable to get country
        }
    }

    override fun getCurrencyCode(): String {
        return try {
            NSLocale.currentLocale.currencyCode ?: mapCountryToCurrency(getCountryCode())
        } catch (_: Exception) {
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
            "CM", "CF", "TD", "CG", "EQ", "GA" -> "XAF"

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

    override suspend fun getAdvertisingId(): String? {
        // IDFA (Identifier for Advertisers) on iOS
        // Note: Requires proper App Tracking Transparency framework setup
        // For now, returning null as stub
        return null
    }

    override suspend fun getInstallationId(): String? {
        // Firebase Installation ID - requires Firebase SDK for iOS
        // For now, returning null as stub
        return null
    }

    override suspend fun getAndroidId(): String {
        // iOS equivalent: Identifier for Vendor (IDFV)
        return UIDevice.currentDevice.identifierForVendor?.UUIDString ?: "unknown"
    }

    override suspend fun getLaunchSessionId(): String {
        if (launchSessionId == null) {
            launchSessionId = NSUUID().UUIDString
        }
        return launchSessionId!!
    }
}

actual fun getAppInfo(): AppInfo = IosAppInfo()
