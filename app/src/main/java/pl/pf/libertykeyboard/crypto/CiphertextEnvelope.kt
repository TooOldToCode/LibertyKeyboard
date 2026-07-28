package pl.pf.libertykeyboard.crypto

import java.text.Normalizer

/** Locates encrypted envelopes in text copied or exposed by another application. */
object CiphertextEnvelope {
    const val PREFIX = "LibertyKeyboard:"
    // 16-byte salt + 12-byte GCM nonce + 16-byte GCM tag, encoded without padding.
    private const val MIN_PAYLOAD_CHARS = 59
    // Encryption accepts at most 8,000 UTF-16 code units. This comfortably covers
    // their maximum UTF-8 representation and prevents unbounded decode/Argon2 work.
    private const val MAX_PAYLOAD_CHARS = 32_100
    const val MAX_ENVELOPE_CHARS = 32_116
    private val pattern = Regex(
        "${Regex.escape(PREFIX)}[A-Za-z0-9_-]{$MIN_PAYLOAD_CHARS,$MAX_PAYLOAD_CHARS}" +
            "(?![A-Za-z0-9_-])"
    )

    fun findAll(text: CharSequence): List<String> =
        pattern.findAll(Normalizer.normalize(text, Normalizer.Form.NFC))
            .map { it.value }
            .toList()

    fun findFirst(text: CharSequence): String? = findAll(text).firstOrNull()

    fun isEnvelopeCharacter(character: Char): Boolean =
        character in 'A'..'Z' || character in 'a'..'z' ||
            character in '0'..'9' || character == '_' || character == '-'

    fun takeIfPlausible(value: String): String? {
        if (!value.startsWith(PREFIX) || value.length > MAX_ENVELOPE_CHARS) return null
        val payload = value.substring(PREFIX.length)
        return value.takeIf {
            payload.length in MIN_PAYLOAD_CHARS..MAX_PAYLOAD_CHARS &&
                payload.all { character ->
                    character in 'A'..'Z' || character in 'a'..'z' ||
                        character in '0'..'9' || character == '_' || character == '-'
                }
        }
    }
}
