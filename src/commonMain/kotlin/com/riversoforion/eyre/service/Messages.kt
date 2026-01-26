package com.riversoforion.eyre.service

interface Messages {

    fun info(message: String)

    fun warn(message: String)

    fun error(message: String)
}
