package pl.pf.libertykeyboard.crypto

import java.text.Normalizer

/** Locates encrypted envelopes in text copied or exposed by another application. */
object CiphertextEnvelope {
    const val PREFIX = "LibertyKeyboard:"
    private val pattern = Regex("${Regex.escape(PREFIX)}[A-Za-z0-9_-]+")

    fun findAll(text: CharSequence): List<String> =
        pattern.findAll(Normalizer.normalize(text, Normalizer.Form.NFC))
            .map { it.value }
            .toList()

    fun findFirst(text: CharSequence): String? = findAll(text).firstOrNull()
}
