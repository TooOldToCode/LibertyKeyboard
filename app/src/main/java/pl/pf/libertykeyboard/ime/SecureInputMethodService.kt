package pl.pf.libertykeyboard.ime

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.StateListDrawable
import android.inputmethodservice.InputMethodService
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodSubtype
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import pl.pf.libertykeyboard.R
import pl.pf.libertykeyboard.crypto.CryptoEngine
import pl.pf.libertykeyboard.crypto.CiphertextEnvelope
import pl.pf.libertykeyboard.crypto.SecretSession
import pl.pf.libertykeyboard.crypto.DecryptedMessageHistory
import java.util.Locale

/** A deliberately small IME: encryption occurs only after the user confirms text with ↵. */
class SecureInputMethodService : InputMethodService() {
    private enum class KeyboardLayer { LETTERS, SYMBOLS, MORE_SYMBOLS }

    private lateinit var root: LinearLayout
    private lateinit var keyboard: LinearLayout
    private lateinit var status: Button
    private lateinit var passwordPanel: LinearLayout
    private lateinit var passwordInput: EditText
    private lateinit var historyPanel: LinearLayout
    private lateinit var historyTitle: TextView
    private lateinit var historyBody: TextView
    private lateinit var historyUp: Button
    private lateinit var historyDown: Button
    private var historyIndex = -1
    private val historyListener: () -> Unit = {
        if (::historyPanel.isInitialized) historyPanel.post {
            historyIndex = DecryptedMessageHistory.size() - 1
            refreshHistoryPanel()
        }
    }
    private var enteringPassword = false
    private var passwordBuffer = CharArray(0)
    private var layer = KeyboardLayer.LETTERS
    private var shifted = false

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        DecryptedMessageHistory.addListener(historyListener)
        historyIndex = DecryptedMessageHistory.size() - 1
        refreshHistoryPanel()
    }

    override fun onFinishInput() {
        DecryptedMessageHistory.removeListener(historyListener)
        if (::historyBody.isInitialized) historyBody.text = ""
        super.onFinishInput()
    }

    override fun onCurrentInputMethodSubtypeChanged(newSubtype: InputMethodSubtype?) {
        super.onCurrentInputMethodSubtypeChanged(newSubtype)
        if (::keyboard.isInitialized && layer == KeyboardLayer.LETTERS) renderKeyboard()
    }

    override fun onCreateInputView(): View {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            isMotionEventSplittingEnabled = true
            setBackgroundColor(KEYBOARD_BACKGROUND)
            setPadding(dp(3), dp(4), dp(3), dp(5))
        }
        val tools = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        tools.addView(
            imageTool(R.drawable.ic_password_settings, "Ustaw hasło sesji") { openPasswordPanel() },
            LinearLayout.LayoutParams(0, dp(48), 1f)
        )
        status = tool("OFF") { toggleEncryption() }.also {
            styleToolButton(it)
            it.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_encryption_off, 0, 0, 0)
            it.compoundDrawablePadding = dp(4)
            tools.addView(it, LinearLayout.LayoutParams(0, dp(48), 1f))
        }
        tools.addView(
            iconTextTool(R.drawable.ic_manual_decrypt, "ODSZYFRUJ", "Odszyfruj zaznaczenie lub schowek") { decryptSelectionOrClipboard() },
            LinearLayout.LayoutParams(0, dp(48), 1f)
        )
        tools.addView(
            iconTextTool(R.drawable.ic_password_clear, "USUŃ", "Wyczyść hasło sesji") { clearSession() },
            LinearLayout.LayoutParams(0, dp(48), 1f)
        )
        root.addView(tools)
        createHistoryPanel()
        root.addView(historyPanel)
        passwordPanel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            visibility = View.GONE
            setPadding(dp(8), dp(8), dp(8), dp(4))
        }
        passwordInput = EditText(this).apply {
            hint = "Hasło sesji"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            setSingleLine(true)
            isFocusable = false
            isFocusableInTouchMode = false
            isCursorVisible = false
            contentDescription = "Hasło sesji"
        }
        passwordPanel.addView(passwordInput)
        val passwordActions = LinearLayout(this)
        passwordActions.addView(tool("AKTYWUJ") { activatePassword() }, LinearLayout.LayoutParams(0, dp(48), 1f))
        passwordActions.addView(tool("WYCZYŚĆ") { clearSession() }, LinearLayout.LayoutParams(0, dp(48), 1f))
        passwordPanel.addView(passwordActions)
        root.addView(passwordPanel)
        keyboard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            isMotionEventSplittingEnabled = true
        }
        root.addView(keyboard)
        renderKeyboard()
        refreshState()
        return root
    }

    private fun renderKeyboard() {
        keyboard.removeAllViews()
        when (layer) {
            KeyboardLayer.LETTERS -> renderLetters()
            KeyboardLayer.SYMBOLS -> renderSymbols(more = false)
            KeyboardLayer.MORE_SYMBOLS -> renderSymbols(more = true)
        }
    }

    private fun renderLetters() {
        val layout = LanguageLayouts.forLanguage(currentLanguage())
        layout.rows.dropLast(1).forEach { addTextRow(it, layout) }
        val last = row().apply {
            if (layout.hasCase) addWeightedKey(if (shifted) "⇧" else "↑", 1.35f, special = true) {
                shifted = !shifted; renderKeyboard()
            }
            layout.rows.last().forEach { value -> addLetterKey(value, layout) }
            addWeightedKey("⌫", 1.35f, special = true, repeat = true) { backspace() }
        }
        keyboard.addView(last)
        addBottomRow("?123", layout.spaceLabel) { layer = KeyboardLayer.SYMBOLS; shifted = false; renderKeyboard() }
    }

    private fun renderSymbols(more: Boolean) {
        val rows = if (more) listOf("~`|•√π÷×¶", "£¢€¥^°={}\\", "_%©®™✓[]<>")
        else listOf("1234567890", "@#€_&-+()", "*/\"':;!?")
        rows.forEach(::addCharacterRow)
        addBottomRow(if (more) "?123" else "=\\<", LanguageLayouts.forLanguage(currentLanguage()).spaceLabel) {
            layer = if (more) KeyboardLayer.SYMBOLS else KeyboardLayer.MORE_SYMBOLS
            renderKeyboard()
        }
    }

    private fun addCharacterRow(chars: String) {
        val row = row()
        chars.forEach { char -> row.addWeightedKey(char.toString(), 1f) { commit(char.toString()) } }
        keyboard.addView(row)
    }

    private fun addTextRow(values: List<String>, layout: LanguageLayout) {
        val row = row()
        values.forEach { row.addLetterKey(it, layout) }
        keyboard.addView(row)
    }

    private fun LinearLayout.addLetterKey(value: String, layout: LanguageLayout) {
        val displayed = displayLetter(value, layout.hasCase)
        val variants = layout.variants[value].orEmpty().map { displayLetter(it, layout.hasCase) }
        addWeightedKey(displayed, 1f, variants = variants) { commit(displayed); consumeShift() }
    }

    private fun addBottomRow(modeLabel: String, spaceLabel: String, switchSymbols: () -> Unit) {
        val bottom = row()
        bottom.addWeightedKey(modeLabel, 1.25f, special = true, action = switchSymbols)
        if (layer != KeyboardLayer.LETTERS) bottom.addWeightedKey("ABC", 1.05f, special = true) {
            layer = KeyboardLayer.LETTERS; renderKeyboard()
        }
        bottom.addWeightedKey(",", .8f) { commit(",") }
        bottom.addWeightedKey("🌐", .7f, special = true) {
            if (!switchToNextInputMethod(false)) {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()
            }
        }
        bottom.addWeightedKey(spaceLabel, 2.7f) { commit(" ") }
        bottom.addWeightedKey(".", .8f) { commit(".") }
        if (layer != KeyboardLayer.LETTERS) bottom.addWeightedKey("⌫", 1.05f, special = true, repeat = true) { backspace() }
        bottom.addWeightedKey("↵", 1.25f, special = true) { confirmText() }
        keyboard.addView(bottom)
    }

    private fun row() = LinearLayout(this).apply {
        gravity = Gravity.CENTER
        isMotionEventSplittingEnabled = true
    }

    private fun LinearLayout.addWeightedKey(
        label: String,
        weight: Float,
        special: Boolean = false,
        repeat: Boolean = false,
        variants: List<String> = emptyList(),
        action: () -> Unit
    ) {
        addView(key(label, special, repeat, variants, action), LinearLayout.LayoutParams(0, dp(50), weight))
    }

    private fun currentLanguage(): String {
        val subtype = (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .currentInputMethodSubtype
        return subtype?.languageTag
            ?.takeIf(String::isNotBlank)
            ?.let { Locale.forLanguageTag(it).language }
            ?: Locale.getDefault().language
    }

    private fun displayLetter(value: String, hasCase: Boolean) =
        if (shifted && hasCase) value.uppercase(Locale.forLanguageTag(currentLanguage())) else value

    private fun consumeShift() {
        if (shifted) { shifted = false; renderKeyboard() }
    }

    private fun tool(label: String, action: () -> Unit) = Button(this).apply {
        text = label
        isFocusable = false
        isFocusableInTouchMode = false
        setOnClickListener {
            hapticTap(this)
            action()
        }
    }

    private fun imageTool(iconRes: Int, description: String, action: () -> Unit) = ImageButton(this).apply {
        setImageResource(iconRes)
        contentDescription = description
        scaleType = android.widget.ImageView.ScaleType.FIT_CENTER
        setPadding(dp(5), dp(5), dp(5), dp(5))
        styleToolButton(this)
        isFocusable = false
        isFocusableInTouchMode = false
        setOnClickListener {
            hapticTap(this)
            action()
        }
    }

    private fun iconTextTool(iconRes: Int, label: String, description: String, action: () -> Unit) = Button(this).apply {
        text = label
        contentDescription = description
        isAllCaps = false
        textSize = 8.5f
        gravity = Gravity.CENTER
        setTextColor(Color.rgb(38, 50, 56))
        setCompoundDrawablesWithIntrinsicBounds(0, iconRes, 0, 0)
        compoundDrawablePadding = 0
        setPadding(dp(1), dp(1), dp(1), dp(1))
        styleToolButton(this)
        isFocusable = false
        isFocusableInTouchMode = false
        setOnClickListener {
            hapticTap(this)
            action()
        }
    }

    private fun styleToolButton(view: View) {
        view.background = StateListDrawable().apply {
            addState(
                intArrayOf(android.R.attr.state_pressed),
                toolButtonShape(Color.rgb(205, 211, 216))
            )
            addState(
                intArrayOf(android.R.attr.state_enabled),
                toolButtonShape(Color.WHITE)
            )
            addState(intArrayOf(), toolButtonShape(Color.rgb(225, 228, 230)))
        }
        view.elevation = dp(1).toFloat()
    }

    private fun toolButtonShape(color: Int) = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = dp(6).toFloat()
        setColor(color)
        setStroke(dp(1), Color.rgb(190, 197, 202))
    }

    private fun hapticTap(view: View) {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }

    private fun key(
        label: String,
        special: Boolean = false,
        repeat: Boolean = false,
        variants: List<String> = emptyList(),
        action: () -> Unit
    ) = Button(this).apply {
        text = label
        isAllCaps = false
        textSize = if (label.length > 3) 14f else 20f
        setTextColor(Color.rgb(35, 39, 42))
        typeface = Typeface.create("sans-serif", Typeface.NORMAL)
        minWidth = 0
        minimumWidth = 0
        minHeight = 0
        minimumHeight = 0
        setPadding(0, 0, 0, 0)
        val normalColor = if (special) SPECIAL_KEY_BACKGROUND else KEY_BACKGROUND
        val pressedColor = if (special) SPECIAL_KEY_PRESSED_BACKGROUND else KEY_PRESSED_BACKGROUND
        background = StateListDrawable().apply {
            addState(
                intArrayOf(android.R.attr.state_pressed),
                InsetDrawable(keyShape(pressedColor), dp(2))
            )
            addState(intArrayOf(), InsetDrawable(keyShape(normalColor), dp(2)))
        }
        elevation = dp(1).toFloat()
        isFocusable = false
        isFocusableInTouchMode = false
        setOnClickListener { action() }
        var repeating = false
        var variantsPopup: PopupWindow? = null
        var popupX = 0
        var popupItemWidth = 1
        var selectedVariant = 0
        val variantViews = mutableListOf<TextView>()
        fun selectVariant(rawX: Float) {
            if (variantViews.isEmpty()) return
            selectedVariant = ((rawX - popupX) / popupItemWidth)
                .toInt().coerceIn(0, variantViews.lastIndex)
            variantViews.forEachIndexed { index, view ->
                view.setBackgroundColor(if (index == selectedVariant) KEY_PRESSED_BACKGROUND else KEY_BACKGROUND)
            }
        }
        val showVariants = Runnable {
            if (!isPressed || variants.isEmpty()) return@Runnable
            hapticTap(this)
            val strip = LinearLayout(context).apply {
                elevation = dp(6).toFloat()
                background = keyShape(SPECIAL_KEY_BACKGROUND)
            }
            variantViews.clear()
            variants.forEach { variant ->
                val item = TextView(context).apply {
                    text = variant
                    textSize = 22f
                    gravity = Gravity.CENTER
                    setTextColor(Color.rgb(35, 39, 42))
                }
                variantViews += item
            }
            popupItemWidth = minOf(dp(VARIANT_KEY_WIDTH_DP), resources.displayMetrics.widthPixels / variants.size)
            variantViews.forEach { strip.addView(it, LinearLayout.LayoutParams(popupItemWidth, dp(52))) }
            val width = popupItemWidth * variants.size
            val location = IntArray(2).also { getLocationOnScreen(it) }
            popupX = (location[0] + this.width / 2 - width / 2)
                .coerceIn(0, resources.displayMetrics.widthPixels - width)
            val xOffset = popupX - location[0]
            val yOffset = -(this.height + dp(52))
            variantsPopup = PopupWindow(strip, width, dp(52), false).also {
                it.isClippingEnabled = false
                it.inputMethodMode = PopupWindow.INPUT_METHOD_NOT_NEEDED
                it.elevation = dp(6).toFloat()
                it.showAsDropDown(this, xOffset, yOffset, Gravity.START)
            }
            selectVariant(location[0] + this.width / 2f)
        }
        val repeatAction = object : Runnable {
            override fun run() {
                if (!repeating) return
                hapticTap(this@apply)
                action()
                postDelayed(this, REPEAT_INTERVAL_MS)
            }
        }
        setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    hapticTap(this)
                    if (variants.isEmpty()) action() else postDelayed(showVariants, LONG_PRESS_DELAY_MS)
                    if (repeat) {
                        repeating = true
                        postDelayed(repeatAction, REPEAT_START_DELAY_MS)
                    }
                    isPressed = true
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (variantsPopup?.isShowing == true) selectVariant(event.rawX)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    repeating = false
                    removeCallbacks(repeatAction)
                    removeCallbacks(showVariants)
                    if (variantsPopup?.isShowing == true) {
                        variantsPopup?.dismiss()
                        commit(variants[selectedVariant])
                        consumeShift()
                    } else if (variants.isNotEmpty()) action()
                    variantsPopup = null
                    isPressed = false
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    repeating = false
                    removeCallbacks(repeatAction)
                    removeCallbacks(showVariants)
                    variantsPopup?.dismiss()
                    variantsPopup = null
                    isPressed = false
                    true
                }
                else -> true
            }
        }
    }

    private fun keyShape(color: Int) = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = dp(5).toFloat()
        setColor(color)
    }

    private fun openPasswordPanel() {
        enteringPassword = true
        passwordBuffer.fill('\u0000')
        passwordBuffer = CharArray(0)
        passwordInput.setText("")
        passwordPanel.visibility = View.VISIBLE
        refreshState()
    }

    private fun commit(value: String) {
        if (enteringPassword) {
            passwordBuffer += value.toCharArray()
            passwordInput.setText(String(passwordBuffer))
            passwordInput.setSelection(passwordInput.length())
        } else activeInputConnection()?.commitText(value, 1)
    }

    private fun backspace() {
        if (enteringPassword) {
            if (passwordBuffer.isNotEmpty()) {
                passwordBuffer[passwordBuffer.lastIndex] = '\u0000'
                passwordBuffer = passwordBuffer.copyOf(passwordBuffer.size - 1)
                passwordInput.setText(String(passwordBuffer))
                passwordInput.setSelection(passwordInput.length())
            }
        } else activeInputConnection()?.let { connection ->
            val selectedText = connection.getSelectedText(0)
            if (selectedText.isNullOrEmpty()) {
                connection.deleteSurroundingText(1, 0)
            } else {
                connection.commitText("", 1)
            }
        }
    }

    private fun toggleEncryption() {
        if (SecretSession.isEnabled()) SecretSession.disable()
        else if (!SecretSession.enable()) {
            openPasswordPanel()
            toast("Ustaw hasło sesji, aby włączyć szyfrowanie")
        }
        refreshState()
    }

    private fun confirmText() {
        if (enteringPassword) {
            activatePassword()
            return
        }
        val connection = activeInputConnection() ?: return
        if (SecretSession.isEnabled()) {
            if (encryptTextBeforeCursor(connection)) submitEncryptedMessage(connection)
        }
        else connection.commitText("\n", 1)
    }

    private fun encryptTextBeforeCursor(connection: InputConnection): Boolean {
        val plain = connection.getTextBeforeCursor(MAX_MESSAGE_CHARS, 0) ?: return false
        if (plain.isEmpty()) return false
        val chars = plain.toString().toCharArray()
        try {
            var ciphertext: String? = null
            val active = SecretSession.withSecret { secret -> ciphertext = CryptoEngine.encrypt(chars, secret) }
            if (!active || ciphertext == null) { toast("Brak hasła sesji"); return false }
            connection.beginBatchEdit()
            connection.deleteSurroundingText(chars.size, 0)
            connection.commitText(ciphertext, 1)
            connection.endBatchEdit()
            return true
        } finally {
            chars.fill('\u0000')
        }
    }

    private fun submitEncryptedMessage(connection: InputConnection) {
        connection.finishComposingText()
        // Some host apps report an editor action as handled without applying its
        // expected effect. Emit a real Enter key press after replacing the plaintext
        // so any active editor receives the same events as from a physical keyboard.
        connection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
        connection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
    }

    private fun decryptSelectionOrClipboard() {
        val selected = activeInputConnection()?.getSelectedText(0)
        val clipboardText = try {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = clipboard.primaryClip
            if (clipboard.hasPrimaryClip() && clip != null && clip.itemCount > 0) {
                clip.getItemAt(0).coerceToText(this)
            } else null
        } catch (_: SecurityException) {
            null
        }
        val candidates = buildList {
            if (!selected.isNullOrEmpty()) addAll(CiphertextEnvelope.findAll(selected))
            if (!clipboardText.isNullOrEmpty()) addAll(CiphertextEnvelope.findAll(clipboardText))
        }.distinct()
        if (candidates.isEmpty()) {
            toast("Nie znaleziono wiadomości LibertyKeyboard w zaznaczeniu ani schowku")
            return
        }
        if (!SecretSession.hasSecret()) {
            toast("Najpierw ustaw hasło sesji")
            return
        }
        for (candidate in candidates) {
            var result: CharArray? = null
            SecretSession.withSecret { secret -> result = CryptoEngine.decrypt(candidate, secret) }
            result?.let {
                DecryptedMessageHistory.add(it)
                it.fill('\u0000')
                return
            }
        }
        toast("Nie udało się odszyfrować — sprawdź hasło sesji")
    }

    private fun createHistoryPanel() {
        historyPanel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            visibility = View.GONE
            setBackgroundColor(Color.rgb(38, 50, 56))
            setPadding(dp(10), dp(6), dp(10), dp(6))
        }
        historyTitle = TextView(this).apply {
            setTextColor(Color.WHITE)
            textSize = 14f
        }
        historyPanel.addView(historyTitle)
        historyBody = TextView(this).apply {
            setTextColor(Color.WHITE)
            textSize = 16f
            setPadding(dp(4), dp(4), dp(4), dp(4))
        }
        historyPanel.addView(ScrollView(this).apply { addView(historyBody) }, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(82)
        ))
        val navigation = LinearLayout(this).apply { gravity = Gravity.CENTER }
        historyUp = tool("▲") {
            if (historyIndex > 0) { historyIndex--; refreshHistoryPanel() }
        }
        historyDown = tool("▼") {
            if (historyIndex + 1 < DecryptedMessageHistory.size()) { historyIndex++; refreshHistoryPanel() }
        }
        val remove = tool("×") {
            if (historyIndex >= 0) DecryptedMessageHistory.removeAt(historyIndex)
        }.apply {
            contentDescription = "Usuń bieżącą odszyfrowaną wiadomość; przytrzymaj, aby wyczyścić historię"
            setOnLongClickListener {
                hapticTap(this)
                DecryptedMessageHistory.clear()
                toast("Wyczyszczono historię odszyfrowań")
                true
            }
        }
        navigation.addView(historyUp, LinearLayout.LayoutParams(0, dp(42), 1f))
        navigation.addView(historyDown, LinearLayout.LayoutParams(0, dp(42), 1f))
        navigation.addView(remove, LinearLayout.LayoutParams(0, dp(42), 1f))
        historyPanel.addView(navigation)
    }

    private fun refreshHistoryPanel() {
        if (!::historyPanel.isInitialized) return
        val size = DecryptedMessageHistory.size()
        if (size == 0) {
            historyIndex = -1
            historyBody.text = ""
            historyPanel.visibility = View.GONE
            return
        }
        historyIndex = historyIndex.coerceIn(0, size - 1)
        val message = DecryptedMessageHistory.get(historyIndex) ?: return
        historyTitle.text = "Odszyfrowana wiadomość    ${historyIndex + 1} / $size"
        historyBody.text = String(message)
        message.fill('\u0000')
        historyUp.isEnabled = historyIndex > 0
        historyDown.isEnabled = historyIndex < size - 1
        historyPanel.visibility = View.VISIBLE
        historyPanel.requestLayout()
        root.requestLayout()
    }

    private fun clearSession() {
        SecretSession.clear()
        DecryptedMessageHistory.clear()
        passwordBuffer.fill('\u0000')
        passwordBuffer = CharArray(0)
        enteringPassword = false
        if (::passwordInput.isInitialized) passwordInput.setText("")
        if (::passwordPanel.isInitialized) passwordPanel.visibility = View.GONE
        refreshState()
    }

    private fun activatePassword() {
        if (passwordBuffer.isEmpty()) { toast("Hasło nie może być puste"); return }
        SecretSession.activate(passwordBuffer)
        SecretSession.enable()
        passwordBuffer.fill('\u0000')
        passwordBuffer = CharArray(0)
        passwordInput.setText("")
        enteringPassword = false
        passwordPanel.visibility = View.GONE
        toast("Hasło sesji aktywne")
        refreshState()
    }

    private fun refreshState() {
        if (!::status.isInitialized) return
        status.text = if (SecretSession.isEnabled()) "ON" else "OFF"
        status.setCompoundDrawablesWithIntrinsicBounds(
            if (SecretSession.isEnabled()) R.drawable.ic_encryption_on else R.drawable.ic_encryption_off,
            0, 0, 0
        )
        status.setTextColor(if (SecretSession.isEnabled()) Color.rgb(0, 110, 60) else Color.DKGRAY)
    }
    /**
     * InputConnection belongs to the currently focused editor and must not be cached.
     * Applications often replace their text field after sending a message; retaining
     * the old connection makes key presses appear to work while their text is dropped.
     */
    private fun activeInputConnection() = currentInputConnection
    private fun toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
    companion object {
        private const val MAX_MESSAGE_CHARS = 8_000
        private const val REPEAT_START_DELAY_MS = 350L
        private const val REPEAT_INTERVAL_MS = 55L
        private const val LONG_PRESS_DELAY_MS = 350L
        private const val VARIANT_KEY_WIDTH_DP = 44
        private val KEYBOARD_BACKGROUND = Color.rgb(233, 237, 240)
        private val KEY_BACKGROUND = Color.rgb(255, 255, 255)
        private val KEY_PRESSED_BACKGROUND = Color.rgb(185, 193, 199)
        private val SPECIAL_KEY_BACKGROUND = Color.rgb(205, 211, 216)
        private val SPECIAL_KEY_PRESSED_BACKGROUND = Color.rgb(160, 170, 178)
    }
}
