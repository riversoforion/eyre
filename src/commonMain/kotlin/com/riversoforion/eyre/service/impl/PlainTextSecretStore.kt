package com.riversoforion.eyre.service.impl

import com.riversoforion.eyre.model.Credential
import com.riversoforion.eyre.service.Messages
import com.riversoforion.eyre.service.SecretStore

class PlainTextSecretStore(private val out: Messages) : SecretStore {

    override suspend fun storeSecret(credential: Credential) {
        val path = "secret path"
        out.info("Stored secret ${credential.name} at $path")
    }

    override suspend fun retrieveSecret(name: String): Credential? {
        TODO("Not yet implemented")
    }
}
