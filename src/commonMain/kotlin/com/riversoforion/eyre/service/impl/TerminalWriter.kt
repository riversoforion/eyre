package com.riversoforion.eyre.service.impl

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.danger
import com.github.ajalt.mordant.terminal.info
import com.github.ajalt.mordant.terminal.warning
import com.riversoforion.eyre.service.Messages

class TerminalWriter(private val terminal: Terminal) : Messages {

    override fun info(message: String) {
        terminal.info(message)
    }

    override fun warn(message: String) {
        terminal.warning(message)
    }

    override fun error(message: String) {
        terminal.danger(message)
    }
}
