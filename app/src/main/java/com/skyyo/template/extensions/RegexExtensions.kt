package com.skyyo.template.extensions

inline val CharSequence?.isEmail: Boolean get() = isMatch(REGEX_EMAIL)

inline val CharSequence?.isName: Boolean get() = isMatch(REGEX_NAME)

inline val CharSequence?.isPassword: Boolean get() = isMatch(REGEX_PASSWORD)

inline val CharSequence?.isCreditCard: Boolean get() = isMatch(REGEX_CREDIT_CARD)

inline val CharSequence?.isExpireDate: Boolean get() = isMatch(EXPIRE_DATE)

inline val CharSequence?.isCvv2: Boolean get() = isMatch(REGEX_CVV2)

fun CharSequence?.isMatch(regex: String): Boolean =
    !this.isNullOrEmpty() && Regex(regex).matches(this)

const val REGEX_CREDIT_CARD = "^(?:4[0-9]{12}(?:[0-9]{3})?" + // visa
    "|(?:5[1-5][0-9]{2}" + // MasterCard
    "|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}" + // MasterCard
    "|3[47][0-9]{13}" + // American Express
    "|3(?:0[0-5]|[68][0-9])[0-9]{11}" + // Diners Club
    "|6(?:011|5[0-9]{2})[0-9]{12}" + // Discover
    "|(?:2131|1800|35\\d{3})\\d{11}" +
    ")\$" // JCB
val NON_DIGITS = Regex("[^\\d]")
const val EXPIRE_DATE = "(?:0[1-9]|1[0-2])/2[0-9]"
const val REGEX_CVV2 = "^[0-9]{3,4}\$"
const val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"
const val REGEX_NAME = "^[A-Za-z]+\$"
const val REGEX_PASSWORD = "^" +
    "(?=.*[0-9])" + // at least 1 digit
    "(?=.*[a-zA-Z])" + // any letter
    "(?=\\S+$)" + // no white spaces
    ".{8,}" + // at least 8 characters
    "$"
