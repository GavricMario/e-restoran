package hr.fer.grupa.erestoran

fun checkCreditCard(creditCard:String?):Boolean{
    val number:Long?=creditCard!!.toLongOrNull()
    return if(number==null)
        false
    else{
        (getSize(number) in 13..16) &&
                (prefixMatched(number, 4) ||
                        prefixMatched(number, 5) ||
                        prefixMatched(number, 37) ||
                        prefixMatched(number, 6)) &&
                ((sumOfDoubleEvenPlace(number) +
                        sumOfOddPlace(number)) % 10 == 0)
    }
}


fun prefixMatched(number: Long, d: Int): Boolean {
    return getPrefix(number, getSize(d.toLong())).toInt() == d
}
fun sumOfOddPlace(number: Long): Int {
    var sum = 0
    val num = number.toString() + ""
    var i = getSize(number) - 1
    while (i >= 0) {
        sum += Integer.parseInt(num[i] + "")
        i -= 2
    }
    return sum
}
fun getSize(d: Long): Int {
    val num = d.toString() + ""
    return num.length
}
fun getDigit(number: Int): Int {
    return if (number < 9) number else number / 10 + number % 10
}
fun sumOfDoubleEvenPlace(number: Long): Int {
    var sum = 0
    val num = number.toString() + ""
    var i = getSize(number) - 2
    while (i >= 0) {
        sum += getDigit(Integer.parseInt(num[i] + "") * 2)
        i -= 2
    }

    return sum
}
fun getPrefix(number: Long, k: Int): Long {
    if (getSize(number) > k) {
        val num = number.toString() + ""
        return java.lang.Long.parseLong(num.substring(0, k))
    }
    return number
}