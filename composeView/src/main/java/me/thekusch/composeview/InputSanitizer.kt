package me.thekusch.composeview

import androidx.compose.ui.text.input.TextFieldValue
import java.text.DecimalFormatSymbols

/**
 * Sanitizes user input by removing unwanted characters.
 * The only allowed characters are
 * - any digit
 * - currency symbol
 * - decimal separator
 * - grouping separator
 */
internal fun sanitizeInput(
    currencySymbol: String,
    decimalFormatSymbols: DecimalFormatSymbols,
    input: TextFieldValue,
): TextFieldValue {

    // StringBuilder will remain null until we need to delete some characters
    var sb: StringBuilder? = null
    val inputText = input.text

    // Index of character either in input text or in StringBuilder if we have one
    var charIndex = 0

    // Because currencySymbol make take multiple characters we need to check multiple consecutive
    // characters to test if we are looking into a StringBuilder
    var indexIntoPossibleCurrencySymbolMatch = 0

    /**
     * Deletes partial currency symbol matches
     *
     * @param matchEndIndex index where partial match ends
     * @param matchLength length of partial match
     * @return new cursor just after last deleted partial match
     */
    fun deletePartialCurrencySymbolMatches(
        matchEndIndex: Int,
        matchLength: Int
    ): Int {
        var deletionIndex = matchEndIndex
        repeat(matchLength) {
            if (deletionIndex >= 0) {
                if (sb == null) {
                    sb = StringBuilder(inputText)
                }
                sb!!.deleteCharAt(deletionIndex)
                deletionIndex--
            }
        }

        return deletionIndex
    }

    fun onNotAllowed() {
        if (sb == null) {
            sb = StringBuilder(inputText)
        }

        // Delete current not allowed character
        if (charIndex >= 0) {
            sb!!.deleteCharAt(charIndex)
            charIndex--
        }

        // Also delete any partial currency symbol matches
        charIndex = deletePartialCurrencySymbolMatches(
            matchEndIndex = charIndex,
            matchLength = indexIntoPossibleCurrencySymbolMatch
        )

        if (charIndex < 0) {
            charIndex = 0
        }

        indexIntoPossibleCurrencySymbolMatch = 0
    }

    while (charIndex >= 0 && charIndex < (sb?.length ?: inputText.length)) {
        val result = testCharacter(
            sb?.get(charIndex) ?: inputText[charIndex],
            currencySymbol,
            decimalFormatSymbols,
            indexIntoPossibleCurrencySymbolMatch
        )

        when (result) {
            CharacterTestResult.NOT_ALLOWED -> onNotAllowed()

            CharacterTestResult.ALLOWED -> {
                // Delete partial currency symbol matches, if any
                if (indexIntoPossibleCurrencySymbolMatch != 0) {
                    deletePartialCurrencySymbolMatches(
                        matchEndIndex = charIndex - 1,
                        matchLength = indexIntoPossibleCurrencySymbolMatch
                    )

                    indexIntoPossibleCurrencySymbolMatch = 0
                }

                charIndex++
            }

            CharacterTestResult.ALLOWED_CURRENCY_SYMBOL -> {
                indexIntoPossibleCurrencySymbolMatch = 0
                charIndex++
            }

            CharacterTestResult.POSSIBLE_CURRENCY_SYMBOL_MATCH -> {
                // If possible currency symbol match was the last character then treat as
                // NOT_ALLOWED
                if (charIndex == (sb?.length ?: inputText.length) - 1) {
                    onNotAllowed()
                } else {
                    charIndex++
                    indexIntoPossibleCurrencySymbolMatch++
                }
            }
        }
    }

    // StringBuilder created means we removed some characters
    return if (sb != null) {
        input.copy(text = sb.toString())
    } else {
        input
    }
}

private fun testCharacter(
    c: Char,
    currencySymbol: String,
    decimalFormatSymbols: DecimalFormatSymbols,
    indexIntoPossibleCurrencySymbolMatch: Int
): CharacterTestResult {
    if (c.isDigit()
        || c == decimalFormatSymbols.decimalSeparator
        || c == decimalFormatSymbols.groupingSeparator
    ) {
        return CharacterTestResult.ALLOWED
    }

    // If we reached here it means either we are looking at the currency symbol or something that is
    // not allowed.
    if (indexIntoPossibleCurrencySymbolMatch < currencySymbol.length) {
        if (c == currencySymbol[indexIntoPossibleCurrencySymbolMatch]) {
            return if (indexIntoPossibleCurrencySymbolMatch == currencySymbol.length - 1) {
                // We just finished matching currency symbol
                CharacterTestResult.ALLOWED_CURRENCY_SYMBOL
            } else {
                // We might be in progress of matching a currency symbol
                CharacterTestResult.POSSIBLE_CURRENCY_SYMBOL_MATCH
            }
        }
    }

    return CharacterTestResult.NOT_ALLOWED
}

private enum class CharacterTestResult {
    NOT_ALLOWED,
    ALLOWED,
    ALLOWED_CURRENCY_SYMBOL,
    POSSIBLE_CURRENCY_SYMBOL_MATCH
}
