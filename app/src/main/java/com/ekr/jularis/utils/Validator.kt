package com.ekr.jularis.utils

import java.util.regex.Pattern

object Validator {
    private const val EMAIL_PATTERN =
        "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

    private const val PASSWORD_PATTERN = /* "(?=.*[0-9])" +  //at least 1 digit
            +  //at least 1 lower case letter
            "(?=.*[A-Z])" +  //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +  //any letter*/
            //                            "(?=.*[@#$%^&+=_~!*()])"+//at least 1 special character
            "(?=.*[A-Z])"+
            ".{8,}" //at least 10 characters


    fun validateEmail(email: String): Boolean {
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun validatePassword(password: String): Boolean {
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
}