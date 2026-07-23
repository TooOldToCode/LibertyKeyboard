package pl.pf.libertykeyboard.crypto

import java.util.concurrent.atomic.AtomicBoolean

/**
 * The only holder of the session secret. It deliberately exposes no String form,
 * has no persistence mechanism, and is shared only inside this application process.
 */
object SecretSession {
    private var secret: CharArray? = null
    private val encryptionEnabled = AtomicBoolean(false)

    @Synchronized fun activate(value: CharArray) {
        clear()
        secret = value.copyOf()
    }

    @Synchronized fun withSecret(block: (CharArray) -> Unit): Boolean {
        val value = secret ?: return false
        block(value)
        return true
    }

    fun enable(): Boolean = synchronized(this) {
        if (secret == null) false else encryptionEnabled.compareAndSet(false, true)
    }

    fun disable() { encryptionEnabled.set(false) }
    fun isEnabled(): Boolean = encryptionEnabled.get()
    fun hasSecret(): Boolean = synchronized(this) { secret != null }

    @Synchronized fun clear() {
        encryptionEnabled.set(false)
        secret?.fill('\u0000')
        secret = null
    }
}
