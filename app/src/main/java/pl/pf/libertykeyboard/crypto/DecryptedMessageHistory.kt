package pl.pf.libertykeyboard.crypto

/** Process-local, bounded history. Cleartext is never persisted and is wiped on removal. */
object DecryptedMessageHistory {
    private const val MAX_MESSAGES = 20
    private val messages = mutableListOf<CharArray>()
    private val listeners = mutableSetOf<() -> Unit>()

    fun add(message: CharArray) {
        val callbacks: List<() -> Unit>
        synchronized(this) {
            messages.add(message.copyOf())
            while (messages.size > MAX_MESSAGES) messages.removeAt(0).fill('\u0000')
            callbacks = listeners.toList()
        }
        callbacks.forEach { it() }
    }

    @Synchronized fun size(): Int = messages.size

    @Synchronized fun get(index: Int): CharArray? = messages.getOrNull(index)?.copyOf()

    fun removeAt(index: Int) {
        val callbacks: List<() -> Unit>
        synchronized(this) {
            if (index !in messages.indices) return
            messages.removeAt(index).fill('\u0000')
            callbacks = listeners.toList()
        }
        callbacks.forEach { it() }
    }

    fun clear() {
        val callbacks: List<() -> Unit>
        synchronized(this) {
            messages.forEach { it.fill('\u0000') }
            messages.clear()
            callbacks = listeners.toList()
        }
        callbacks.forEach { it() }
    }

    @Synchronized fun addListener(listener: () -> Unit) { listeners.add(listener) }
    @Synchronized fun removeListener(listener: () -> Unit) { listeners.remove(listener) }
}
