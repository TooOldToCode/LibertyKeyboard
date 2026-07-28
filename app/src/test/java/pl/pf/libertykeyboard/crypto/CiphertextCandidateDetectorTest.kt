package pl.pf.libertykeyboard.crypto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CiphertextCandidateDetectorTest {
    private val password = "test-password".toCharArray()

    @Test
    fun returnsCompleteEnvelopeFirstAndOnlyOnceAcrossDuplicateFields() {
        val cipher = CryptoEngine.encrypt("message".toCharArray(), password)

        val result = CiphertextCandidateDetector.find(listOf(cipher, cipher, "Sender", cipher))

        assertEquals(cipher, result.first())
        assertEquals(1, result.count { it == cipher })
    }

    @Test
    fun reconstructsEnvelopeSplitAcrossAdjacentFragments() {
        val cipher = CryptoEngine.encrypt("split message".toCharArray(), password)
        val first = cipher.substring(0, 31)
        val second = cipher.substring(31, 57)
        val third = cipher.substring(57)

        val result = CiphertextCandidateDetector.find(listOf(first, second, third, "12:30"))

        assertTrue(cipher in result)
    }

    @Test
    fun doesNotJoinAcrossAnUnrelatedLabel() {
        val cipher = CryptoEngine.encrypt("split message".toCharArray(), password)
        val first = cipher.substring(0, 31)
        val second = cipher.substring(31)

        val result = CiphertextCandidateDetector.find(listOf(first, "Sender name", second))

        assertFalse(cipher in result)
    }

    @Test
    fun rejectsImplausiblyShortMarker() {
        assertTrue(CiphertextCandidateDetector.find(listOf("LibertyKeyboard:abc")).isEmpty())
    }
}
