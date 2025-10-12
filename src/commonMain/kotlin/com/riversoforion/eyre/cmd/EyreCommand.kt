package com.riversoforion.eyre.cmd

import com.github.ajalt.clikt.command.SuspendingCliktCommand

class EyreCommand : SuspendingCliktCommand(name = "eyre") {
    override suspend fun run() = Unit
}
