package com.riversoforion.eyre.model

import kotlin.time.Instant

data class Credential(val type: String, val name: String, val expiration: Instant, val secret: Secret)
