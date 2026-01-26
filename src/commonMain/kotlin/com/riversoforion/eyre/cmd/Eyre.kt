package com.riversoforion.eyre.cmd

import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.core.terminal
import com.riversoforion.eyre.service.Messages
import com.riversoforion.eyre.service.SecretStore
import com.riversoforion.eyre.service.impl.PlainTextSecretStore
import com.riversoforion.eyre.service.impl.TerminalWriter

class Eyre : SuspendingCliktCommand(name = "eyre") {

    override fun aliases(): Map<String, List<String>> = mapOf("a" to listOf("add"))

    override suspend fun run() {
        val out = currentContext.findOrSetObject<Messages>(OUT) {
            TerminalWriter(currentContext.terminal)
        }
        currentContext.findOrSetObject<SecretStore>(SECRET_STORE) {
            PlainTextSecretStore(out)
        }
    }

    companion object Ctx {

        const val OUT = "out"
        const val SECRET_STORE = "secretStore"
    }
}
