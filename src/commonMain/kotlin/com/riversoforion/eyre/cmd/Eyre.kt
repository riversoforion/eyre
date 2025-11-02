package com.riversoforion.eyre.cmd

import com.github.ajalt.clikt.command.SuspendingCliktCommand

class Eyre : SuspendingCliktCommand(name = "eyre") {
    override suspend fun run() = Unit
}
