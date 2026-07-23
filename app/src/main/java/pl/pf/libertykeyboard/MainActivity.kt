package pl.pf.libertykeyboard

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
        setContentView(layout)
    }

    private fun showAccessibilityDisclosure() {
        AlertDialog.Builder(this)
            .setTitle("Dostęp do treści na ekranie")
            .setMessage(
                "Opcjonalny podgląd odszyfrowania używa Usługi ułatwień dostępu, aby odczytać " +
                    "widoczny tekst na ekranie i rozpoznać wiadomości zaczynające się od LibertyKeyboard:. " +
                    "Tekst jest przetwarzany wyłącznie lokalnie, nie jest zapisywany ani wysyłany. " +
                    "Usługa nie klika, nie wysyła wiadomości i nie zmienia aplikacji komunikatora."
            )
            .setNegativeButton("Anuluj", null)
            .setPositiveButton("Rozumiem — przejdź do ustawień") { _, _ ->
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
            .show()
    }
}
