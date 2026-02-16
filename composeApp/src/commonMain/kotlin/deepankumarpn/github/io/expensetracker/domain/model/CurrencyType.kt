package deepankumarpn.github.io.expensetracker.domain.model

import deepankumarpn.github.io.expensetracker.utils.getAppInfo

/**
 * Enum representing different currency types.
 */
enum class CurrencyType(val symbol: String, val code: String) {
    // Major Global Currencies
    USD("$", "USD"),          // United States Dollar
    EUR("€", "EUR"),          // Euro
    GBP("£", "GBP"),          // British Pound
    JPY("¥", "JPY"),          // Japanese Yen
    CNY("¥", "CNY"),          // Chinese Yuan

    // Asia Pacific
    INR("₹", "INR"),          // Indian Rupee
    AUD("A$", "AUD"),         // Australian Dollar
    NZD("NZ$", "NZD"),        // New Zealand Dollar
    SGD("S$", "SGD"),         // Singapore Dollar
    HKD("HK$", "HKD"),        // Hong Kong Dollar
    KRW("₩", "KRW"),          // South Korean Won
    TWD("NT$", "TWD"),        // Taiwan Dollar
    THB("฿", "THB"),          // Thai Baht
    MYR("RM", "MYR"),         // Malaysian Ringgit
    IDR("Rp", "IDR"),         // Indonesian Rupiah
    PHP("₱", "PHP"),          // Philippine Peso
    VND("₫", "VND"),          // Vietnamese Dong
    PKR("₨", "PKR"),          // Pakistani Rupee
    BDT("৳", "BDT"),          // Bangladeshi Taka
    LKR("Rs", "LKR"),         // Sri Lankan Rupee
    NPR("Rs", "NPR"),         // Nepalese Rupee
    MMK("K", "MMK"),          // Myanmar Kyat
    KHR("៛", "KHR"),          // Cambodian Riel
    LAK("₭", "LAK"),          // Lao Kip
    BND("B$", "BND"),         // Brunei Dollar
    MOP("MOP$", "MOP"),       // Macau Pataca
    AFN("؋", "AFN"),          // Afghan Afghani
    MVR("Rf", "MVR"),         // Maldivian Rufiyaa

    // Americas
    CAD("C$", "CAD"),         // Canadian Dollar
    MXN("$", "MXN"),          // Mexican Peso
    BRL("R$", "BRL"),         // Brazilian Real
    ARS("$", "ARS"),          // Argentine Peso
    CLP("$", "CLP"),          // Chilean Peso
    COP("$", "COP"),          // Colombian Peso
    PEN("S/", "PEN"),         // Peruvian Sol
    VEF("Bs", "VEF"),         // Venezuelan Bolívar
    UYU("\$U", "UYU"),        // Uruguayan Peso
    PYG("₲", "PYG"),          // Paraguayan Guaraní
    BOB("Bs.", "BOB"),        // Bolivian Boliviano
    CRC("₡", "CRC"),          // Costa Rican Colón
    GTQ("Q", "GTQ"),          // Guatemalan Quetzal
    HNL("L", "HNL"),          // Honduran Lempira
    NIO("C$", "NIO"),         // Nicaraguan Córdoba
    PAB("B/.", "PAB"),        // Panamanian Balboa
    DOP("RD$", "DOP"),        // Dominican Peso
    CUP("$", "CUP"),          // Cuban Peso
    JMD("J$", "JMD"),         // Jamaican Dollar
    TTD("TT$", "TTD"),        // Trinidad and Tobago Dollar
    BSD("B$", "BSD"),         // Bahamian Dollar
    BBD("Bds$", "BBD"),       // Barbadian Dollar
    BZD("BZ$", "BZD"),        // Belize Dollar
    XCD("EC$", "XCD"),        // East Caribbean Dollar

    // Europe (Non-Eurozone)
    CHF("Fr", "CHF"),         // Swiss Franc
    SEK("kr", "SEK"),         // Swedish Krona
    NOK("kr", "NOK"),         // Norwegian Krone
    DKK("kr", "DKK"),         // Danish Krone
    ISK("kr", "ISK"),         // Icelandic Króna
    PLN("zł", "PLN"),         // Polish Złoty
    CZK("Kč", "CZK"),         // Czech Koruna
    HUF("Ft", "HUF"),         // Hungarian Forint
    RON("lei", "RON"),        // Romanian Leu
    BGN("лв", "BGN"),         // Bulgarian Lev
    HRK("kn", "HRK"),         // Croatian Kuna
    RSD("din", "RSD"),        // Serbian Dinar
    RUB("₽", "RUB"),          // Russian Ruble
    UAH("₴", "UAH"),          // Ukrainian Hryvnia
    BYN("Br", "BYN"),         // Belarusian Ruble
    TRY("₺", "TRY"),          // Turkish Lira
    GEL("₾", "GEL"),          // Georgian Lari
    AMD("֏", "AMD"),          // Armenian Dram
    AZN("₼", "AZN"),          // Azerbaijani Manat
    MDL("L", "MDL"),          // Moldovan Leu
    BAM("KM", "BAM"),         // Bosnia-Herzegovina Mark
    MKD("ден", "MKD"),        // Macedonian Denar
    ALL("L", "ALL"),          // Albanian Lek

