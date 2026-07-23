package pl.pf.libertykeyboard.crypto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Test

class CryptoEngineTest {
    @After fun clearSession() = SecretSession.clear()

    @Test fun encryptDecryptRoundTrip() {
        val password = "wspolny-sekret".toCharArray()
        val cipher = CryptoEngine.encrypt("Spotkanie o 18:00".toCharArray(), password)
        assertTrue(cipher.startsWith("LibertyKeyboard:"))
        val output = CryptoEngine.decrypt(cipher, password)
        assertEquals("Spotkanie o 18:00", String(output!!))
        password.fill('\u0000'); output.fill('\u0000')
    }

    @Test fun wrongPasswordFails() {
        val cipher = CryptoEngine.encrypt("tajne".toCharArray(), "dobre".toCharArray())
        assertNull(CryptoEngine.decrypt(cipher, "zle".toCharArray()))
    }

    @Test fun decryptsEnvelopeSurroundedByMessengerText() {
        val password = "sekret".toCharArray()
        val cipher = CryptoEngine.encrypt("wiadomość".toCharArray(), password)
        val output = CryptoEngine.decrypt("Nadawca\n$cipher\n12:30", password)
        assertEquals("wiadomość", String(output!!))
        password.fill('\u0000'); output.fill('\u0000')
    }

    @Test fun sessionPasswordActivatesAndEncryptionModeToggles() {
        val password = "haslo-sesji".toCharArray()
        SecretSession.activate(password)
        password.fill('\u0000')

        assertTrue(SecretSession.hasSecret())
        assertFalse(SecretSession.isEnabled())
        assertTrue(SecretSession.enable())
        assertTrue(SecretSession.isEnabled())

        SecretSession.disable()
        assertFalse(SecretSession.isEnabled())
        assertTrue(SecretSession.hasSecret())
    }

    @Test fun clearingSessionRemovesPasswordAndTurnsEncryptionOff() {
        SecretSession.activate("haslo-sesji".toCharArray())
        SecretSession.enable()
        SecretSession.clear()

        assertFalse(SecretSession.hasSecret())
        assertFalse(SecretSession.isEnabled())
        assertFalse(SecretSession.enable())
    }
}
