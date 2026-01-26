package com.riversoforion.eyre

import io.kotest.core.config.AbstractProjectConfig

@Suppress("unused")
class EyreTestConfig : AbstractProjectConfig() {

    override val displayFullTestPath = true
    override val globalAssertSoftly = true
}
