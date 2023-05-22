package me.thekusch.composeview

import androidx.compose.ui.text.input.TextFieldValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import java.text.DecimalFormatSymbols
import java.util.Locale

class InputSanitizerTest {

    private val decimalFormatSymbols = DecimalFormatSymbols(Locale.US)

    @Test
    fun doesNothingForEmptyText() {
        val input = TextFieldValue("")

        assertSame(input, sanitizeInput("USD", decimalFormatSymbols, input))
    }

    @Test
    fun removesPartialCurrencySymbolMatchWhenNothingElseInInput() {
        assertEquals(
            TextFieldValue(""),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("U"))
        )

        assertEquals(
            TextFieldValue(""),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("US"))
        )
    }

    @Test
    fun removesPartialCurrencySymbolMatchAtBeginningOfInput() {
        assertEquals(
            TextFieldValue("1"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("U1"))
        )

        assertEquals(
            TextFieldValue("1"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("US1"))
        )
    }

    @Test
    fun removesPartialCurrencySymbolMatchAtBeginningOfInputFollowedByDecimalSeparator() {
        assertEquals(
            TextFieldValue("."),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("U."))
        )

        assertEquals(
            TextFieldValue("."),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("US."))
        )
    }

    @Test
    fun removesPartialCurrencySymbolMatchAtBeginningOfInputFollowedByGroupingSeparator() {
        assertEquals(
            TextFieldValue(","),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("U,"))
        )

        assertEquals(
            TextFieldValue(","),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("US,"))
        )
    }

    @Test
    fun removesPartialCurrencySymbolMatchAtMiddleOfInput() {
        assertEquals(
            TextFieldValue("126"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("12U6"))
        )

        assertEquals(
            TextFieldValue("126"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("12US6"))
        )
    }

    @Test
    fun removesPartialCurrencySymbolMatchAtMiddleOfInputFollowedByDecimalSeparator() {
        assertEquals(
            TextFieldValue("12."),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("12U."))
        )

        assertEquals(
            TextFieldValue("12."),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("12US."))
        )
    }

    @Test
    fun removesPartialCurrencySymbolMatchAtMiddleOfInputFollowedByGroupingSeparator() {
        assertEquals(
            TextFieldValue("12,"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("12U,"))
        )

        assertEquals(
            TextFieldValue("12,"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("12US,"))
        )
    }

    @Test
    fun removesPartialCurrencySymbolMatchAtMiddleOfInputPrecedingDecimalSeparator() {
        assertEquals(
            TextFieldValue("13."),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("13.U"))
        )

        assertEquals(
            TextFieldValue("13."),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("13.US"))
        )
    }

    @Test
    fun removesPartialCurrencySymbolMatchAtMiddleOfInputPrecedingGroupingSeparator() {
        assertEquals(
            TextFieldValue("75,"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("75,U"))
        )

        assertEquals(
            TextFieldValue("75,"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("75,US"))
        )
    }

    @Test
    fun removesPartialCurrencySymbolMatchAtEndOfInput() {
        assertEquals(
            TextFieldValue("12"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("12U"))
        )

        assertEquals(
            TextFieldValue("12"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("12US"))
        )
    }

    @Test
    fun removesPartialCurrencySymbolMatchAtEndOfInputPrecedingDecimalSeparator() {
        assertEquals(
            TextFieldValue("1."),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("1.U"))
        )

        assertEquals(
            TextFieldValue("1."),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("1.US"))
        )
    }

    @Test
    fun removesPartialCurrencySymbolMatchAtEndOfInputPrecedingGroupingSeparator() {
        assertEquals(
            TextFieldValue("0,"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("0,U"))
        )

        assertEquals(
            TextFieldValue("0,"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("0,US"))
        )
    }

    @Test
    fun doesNotRemoveCurrencySymbolsAtBeginning() {
        assertEquals(
            TextFieldValue("$99"),
            sanitizeInput("$", decimalFormatSymbols, TextFieldValue("$99"))
        )

        assertEquals(
            TextFieldValue("USD99"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("USD99"))
        )
    }

    @Test
    fun doesNotRemoveCurrencySymbolsAtTheBeginningFollowedByDecimalSeparator() {
        assertEquals(
            TextFieldValue("$.1"),
            sanitizeInput("$", decimalFormatSymbols, TextFieldValue("$.1"))
        )

        assertEquals(
            TextFieldValue("USD.1"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("USD.1"))
        )
    }

    @Test
    fun doesNotRemoveCurrencySymbolsAtTheBeginningFollowedByGroupingSeparator() {
        assertEquals(
            TextFieldValue("$,1"),
            sanitizeInput("$", decimalFormatSymbols, TextFieldValue("$,1"))
        )

        assertEquals(
            TextFieldValue("USD,1"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("USD,1"))
        )
    }

    @Test
    fun doesNotRemoveCurrencySymbolsInTheMiddle() {
        assertEquals(
            TextFieldValue("12$1"),
            sanitizeInput("$", decimalFormatSymbols, TextFieldValue("12$1"))
        )

        assertEquals(
            TextFieldValue("12USD1"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("12USD1"))
        )
    }

    @Test
    fun doesNotRemoveCurrencySymbolsInTheEnd() {
        assertEquals(
            TextFieldValue("889$"),
            sanitizeInput("$", decimalFormatSymbols, TextFieldValue("889$"))
        )

        assertEquals(
            TextFieldValue("52USD"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("52USD"))
        )
    }

    @Test
    fun doesNotRemoveCurrencySymbolsInTheEndPrecedingDecimalSeparator() {
        assertEquals(
            TextFieldValue("11.$"),
            sanitizeInput("$", decimalFormatSymbols, TextFieldValue("11.$"))
        )

        assertEquals(
            TextFieldValue("11.USD"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("11.USD"))
        )
    }

    @Test
    fun doesNotRemoveCurrencySymbolsInTheEndPrecedingGroupingSeparator() {
        assertEquals(
            TextFieldValue("889,$"),
            sanitizeInput("$", decimalFormatSymbols, TextFieldValue("889,$"))
        )

        assertEquals(
            TextFieldValue("52,USD"),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("52,USD"))
        )
    }

    @Test
    fun removesUnwantedInput() {
        assertEquals(
            TextFieldValue(""),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("a"))
        )

        assertEquals(
            TextFieldValue(""),
            sanitizeInput("USD", decimalFormatSymbols, TextFieldValue("abc"))
        )

        assertEquals(
            TextFieldValue(""),
            sanitizeInput(
                "USD",
                decimalFormatSymbols,
                TextFieldValue("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
            )
        )

        assertEquals(
            TextFieldValue(""),
            sanitizeInput(
                "USD",
                decimalFormatSymbols,
                TextFieldValue("[]{}:\"\'?/<>-=_+!@#$%^&*()`\\|")
            )
        )
    }
}
