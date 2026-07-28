package pl.pf.libertykeyboard.crypto

import java.text.Normalizer

/**
 * Extracts complete envelopes and cautiously reconstructs envelopes split over
 * adjacent accessibility text fragments. It never concatenates an entire
 * window, because unrelated labels and timestamps can otherwise become part of
 * the URL-safe Base64 payload.
 */
object CiphertextCandidateDetector {
    private const val MAX_JOINED_FRAGMENTS = 8
    private val payloadFragment = Regex("^[A-Za-z0-9_-]+$")

    fun find(texts: List<String>): List<String> {
        if (texts.isEmpty()) return emptyList()
        val normalized = texts.map { Normalizer.normalize(it, Normalizer.Form.NFC) }
        val candidates = LinkedHashSet<String>()

        normalized.forEach { candidates.addAll(CiphertextEnvelope.findAll(it)) }

        normalized.indices.forEach { start ->
            val text = normalized[start]
            var prefixAt = text.indexOf(CiphertextEnvelope.PREFIX)
            while (prefixAt >= 0) {
                val payloadStart = prefixAt + CiphertextEnvelope.PREFIX.length
                val payload = text.substring(payloadStart)
                    .takeWhile(CiphertextEnvelope::isEnvelopeCharacter)
                val initial = CiphertextEnvelope.PREFIX + payload
                if (payload.isNotEmpty()) {
                    var joined = initial
                    CiphertextEnvelope.takeIfPlausible(joined)?.let(candidates::add)

                    var index = start + 1
                    var joinedFragments = 1
                    while (index < normalized.size && joinedFragments < MAX_JOINED_FRAGMENTS) {
                        val fragment = normalized[index].trim()
                        if (!payloadFragment.matches(fragment)) break
                        if (joined.length + fragment.length > CiphertextEnvelope.MAX_ENVELOPE_CHARS) break
                        joined += fragment
                        CiphertextEnvelope.takeIfPlausible(joined)?.let(candidates::add)
                        index++
                        joinedFragments++
                    }
                }
                prefixAt = text.indexOf(CiphertextEnvelope.PREFIX, prefixAt + 1)
            }
        }
        return candidates.toList()
    }
}
