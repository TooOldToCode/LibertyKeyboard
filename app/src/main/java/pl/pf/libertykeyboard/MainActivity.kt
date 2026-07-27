package pl.pf.libertykeyboard

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.net.Uri
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

    companion object {
        private const val PRIVACY_POLICY_URL =
            "https://toooldtocode.github.io/LibertyKeyboard/privacy-policy"
    }
}
