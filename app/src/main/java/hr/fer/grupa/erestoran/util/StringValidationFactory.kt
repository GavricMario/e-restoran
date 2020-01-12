package hr.fer.grupa.erestoran.util

const val USER_NAME_REGEX = "^[A-Za-z][A-Za-z0-9_]{5,29}$"
fun isValidUsername(userName: String): Boolean {
    return USER_NAME_REGEX.toRegex().matches(userName)
}

fun isEmailValid(email: String) {
    TODO("validacija email adrese")
}