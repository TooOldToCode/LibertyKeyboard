package pl.pf.libertykeyboard.accessibility

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import pl.pf.libertykeyboard.crypto.CryptoEngine
import pl.pf.libertykeyboard.crypto.CiphertextEnvelope
import pl.pf.libertykeyboard.crypto.DecryptedMessageHistory
import pl.pf.libertykeyboard.crypto.SecretSession

/**
 * User-enabled helper only. It reads visible encrypted text, performs no action
 * in the source app, and forwards decrypted messages to the in-keyboard history.
 */
class DecryptionAccessibilityService : AccessibilityService() {
    private val handler = Handler(Looper.getMainLooper())
    private val rescanWindow = Runnable { scanActiveWindow() }
    private val seenCiphertexts = LinkedHashSet<String>()

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!SecretSession.hasSecret()) return

        // Some communicators provide the message in event.text, while others only
        // expose it in the active window's accessibility-node tree.
        val eventText = buildList {
            addAll(event.text.map(CharSequence::toString))
            event.contentDescription?.let { add(it.toString()) }
            event.source?.let { addAll(textFromNodeTree(it)) }
        }
        decryptVisible(eventText)

        // Messenger frequently emits the event just before its accessibility tree
        // contains the new bubble. Scan now and once more after the UI settles.
        scanActiveWindow()
        handler.removeCallbacks(rescanWindow)
        handler.postDelayed(rescanWindow, WINDOW_RESCAN_DELAY_MS)
    }

    private fun scanActiveWindow() {
        if (!SecretSession.hasSecret()) return
        rootInActiveWindow?.let { decryptVisible(textFromNodeTree(it)) }
    }

    private fun decryptVisible(visibleText: List<String>): Boolean {
        var decryptedAny = false
        val candidates = buildList {
            visibleText.forEach { addAll(CiphertextEnvelope.findAll(it)) }
            // Some Compose-based communicators split one text bubble over several
            // accessibility nodes. This also catches a marker exposed separately.
            if (visibleText.size > 1) addAll(CiphertextEnvelope.findAll(visibleText.joinToString("")))
        }.distinct()
        for (candidate in candidates) {
            if (candidate in seenCiphertexts) continue
            var result: CharArray? = null
            SecretSession.withSecret { secret -> result = CryptoEngine.decrypt(candidate, secret) }
            result?.let {
                rememberCiphertext(candidate)
                DecryptedMessageHistory.add(it)
                it.fill('\u0000')
                decryptedAny = true
            }
        }
        return decryptedAny
    }

    private fun rememberCiphertext(ciphertext: String) {
        seenCiphertexts.add(ciphertext)
        while (seenCiphertexts.size > MAX_SEEN_CIPHERTEXTS) {
            val oldest = seenCiphertexts.iterator()
            oldest.next()
            oldest.remove()
        }
    }

    override fun onInterrupt() {
        handler.removeCallbacks(rescanWindow)
    }

    override fun onDestroy() {
        handler.removeCallbacks(rescanWindow)
        super.onDestroy()
    }

    private fun textFromNodeTree(root: AccessibilityNodeInfo): List<String> {
        val result = mutableListOf<String>()
        val pending = ArrayDeque<AccessibilityNodeInfo>()
        pending.add(root)
        while (pending.isNotEmpty()) {
            val node = pending.removeFirst()
            node.text?.let { result.add(it.toString()) }
            node.contentDescription?.let { result.add(it.toString()) }
            for (index in 0 until node.childCount) node.getChild(index)?.let(pending::addLast)
        }
        return result
    }

    companion object {
        private const val WINDOW_RESCAN_DELAY_MS = 400L
        private const val MAX_SEEN_CIPHERTEXTS = 100
    }
}
