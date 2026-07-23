package pl.pf.libertykeyboard.ime

internal data class LanguageLayout(
    val rows: List<List<String>>,
    val variants: Map<String, List<String>> = emptyMap(),
    val spaceLabel: String = "space",
    val hasCase: Boolean = true
)

internal object LanguageLayouts {
    private fun rows(vararg rows: String) = rows.map { row -> row.map(Char::toString) }

    private val latinVariants = mapOf(
        "a" to listOf("á", "à", "â", "ä", "æ", "ã", "å", "ā", "ă", "ą"),
        "c" to listOf("ç", "ć", "č", "ĉ", "ċ"),
        "d" to listOf("ď", "đ", "ð"), "e" to listOf("é", "è", "ê", "ë", "ę", "ė", "ē", "ě"),
        "g" to listOf("ğ", "ģ", "ĝ", "ġ"), "i" to listOf("í", "ì", "î", "ï", "ī", "į", "ı"),
        "l" to listOf("ł", "ľ", "ĺ"), "n" to listOf("ñ", "ń", "ň", "ņ"),
        "o" to listOf("ó", "ò", "ô", "ö", "œ", "ø", "õ", "ō", "ő"),
        "r" to listOf("ř", "ŕ", "ŗ"), "s" to listOf("ś", "š", "ş", "ș", "ß"),
        "t" to listOf("ť", "ţ", "ț", "þ"), "u" to listOf("ú", "ù", "û", "ü", "ū", "ů", "ű", "ų"),
        "y" to listOf("ý", "ÿ", "ŷ"), "z" to listOf("ź", "ż", "ž")
    )

    private fun latin(order: Array<String> = arrayOf("qwertyuiop", "asdfghjkl", "zxcvbnm"), label: String) =
        LanguageLayout(rows(*order), latinVariants, label)

    fun forLanguage(language: String): LanguageLayout = when (language) {
        "de" -> latin(arrayOf("qwertzuiop", "asdfghjkl", "yxcvbnm"), "Deutsch")
        "fr" -> latin(arrayOf("azertyuiop", "qsdfghjklm", "wxcvbn"), "Français")
        "es" -> latin(label = "Español")
        "pt" -> latin(label = "Português")
        "it" -> latin(label = "Italiano")
        "nl" -> latin(label = "Nederlands")
        "tr" -> latin(arrayOf("qwertyuıopğü", "asdfghjklşi", "zxcvbnmöç"), "Türkçe")
        "cs" -> latin(arrayOf("qwertzuiop", "asdfghjkl", "yxcvbnm"), "Čeština")
        "sk" -> latin(arrayOf("qwertzuiop", "asdfghjkl", "yxcvbnm"), "Slovenčina")
        "pl" -> latin(label = "Polski")
        "ru" -> LanguageLayout(rows("йцукенгшщзх", "фывапролджэ", "ячсмитьбю"), spaceLabel = "Русский")
        "uk" -> LanguageLayout(rows("йцукенгшщзхї", "фівапролджє", "ячсмитьбю"), spaceLabel = "Українська")
        "be" -> LanguageLayout(rows("йцукенгшўзх", "фывапролджэ", "ячсмітьбю"), spaceLabel = "Беларуская")
        "bg" -> LanguageLayout(rows("уеишщксдзц", "ьяаожгтнвмч", "юйъефхпрлб"), spaceLabel = "Български")
        "sr" -> LanguageLayout(rows("љњертзуиопш", "асдфгхјклчћ", "џжцвбнмчђ"), spaceLabel = "Српски")
        "el" -> LanguageLayout(rows("ςερτυθιοπ", "ασδφγηξκλ", "ζχψωβνμ"), spaceLabel = "Ελληνικά")
        "ar" -> LanguageLayout(rows("ضصثقفغعهخحج", "شسيبلاتنمكط", "ئءؤرلاىةوزظ"), spaceLabel = "العربية", hasCase = false)
        "fa" -> LanguageLayout(rows("ضصثقفغعهخحجچ", "شسیبلاتنمکگ", "ظطزرذدپوژ"), spaceLabel = "فارسی", hasCase = false)
        "he" -> LanguageLayout(rows("קראטוןםפשדג", "כעיחלךףזסבהנ", "מצתץ"), spaceLabel = "עברית", hasCase = false)
        else -> latin(label = "English")
    }
}
