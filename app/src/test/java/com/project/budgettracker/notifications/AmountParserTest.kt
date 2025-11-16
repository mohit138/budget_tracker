package com.project.budgettracker.notifications

import org.junit.Assert.*
import org.junit.Test

class AmountParserTest {

    // -------------------------------------------------------
    // 1. IGNORE CHECKS
    // -------------------------------------------------------
    @Test
    fun test_ignoreKeywords() {
        assertTrue(AmountParser.containsIgnoredKeywords("Your OTP is 123456"))
        assertTrue(AmountParser.containsIgnoredKeywords("Get Rs 200 off now!"))
        assertFalse(AmountParser.containsIgnoredKeywords("Rs 200 debited"))
    }

    // -------------------------------------------------------
    // 2. CURRENCY-PREFIXED AMOUNTS
    // -------------------------------------------------------
    @Test
    fun test_extractCurrencyAmount() {
        assertEquals(125.0, AmountParser.extractCurrencyAmount("Spent Rs.125 today")!!, 0.001)
        assertEquals(825.0, AmountParser.extractCurrencyAmount("Sent Rs.825.00 to someone")!!, 0.001)
        assertEquals(980.0, AmountParser.extractCurrencyAmount("₹980 debited from account")!!, 0.001)
        assertEquals(550.0, AmountParser.extractCurrencyAmount("INR 550.00 processed")!!, 0.001)

        assertNull(AmountParser.extractCurrencyAmount("No money here"))
    }

    // -------------------------------------------------------
    // 3. ACTION-BASED AMOUNTS
    // -------------------------------------------------------
    @Test
    fun test_extractActionBasedAmount() {
        assertEquals(170.0, AmountParser.extractActionBasedAmount("debited by 170.0 today")!!, 0.001)
        assertEquals(200.5, AmountParser.extractActionBasedAmount("sent 200.5 to user")!!, 0.001)
        assertEquals(780.0, AmountParser.extractActionBasedAmount("paid 780 for bill")!!, 0.001)
        assertEquals(150.0, AmountParser.extractActionBasedAmount("credited 150.0")!!, 0.001)

        assertNull(AmountParser.extractActionBasedAmount("nothing here"))
    }

    // -------------------------------------------------------
    // 4. FALLBACK NUMERIC EXTRACTION
    // -------------------------------------------------------
    @Test
    fun test_extractFallbackAmount() {
        // Should extract the first valid amount, ignoring account numbers
        assertEquals(170.0,
            AmountParser.extractFallbackAmount(
                "A/C X1118 debited by 170.0 on 13May25 Ref 111114761111"
            )!!,
            0.001
        )

        // Should ignore years (2025)
        assertEquals(980.0,
            AmountParser.extractFallbackAmount(
                "UPI debit 980.00 on 27-07-2025"
            )!!,
            0.001
        )

        // No pure numbers
        assertNull(AmountParser.extractFallbackAmount("This has no amount"))
    }

    // -------------------------------------------------------
    // 5. FULL INTEGRATION TESTS (REAL MESSAGES)
    // -------------------------------------------------------
    @Test
    fun test_amountParsing_fullMessages() {

        val messages = listOf(
            // 1
            Triple(
                "Spent Rs.125 On HDFC Bank Card 1116 At SWIGGY64947 On 2025-11-09:11:25:50.Not You?",
                125.0,
                "HDFC card spend"
            ),

            // 2
            Triple(
                """
                Sent Rs.825.00
                From HDFC Bank A/C *1110
                To YOGI STHAAN SOULFUL LIVING
                """.trimIndent(),
                825.00,
                "HDFC UPI payment"
            ),

            // 3
            Triple(
                """
                Sent Rs.200.00
                From HDFC Bank A/C *1110
                To Mrs roy kapur
                """.trimIndent(),
                200.00,
                "HDFC UPI payment 2"
            ),

            // 4 SBI
            Triple(
                """
                Dear UPI user A/C X1118 debited by 170.0 on date 13May25 trf to SENDHOOR COFFEE Refno 111114761111.
                """.trimIndent(),
                170.0,
                "SBI debit"
            ),

            // 5 Federal Bank
            Triple(
                "Rs 980.00 debited via UPI on 27-07-2025 16:12:16 to VPA grofersindia.rzp¡hdfcbank.",
                980.00,
                "Federal Bank debit"
            ),

            // 6 Without Rs
            Triple(
                "A/C X1118 debited by 170.0 on 13May25 Ref 111114761111"
                , 170.0,
                "Fallback without Rs"
            ),

            // Ignore OTP
            Triple(
                "Your OTP for transaction of Rs.500 is 123456",
                null,
                "OTP — ignored"
            ),

            // Ignore offer
            Triple(
                "Special offer! Get ₹200 off on your next order!",
                null,
                "Offer — ignored"
            ),
        )

        for ((msg, expected, name) in messages) {
            val parsed = AmountParser.extractAmount(msg)

            if (expected == null) {
                assertNull("Failed: $name", parsed)
            } else {
                assertEquals("Failed: $name", expected, parsed!!, 0.001)
            }
        }
    }
}
