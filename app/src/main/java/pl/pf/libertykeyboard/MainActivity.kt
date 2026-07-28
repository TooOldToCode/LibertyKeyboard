package pl.pf.libertykeyboard

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

/** Entry point deliberately contains onboarding only; it never handles a secret. */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val padding = (24 * resources.displayMetrics.density).toInt()
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(padding, padding, padding, padding)
        }
        layout.addView(TextView(this).apply {
            text = "Secure Keyboard\n\nKlawiatura szyfruje lokalnie. Hasło wpisujesz wyłącznie z paska klawiatury; aplikacja go nie zapisuje."
            textSize = 18f
        })
        layout.addView(Button(this).apply {
            text = "Włącz klawiaturę"
            setOnClickListener { startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)) }
        })
        layout.addView(Button(this).apply {
            text = "Włącz podgląd odszyfrowanych wiadomości"
            setOnClickListener { showAccessibilityDisclosure() }
        })
        layout.addView(Button(this).apply {
            text = "Polityka prywatności"
            setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL)))
            }
        })
        layout.addView(Button(this).apply {
            text = "Instrukcja użytkownika"
            setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(USER_GUIDE_URL)))
            }
        })
        layout.addView(Button(this).apply {
            text = "O aplikacji"
            setOnClickListener { showAbout() }
        })
        setContentView(layout)
    }

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
