const val CARD_DAY_LIMIT = 150_000
const val CARD_MONTH_LIMIT = 600_000
const val MIN_WITHOUT_COMMISSION_VALUE = 300
const val MAX_WITHOUT_COMMISSION_VALUE = 75_000
const val MAESTRO_PERCENT = 0.006
const val MAESTRO_FIX_COMMISSION = 20
const val VISA_FIX_COMMISSION = 35
const val VISA_PERCENT = 0.0075
const val VK_DAY_LIMIT = 15_000
const val VK_MONTH_LIMIT = 40_000
const val MASTERCARD = 1
const val VISA = 2
const val VK_PAY = 3
fun main() {
    print("Введите тип вашей банковской карты: $MASTERCARD - Mastercard/Maestro, $VISA - Visa/Мир, $VK_PAY - VkPay: ")
    val cardType = readLine()!!.toInt()
    if (cardType !in MASTERCARD..VK_PAY) {
        println("Тип карты должен быть числом $MASTERCARD...$VISA")
        return
    }
    var totalTransfers = 0
    while(true) {
        print("Введите сумму перевода, коп. (пустая строка - выход): ")
        val transfer = readLine()
        if(transfer!!.isEmpty()) {
            return
        }
        val transferValue = transfer.toInt()
        if(!checkLimits(cardType, totalTransfers + transferValue, transferValue)) {
            println("Превышен лимит по переводам. Попробуйте другую карту.")
            return
        }
        val commission = getCommission(cardType, totalTransfers + transferValue, transferValue)
        println("Комиссия за перевод составит: $commission коп.")
        totalTransfers += transferValue
    }
}
fun checkLimits(cardType: Int = VK_PAY, totalTransfers: Int, transferValue: Int): Boolean {
    return when {
        cardType < VK_PAY && transferValue <= CARD_DAY_LIMIT && (totalTransfers) <= CARD_MONTH_LIMIT -> true
        cardType == VK_PAY && transferValue <= VK_DAY_LIMIT && (totalTransfers) <= VK_MONTH_LIMIT -> true
        else -> false
    }
}
fun getCommission(cardType: Int = VK_PAY, totalTransfers: Int, transferValue: Int): Int {
    return when(cardType) {
        MASTERCARD -> getMaestroCommission(totalTransfers, transferValue)
        VISA -> getVisaCommission(transferValue)
        else -> 0
    }
}
fun getMaestroCommission(totalTransfers: Int, transferValue: Int): Int {
    val withoutCommission = totalTransfers in MIN_WITHOUT_COMMISSION_VALUE..MAX_WITHOUT_COMMISSION_VALUE
    return if (withoutCommission) 0
    else (transferValue * MAESTRO_PERCENT + MAESTRO_FIX_COMMISSION).toInt()
}
fun getVisaCommission(transferValue: Int): Int {
    val moreWhenFixMinimalCommission = (transferValue * VISA_PERCENT).toInt() > VISA_FIX_COMMISSION
    return if (moreWhenFixMinimalCommission) (transferValue * VISA_PERCENT).toInt()
    else VISA_FIX_COMMISSION
}