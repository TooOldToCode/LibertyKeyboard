package pl.pf.libertykeyboard.crypto

import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CiphertextRegistryTest {
    @After
    fun clear() = CiphertextRegistry.clear()

    @Test
    fun outgoingCiphertextIsNotAttempted() {
        CiphertextRegistry.rememberOutgoing("outgoing")

        assertFalse(CiphertextRegistry.claim("outgoing"))
        assertTrue(CiphertextRegistry.claim("incoming"))
    }

    @Test
    fun ciphertextCanBeClaimedOnlyOncePerSession() {
        assertTrue(CiphertextRegistry.claim("candidate"))
        assertFalse(CiphertextRegistry.claim("candidate"))
    }

    @Test
    fun clearingSecretAlsoClearsDetectionState() {
        CiphertextRegistry.claim("candidate")
        SecretSession.activate("secret".toCharArray())

        assertTrue(CiphertextRegistry.claim("candidate"))
    }
}
