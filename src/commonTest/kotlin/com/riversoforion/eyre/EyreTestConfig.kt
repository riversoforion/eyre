package com.riversoforion.eyre

import io.kotest.core.config.AbstractProjectConfig

@Suppress("unused")
class EyreTestConfig : AbstractProjectConfig() {

    override val displayFullTestPath = false
    override val globalAssertSoftly = true
}
