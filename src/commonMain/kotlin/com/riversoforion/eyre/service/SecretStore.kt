package com.riversoforion.eyre.service

import com.riversoforion.eyre.model.Credential

interface SecretStore {

    suspend fun storeSecret(credential: Credential)
    suspend fun retrieveSecret(name: String): Credential?
}
