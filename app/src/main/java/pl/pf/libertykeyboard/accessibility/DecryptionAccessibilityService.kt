package pl.pf.libertykeyboard.accessibility

import android.app.Notification
import android.accessibilityservice.AccessibilityService
import android.os.Build
import android.os.Bundle
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
    private val rescanActiveWindow = Runnable { scanActiveWindow() }
    private val processedWindowOccurrences = LinkedHashMap<String, Int>()
    private val seenNotificationCiphertexts = LinkedHashSet<String>()

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!SecretSession.hasSecret()) return

        decryptNotification(textFromNotification(event))

        // The event often arrives just before the new bubble is present. Debounce one
        // short series of bounded active-window scans. Messenger animations can take
        // longer than one frame, while scanning every window would stall the IME.
        handler.removeCallbacks(rescanActiveWindow)
        WINDOW_RESCAN_DELAYS_MS.forEach { delay ->
            handler.postDelayed(rescanActiveWindow, delay)
        }
    }

    private fun scanActiveWindow() {
        if (!SecretSession.hasSecret()) return
        rootInActiveWindow?.let { decryptWindow(textFromNodeTree(it)) }
    }

    private fun decryptWindow(visibleText: List<String>) {
        val occurrenceCounts = ciphertextsIn(visibleText).groupingBy { it }.eachCount()
        occurrenceCounts.forEach { (candidate, count) ->
            val alreadyProcessed = processedWindowOccurrences[candidate] ?: 0
            var processed = alreadyProcessed
            repeat((count - alreadyProcessed).coerceAtLeast(0)) {
                if (decryptAndRemember(candidate)) processed++
            }
            if (processed > alreadyProcessed) rememberWindowOccurrences(candidate, processed)
        }
    }

    private fun decryptNotification(notificationText: List<String>) {
        ciphertextsIn(notificationText).distinct().forEach { candidate ->
            if (!seenNotificationCiphertexts.add(candidate)) return@forEach
            if (decryptAndRemember(candidate)) {
                rememberWindowOccurrences(
                    candidate,
                    (processedWindowOccurrences[candidate] ?: 0) + 1
                )
            }
        }
        trimToSize(seenNotificationCiphertexts)
    }

    private fun ciphertextsIn(visibleText: List<String>): List<String> {
        val individual = visibleText.flatMap(CiphertextEnvelope::findAll)
        // Some Compose-based communicators split one text bubble over several
        // accessibility nodes. This also catches a marker exposed separately.
        if (visibleText.size <= 1) return individual
        val joinedOnly = CiphertextEnvelope.findAll(visibleText.joinToString(""))
            .filterNot(individual::contains)
        return individual + joinedOnly
    }

    private fun decryptAndRemember(candidate: String): Boolean {
        var result: CharArray? = null
        SecretSession.withSecret { secret -> result = CryptoEngine.decrypt(candidate, secret) }
        result?.let {
            DecryptedMessageHistory.add(it)
            it.fill('\u0000')
            return true
        }
        return false
    }

    private fun rememberWindowOccurrences(ciphertext: String, count: Int) {
        processedWindowOccurrences[ciphertext] = count
        while (processedWindowOccurrences.size > MAX_REMEMBERED_CIPHERTEXTS) {
            processedWindowOccurrences.remove(processedWindowOccurrences.keys.first())
        }
    }

    private fun trimToSize(values: LinkedHashSet<String>) {
        while (values.size > MAX_REMEMBERED_CIPHERTEXTS) {
            val oldest = values.iterator()
            oldest.next()
            oldest.remove()
        }
    }

    override fun onInterrupt() {
        handler.removeCallbacks(rescanActiveWindow)
    }

    override fun onDestroy() {
        handler.removeCallbacks(rescanActiveWindow)
        super.onDestroy()
    }

    private fun textFromNodeTree(root: AccessibilityNodeInfo): List<String> {
        val result = mutableListOf<String>()
        val pending = ArrayDeque<AccessibilityNodeInfo>()
        pending.add(root)
        var visitedNodes = 0
        var collectedCharacters = 0
        while (
            pending.isNotEmpty() &&
            visitedNodes < MAX_SCANNED_NODES &&
            collectedCharacters < MAX_SCANNED_CHARACTERS
        ) {
            val node = pending.removeFirst()
            visitedNodes++
            node.text?.toString()?.takeIf(String::isNotEmpty)?.let {
                result.add(it)
                collectedCharacters += it.length
            }
            node.contentDescription?.toString()?.takeIf(String::isNotEmpty)?.let {
                result.add(it)
                collectedCharacters += it.length
            }
            for (index in 0 until node.childCount) node.getChild(index)?.let(pending::addLast)
        }
        return result
    }

    private fun textFromNotification(event: AccessibilityEvent): List<String> {
        val notification = event.parcelableData as? Notification ?: return emptyList()
        val extras = notification.extras ?: return emptyList()
        return buildList {
            NOTIFICATION_TEXT_KEYS.forEach { key ->
                extras.getCharSequence(key)?.let { add(it.toString()) }
            }
            extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)
                ?.forEach { add(it.toString()) }
            notificationMessages(extras)
                ?.filterIsInstance<Bundle>()
                ?.forEach { message ->
                    message.getCharSequence("text")?.let { add(it.toString()) }
                }
        }
    }

    private fun notificationMessages(extras: Bundle): Array<out android.os.Parcelable>? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            extras.getParcelableArray(Notification.EXTRA_MESSAGES, android.os.Parcelable::class.java)
        } else {
            @Suppress("DEPRECATION")
            extras.getParcelableArray(Notification.EXTRA_MESSAGES)
        }

    companion object {
        private val WINDOW_RESCAN_DELAYS_MS = longArrayOf(250L, 750L, 1_500L)
        private const val MAX_SCANNED_NODES = 1_500
        private const val MAX_SCANNED_CHARACTERS = 100_000
        private val NOTIFICATION_TEXT_KEYS = arrayOf(
            Notification.EXTRA_TEXT,
            Notification.EXTRA_BIG_TEXT,
            Notification.EXTRA_TITLE,
            Notification.EXTRA_SUB_TEXT
        )
        private const val MAX_REMEMBERED_CIPHERTEXTS = 100
    }
}
