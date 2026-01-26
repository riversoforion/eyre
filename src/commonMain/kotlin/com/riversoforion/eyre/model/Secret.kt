package com.riversoforion.eyre.model

sealed interface Secret {

    val id: String

    data class ApiToken(override val id: String, val token: String) : Secret

    data class ClientIdAndSecret(override val id: String, val clientId: String, val clientSecret: String) : Secret

    open class CustomSecret(override val id: String, val values: Map<String, Any>): Secret
}
