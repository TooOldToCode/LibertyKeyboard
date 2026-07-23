package pl.pf.libertykeyboard.ime

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LanguageLayoutsTest {
    @Test
    fun `latin layouts expose common national characters`() {
        val polish = LanguageLayouts.forLanguage("pl")
        val german = LanguageLayouts.forLanguage("de")

        assertTrue("ą" in polish.variants.getValue("a"))
        assertTrue("ä" in german.variants.getValue("a"))
        assertEquals("qwertzuiop", german.rows.first().joinToString(""))
    }

    @Test
    fun `non latin layouts use their native scripts`() {
        assertTrue(LanguageLayouts.forLanguage("ru").rows.flatten().contains("я"))
        assertTrue(LanguageLayouts.forLanguage("el").rows.flatten().contains("ω"))
        assertTrue(LanguageLayouts.forLanguage("ar").rows.flatten().contains("ع"))
        assertFalse(LanguageLayouts.forLanguage("ar").hasCase)
    }

    @Test
    fun `unknown language safely falls back to english qwerty`() {
        val layout = LanguageLayouts.forLanguage("xx")
        assertEquals("qwertyuiop", layout.rows.first().joinToString(""))
        assertEquals("English", layout.spaceLabel)
    }
}
