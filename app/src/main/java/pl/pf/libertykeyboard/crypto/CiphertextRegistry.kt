package pl.pf.libertykeyboard.crypto

/**
 * Process-local detection state. Nothing is persisted: all entries are cleared
 * together with the session password.
 */
object CiphertextRegistry {
    private const val MAX_PROCESSED = 256
    private const val MAX_OUTGOING = 128
    private val attempted = LinkedHashSet<String>()
    private val outgoing = LinkedHashSet<String>()

    @Synchronized
    fun rememberOutgoing(ciphertext: String) {
        outgoing.add(ciphertext)
        trim(outgoing, MAX_OUTGOING)
    }

    @Synchronized
    fun claim(ciphertext: String): Boolean {
        if (ciphertext in outgoing || ciphertext in attempted) return false
        attempted.add(ciphertext)
        trim(attempted, MAX_PROCESSED)
        return true
    }

    @Synchronized
    fun clear() {
        attempted.clear()
        outgoing.clear()
    }

    private fun trim(values: LinkedHashSet<String>, maximum: Int) {
        while (values.size > maximum) {
            val oldest = values.iterator()
            oldest.next()
            oldest.remove()
        }
    }
}
