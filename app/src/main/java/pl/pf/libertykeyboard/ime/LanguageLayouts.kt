package pl.pf.libertykeyboard.ime

internal data class LanguageLayout(
    val rows: List<List<String>>,
    val variants: Map<String, List<String>> = emptyMap(),
    val spaceLabel: String = "space",
    val hasCase: Boolean = true
)

internal object LanguageLayouts {
    private fun rows(vararg rows: String) = rows.map { row -> row.map(Char::toString) }

    private val polishVariants = mapOf(
        "a" to listOf("ą"), "c" to listOf("ć"), "e" to listOf("ę"),
        "l" to listOf("ł"), "n" to listOf("ń"), "o" to listOf("ó"),
        "s" to listOf("ś"), "z" to listOf("ź", "ż")
    )

    private val germanVariants = mapOf(
        "a" to listOf("ä"), "o" to listOf("ö"),
        "u" to listOf("ü"), "s" to listOf("ß")
    )

    private val frenchVariants = mapOf(
        "a" to listOf("à", "â", "æ"), "c" to listOf("ç"),
        "e" to listOf("é", "è", "ê", "ë"), "i" to listOf("î", "ï"),
        "o" to listOf("ô", "œ"), "u" to listOf("ù", "û", "ü"),
        "y" to listOf("ÿ")
    )

    private val spanishVariants = mapOf(
        "a" to listOf("á"), "e" to listOf("é"), "i" to listOf("í"),
        "n" to listOf("ñ"), "o" to listOf("ó"), "u" to listOf("ú", "ü")
    )

    private val portugueseVariants = mapOf(
        "a" to listOf("á", "à", "â", "ã"), "c" to listOf("ç"),
        "e" to listOf("é", "ê"), "i" to listOf("í"),
        "o" to listOf("ó", "ô", "õ"), "u" to listOf("ú")
    )

    private val italianVariants = mapOf(
        "a" to listOf("à"), "e" to listOf("è", "é"),
        "i" to listOf("ì"), "o" to listOf("ò"), "u" to listOf("ù")
    )

    private val dutchVariants = mapOf(
        "e" to listOf("é", "è", "ê", "ë"), "i" to listOf("ï"),
        "o" to listOf("ö"), "u" to listOf("ü")
    )

    private val czechVariants = mapOf(
        "a" to listOf("á"), "c" to listOf("č"), "d" to listOf("ď"),
        "e" to listOf("é", "ě"), "i" to listOf("í"), "n" to listOf("ň"),
        "o" to listOf("ó"), "r" to listOf("ř"), "s" to listOf("š"),
        "t" to listOf("ť"), "u" to listOf("ú", "ů"), "y" to listOf("ý"),
        "z" to listOf("ž")
    )

    private val slovakVariants = mapOf(
        "a" to listOf("á", "ä"), "c" to listOf("č"), "d" to listOf("ď"),
        "e" to listOf("é"), "i" to listOf("í"), "l" to listOf("ĺ", "ľ"),
        "n" to listOf("ň"), "o" to listOf("ó", "ô"), "r" to listOf("ŕ"),
        "s" to listOf("š"), "t" to listOf("ť"), "u" to listOf("ú"),
        "y" to listOf("ý"), "z" to listOf("ž")
    )

    private val turkishVariants = mapOf(
        "a" to listOf("â"), "i" to listOf("î"), "u" to listOf("û")
    )

    private fun latin(
        order: Array<String> = arrayOf("qwertyuiop", "asdfghjkl", "zxcvbnm"),
        label: String,
        variants: Map<String, List<String>> = emptyMap()
    ) = LanguageLayout(rows(*order), variants, label)

    fun forLanguage(language: String): LanguageLayout = when (language) {
        "de" -> latin(arrayOf("qwertzuiop", "asdfghjkl", "yxcvbnm"), "Deutsch", germanVariants)
        "fr" -> latin(arrayOf("azertyuiop", "qsdfghjklm", "wxcvbn"), "Français", frenchVariants)
        "es" -> latin(label = "Español", variants = spanishVariants)
        "pt" -> latin(label = "Português", variants = portugueseVariants)
        "it" -> latin(label = "Italiano", variants = italianVariants)
        "nl" -> latin(label = "Nederlands", variants = dutchVariants)
        "tr" -> latin(arrayOf("qwertyuıopğü", "asdfghjklşi", "zxcvbnmöç"), "Türkçe", turkishVariants)
        "cs" -> latin(arrayOf("qwertzuiop", "asdfghjkl", "yxcvbnm"), "Čeština", czechVariants)
        "sk" -> latin(arrayOf("qwertzuiop", "asdfghjkl", "yxcvbnm"), "Slovenčina", slovakVariants)
        "pl" -> latin(label = "Polski", variants = polishVariants)
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
