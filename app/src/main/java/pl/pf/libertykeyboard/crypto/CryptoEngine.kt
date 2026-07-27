package pl.pf.libertykeyboard.crypto

import com.google.crypto.tink.subtle.AesGcmJce
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.Base64

/**
 * Stateless crypto core: Argon2id derives a per-message AES-256 key and Tink's
 * AES-GCM primitive creates/authenticates the nonce. The clear prefix is the sole
 * protocol marker; salt and AEAD output are an opaque, URL-safe encoded payload.
 */
object CryptoEngine {
    private const val SALT_SIZE = 16
    private const val KEY_SIZE = 32
    private const val MIN_CIPHERTEXT_SIZE = 28 // GCM nonce + tag
    private val random = SecureRandom()

    fun encrypt(plain: CharArray, password: CharArray): String {
        var plainBytes: ByteArray? = null
        var key: ByteArray? = null
        val salt = ByteArray(SALT_SIZE)
        try {
            random.nextBytes(salt)
            plainBytes = StandardCharsets.UTF_8.encode(java.nio.CharBuffer.wrap(plain)).let { buffer ->
                ByteArray(buffer.remaining()).also { buffer.get(it) }
            }
            key = deriveKey(password, salt)
            val encrypted = AesGcmJce(key).encrypt(plainBytes, null)
            val packet = ByteBuffer.allocate(SALT_SIZE + encrypted.size).put(salt).put(encrypted).array()
            encrypted.fill(0)
            return CiphertextEnvelope.PREFIX + Base64.getUrlEncoder().withoutPadding().encodeToString(packet)
        } finally {
            plainBytes?.fill(0)
            key?.fill(0)
            salt.fill(0)
        }
    }

    fun decrypt(message: CharSequence, password: CharArray): CharArray? {
        val envelope = CiphertextEnvelope.findFirst(message) ?: return null
        var packet: ByteArray? = null
        var key: ByteArray? = null
        var decrypted: ByteArray? = null
        try {
            packet = Base64.getUrlDecoder().decode(envelope.substring(CiphertextEnvelope.PREFIX.length))
            if (packet.size < SALT_SIZE + MIN_CIPHERTEXT_SIZE) return null
            val salt = packet.copyOfRange(0, SALT_SIZE)
            try { key = deriveKey(password, salt) } finally { salt.fill(0) }
            val decryptedBytes = AesGcmJce(key).decrypt(packet.copyOfRange(SALT_SIZE, packet.size), null)
            decrypted = decryptedBytes
            return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(decryptedBytes)).toString().toCharArray()
        } catch (_: Exception) {
            return null // wrong secret or untrusted, malformed input
        } finally {
            packet?.fill(0)
            key?.fill(0)
            decrypted?.fill(0)
        }
    }

    private fun deriveKey(password: CharArray, salt: ByteArray): ByteArray {
        val passwordBytes = StandardCharsets.UTF_8.encode(java.nio.CharBuffer.wrap(password)).let { buffer ->
            ByteArray(buffer.remaining()).also { buffer.get(it) }
        }
        return try {
            val generator = Argon2BytesGenerator()
            generator.init(
                Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                    .withSalt(salt)
                    .withIterations(3)
                    .withMemoryAsKB(64 * 1024)
                    .withParallelism(1)
                    .build()
            )
            ByteArray(KEY_SIZE).also { generator.generateBytes(passwordBytes, it) }
        } finally {
            passwordBytes.fill(0)
        }
    }
}
