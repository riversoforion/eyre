package com.riversoforion.eyre.service.impl

import com.riversoforion.eyre.model.Credential
import com.riversoforion.eyre.model.Secret
import com.riversoforion.eyre.service.Messages
import dev.mokkery.MockMode
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.core.spec.style.DescribeSpec
import kotlin.time.Clock

class PlainTextSecretStoreTests : DescribeSpec({

    val messages = mock<Messages>(MockMode.autoUnit)
    val secretStore = PlainTextSecretStore(messages)

    describe("storeSecret") {

        it("should store the secret to the file system") {
            val credential = Credential("token",
                                        "my-secret-token",
                                        Clock.System.now(),
                                        Secret.CustomSecret("my-secret-token", mapOf()))

            secretStore.storeSecret(credential)

            verify { messages.info("Stored secret my-secret-token at secret path") }
        }

        it("should notify user if storage fails") {
            // Not yet implemented
        }
    }

    describe("retrieveSecret") {

        it("should retrieve the secret from the file system") {
            // Not yet implemented
        }

        it("should return null if the secret does not exist") {
            // Not yet implemented
        }
    }
})