    // Middle East
    AED("د.إ", "AED"),        // UAE Dirham
    SAR("ر.س", "SAR"),        // Saudi Riyal
    QAR("ر.ق", "QAR"),        // Qatari Riyal
    OMR("ر.ع.", "OMR"),       // Omani Rial
    KWD("د.ك", "KWD"),        // Kuwaiti Dinar
    BHD("د.ب", "BHD"),        // Bahraini Dinar
    JOD("د.ا", "JOD"),        // Jordanian Dinar
    LBP("ل.ل", "LBP"),        // Lebanese Pound
    SYP("£S", "SYP"),         // Syrian Pound
    IQD("ع.د", "IQD"),        // Iraqi Dinar
    YER("﷼", "YER"),         // Yemeni Rial
    ILS("₪", "ILS"),          // Israeli Shekel
    IRR("﷼", "IRR"),         // Iranian Rial

    // Africa
    ZAR("R", "ZAR"),          // South African Rand
    EGP("E£", "EGP"),         // Egyptian Pound
    NGN("₦", "NGN"),          // Nigerian Naira
    KES("KSh", "KES"),        // Kenyan Shilling
    GHS("₵", "GHS"),          // Ghanaian Cedi
    TZS("TSh", "TZS"),        // Tanzanian Shilling
    UGX("USh", "UGX"),        // Ugandan Shilling
    ETB("Br", "ETB"),         // Ethiopian Birr
    MAD("د.م.", "MAD"),       // Moroccan Dirham
    TND("د.ت", "TND"),        // Tunisian Dinar
    DZD("د.ج", "DZD"),        // Algerian Dinar
    LYD("ل.د", "LYD"),        // Libyan Dinar
    SDG("ج.س.", "SDG"),       // Sudanese Pound
    SSP("£", "SSP"),          // South Sudanese Pound
    MUR("₨", "MUR"),          // Mauritian Rupee
    SCR("₨", "SCR"),          // Seychellois Rupee
    MWK("MK", "MWK"),         // Malawian Kwacha
    ZMW("ZK", "ZMW"),         // Zambian Kwacha
    BWP("P", "BWP"),          // Botswana Pula
    NAD("N$", "NAD"),         // Namibian Dollar
    SZL("L", "SZL"),          // Swazi Lilangeni
    LSL("L", "LSL"),          // Lesotho Loti
    AOA("Kz", "AOA"),         // Angolan Kwanza
    MZN("MT", "MZN"),         // Mozambican Metical
    RWF("FRw", "RWF"),        // Rwandan Franc
    BIF("FBu", "BIF"),        // Burundian Franc
    DJF("Fdj", "DJF"),        // Djiboutian Franc
    SOS("Sh", "SOS"),         // Somali Shilling
    GMD("D", "GMD"),          // Gambian Dalasi
    SLL("Le", "SLL"),         // Sierra Leonean Leone
    GNF("FG", "GNF"),         // Guinean Franc
    LRD("L$", "LRD"),         // Liberian Dollar
    CVE("$", "CVE"),          // Cape Verdean Escudo
    XOF("CFA", "XOF"),        // West African CFA Franc
    XAF("FCFA", "XAF"),       // Central African CFA Franc

    // Oceania
    FJD("FJ$", "FJD"),        // Fijian Dollar
    PGK("K", "PGK"),          // Papua New Guinean Kina
    WST("WS$", "WST"),        // Samoan Tālā
    TOP("T$", "TOP"),         // Tongan Paʻanga
    VUV("VT", "VUV"),         // Vanuatu Vatu
    SBD("SI$", "SBD"),        // Solomon Islands Dollar
    XPF("₣", "XPF"),          // CFP Franc

    // Other
    KZT("₸", "KZT"),          // Kazakhstani Tenge
    UZS("сўм", "UZS"),        // Uzbekistani Som
    TJS("ЅМ", "TJS"),         // Tajikistani Somoni
    KGS("с", "KGS"),          // Kyrgyzstani Som
    TMT("m", "TMT"),          // Turkmenistani Manat
    MNT("₮", "MNT");          // Mongolian Tögrög

    companion object {
        /**
         * Get CurrencyType from currency code.
         * Falls back to USD if code not found.
         */
        fun fromCode(code: String): CurrencyType {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: USD
        }

        /**
         * Get the default currency based on device locale.
         * Uses AppInfo to detect device locale and currency.
         */
        fun getDefaultCurrency(): CurrencyType {
            return try {
                val appInfo = getAppInfo()
                appInfo.getDefaultCurrency()
            } catch (_: Exception) {
                USD // Fallback to USD if unable to detect
            }
        }
    }
}
