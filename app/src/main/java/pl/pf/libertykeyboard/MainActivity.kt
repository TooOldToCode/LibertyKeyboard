package pl.pf.libertykeyboard

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/** Entry point deliberately contains onboarding only; it never handles a secret. */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(2, 18, 62)
        window.navigationBarColor = Color.rgb(2, 18, 62)

        val root = FrameLayout(this)
        root.addView(ImageView(this).apply {
            setImageResource(R.drawable.main_app_art)
            scaleType = ImageView.ScaleType.CENTER_CROP
            contentDescription = null
        }, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ))
        root.addView(View(this).apply {
            setBackgroundColor(Color.argb(132, 1, 15, 53))
        }, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ))

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(24), dp(10), dp(24), dp(28))
        }

        content.addView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(20), dp(10), dp(20), dp(10))
            background = roundedBackground(Color.argb(118, 2, 25, 79), dp(16).toFloat())

            addView(TextView(this@MainActivity).apply {
                text = "LIBERTY KEYBOARD"
                textSize = 27f
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER
                setTextColor(Color.rgb(255, 193, 7))
                letterSpacing = 0.08f
            })
        }, fullWidthParams())

        content.addView(View(this), LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dp(300)
        ))

        content.addView(actionButton("Włącz klawiaturę", primary = true) {
            startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
        }, fullWidthParams(bottomMargin = dp(10)))
        content.addView(actionButton("Włącz podgląd odszyfrowanych wiadomości") {
            showAccessibilityDisclosure()
        }, fullWidthParams(bottomMargin = dp(10)))
        content.addView(actionButton("Polityka prywatności") {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL)))
        }, fullWidthParams(bottomMargin = dp(10)))
        content.addView(actionButton("Instrukcja użytkownika") {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(USER_GUIDE_URL)))
        }, fullWidthParams(bottomMargin = dp(10)))
        content.addView(actionButton("O aplikacji") {
            showAbout()
        }, fullWidthParams())

        root.addView(ScrollView(this).apply {
            isFillViewport = true
            clipToPadding = false
            addView(content, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ))
        }, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ))
        setContentView(root)
    }

    private fun actionButton(
        label: String,
        primary: Boolean = false,
        action: () -> Unit
    ) = Button(this).apply {
        text = label
        textSize = 15f
        isAllCaps = false
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(if (primary) Color.rgb(2, 25, 79) else Color.WHITE)
        background = roundedBackground(
            if (primary) Color.rgb(255, 193, 7) else Color.argb(235, 3, 43, 116),
            dp(14).toFloat(),
            if (primary) Color.rgb(255, 220, 92) else Color.argb(210, 255, 193, 7)
        )
        minHeight = dp(54)
        setPadding(dp(16), dp(8), dp(16), dp(8))
        elevation = dp(4).toFloat()
        setOnClickListener { action() }
    }

    private fun roundedBackground(
        color: Int,
        radius: Float,
        strokeColor: Int? = null
    ) = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(color)
        cornerRadius = radius
        strokeColor?.let { setStroke(dp(1), it) }
    }

    private fun fullWidthParams(bottomMargin: Int = 0) =
        LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            this.bottomMargin = bottomMargin
        }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()

    private fun showAccessibilityDisclosure() {
        AlertDialog.Builder(this)
            .setTitle("Dostęp do treści na ekranie")
            .setMessage(
                "Aby automatycznie wykrywać zaszyfrowane wiadomości, Liberty Keyboard używa " +
                    "Usługi ułatwień dostępu. Po jej włączeniu aplikacja może odczytywać tekst " +
                    "i opisy elementów widoczne w aktywnym oknie oraz treść powiadomień z innych aplikacji.\n\n" +
                    "Dane są używane wyłącznie do lokalnego wyszukiwania wiadomości zaczynających się " +
                    "od LibertyKeyboard: i ich odszyfrowania. Nie są przesyłane poza urządzenie ani " +
                    "udostępniane osobom trzecim. Surowa treść ekranu i powiadomień nie jest zapisywana. " +
                    "Odszyfrowane wiadomości są tymczasowo przechowywane tylko w pamięci aplikacji, " +
                    "dopóki ich nie usuniesz, nie wyczyścisz hasła lub proces aplikacji nie zostanie zakończony.\n\n" +
                    "Usługa nie klika elementów, nie wysyła wiadomości i nie zmienia zawartości innych aplikacji. " +
                    "Funkcja jest opcjonalna i można ją wyłączyć w ustawieniach dostępności."
            )
            .setNegativeButton("Nie zgadzam się", null)
            .setPositiveButton("Zgadzam się — przejdź do ustawień") { _, _ ->
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
            .show()
    }

    private fun showAbout() {
        AlertDialog.Builder(this)
            .setTitle("Liberty Keyboard ${getString(R.string.app_version_name)}")
            .setMessage(
                "Autor\n" +
                    "Przemysław Frątczak (TooOldToCode)\n\n" +
                    "Aplikacja powstaje metodą vibe coding przy wsparciu OpenAI Codex.\n\n" +
                    "Licencja\nGNU General Public License v3.0"
            )
            .setNeutralButton("Kod źródłowy") { _, _ ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOURCE_CODE_URL)))
            }
            .setPositiveButton("Zamknij", null)
            .show()
    }

    companion object {
        private const val PRIVACY_POLICY_URL =
            "https://toooldtocode.github.io/LibertyKeyboard/privacy-policy.html"
        private const val USER_GUIDE_URL =
            "https://toooldtocode.github.io/LibertyKeyboard/user-guide.html"
        private const val SOURCE_CODE_URL =
            "https://github.com/TooOldToCode/LibertyKeyboard"
    }
}
