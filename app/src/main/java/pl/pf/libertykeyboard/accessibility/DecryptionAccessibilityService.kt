package pl.pf.libertykeyboard.accessibility

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityWindowInfo
import pl.pf.libertykeyboard.crypto.CiphertextCandidateDetector
import pl.pf.libertykeyboard.crypto.CiphertextRegistry
import pl.pf.libertykeyboard.crypto.CryptoEngine
import pl.pf.libertykeyboard.crypto.DecryptedMessageHistory
import pl.pf.libertykeyboard.crypto.SecretSession
import java.util.concurrent.Executors

/**
 * User-enabled helper only. It reads visible encrypted text, performs no action
 * in the source app, and forwards decrypted messages to the in-keyboard history.
 */
class DecryptionAccessibilityService : AccessibilityService() {
    private val handler = Handler(Looper.getMainLooper())
    private val rescanWindows = Runnable { scanRelevantWindows() }
    private val decryptionExecutor = Executors.newSingleThreadExecutor()

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!SecretSession.hasSecret()) return

        if (event.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            decryptTexts(textFromNotification(event))
        }

        // Some applications expose useful text directly on the event even when
        // event.source is null or contains only a small changed subtree.
        decryptTexts(buildList {
            event.text.mapTo(this, CharSequence::toString)
            event.contentDescription?.let { add(it.toString()) }
        })
        event.source?.let { decryptTexts(textFromNodeTree(it)) }

        // Scan immediately so a steady stream of events cannot postpone all work.
        // Follow-up scans catch bubbles rendered after the original event.
        scanRelevantWindows()
        handler.removeCallbacks(rescanWindows)
        WINDOW_RESCAN_DELAYS_MS.forEach { handler.postDelayed(rescanWindows, it) }
    }

    private fun scanRelevantWindows() {
        if (!SecretSession.hasSecret()) return
        val roots = LinkedHashMap<Int, AccessibilityNodeInfo>()

        windows.asSequence()
            .filter { it.type == AccessibilityWindowInfo.TYPE_APPLICATION }
            .forEach { window ->
                window.root?.let { root ->
                    if (root.packageName?.toString() != packageName) roots[window.id] = root
                }
            }

        // Some devices expose no interactive-window list despite the requested
        // service flag. Keep the active root as a compatibility fallback.
        if (roots.isEmpty()) {
            rootInActiveWindow?.let { root ->
                if (root.packageName?.toString() != packageName) roots[Int.MIN_VALUE] = root
            }
        }
        roots.values.forEach { decryptTexts(textFromNodeTree(it)) }
    }

    private fun decryptTexts(texts: List<String>) {
        val candidates = CiphertextCandidateDetector.find(texts)
        if (candidates.isEmpty()) return
        decryptionExecutor.execute {
            val successful = mutableListOf<String>()
            candidates.forEach { candidate ->
                // Once a shorter reconstructed candidate authenticates, longer
                // candidates with that prefix merely contain adjacent UI text.
                if (successful.any(candidate::startsWith)) return@forEach
                if (!CiphertextRegistry.claim(candidate)) return@forEach
                if (decryptAndRemember(candidate)) successful.add(candidate)
            }
        }
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

    override fun onInterrupt() {
        handler.removeCallbacks(rescanWindows)
    }

    override fun onDestroy() {
        handler.removeCallbacks(rescanWindows)
        decryptionExecutor.shutdownNow()
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
        private val WINDOW_RESCAN_DELAYS_MS = longArrayOf(300L, 1_000L)
        private const val MAX_SCANNED_NODES = 1_500
        private const val MAX_SCANNED_CHARACTERS = 100_000
        private val NOTIFICATION_TEXT_KEYS = arrayOf(
            Notification.EXTRA_TEXT,
            Notification.EXTRA_BIG_TEXT,
            Notification.EXTRA_TITLE,
            Notification.EXTRA_SUB_TEXT
        )
    }
}
