object AmountParser {

    private val ignoreKeywords = listOf("otp", "offer", " off ")

    // -----------------------------
    // 1. Ignore spam/OTP
    // -----------------------------
    fun containsIgnoredKeywords(text: String): Boolean {
        val lower = text.lowercase()
        return ignoreKeywords.any { lower.contains(it) }
    }

    // -----------------------------
    // 2. Rs / INR / ₹ formats
    // -----------------------------
    fun extractCurrencyAmount(text: String): Double? {
        val regex = Regex(
            "(?:rs\\.?\\s*|inr\\s*|₹)\\s*([0-9]+(?:\\.[0-9]+)?)",
            RegexOption.IGNORE_CASE
        )
        return regex.find(text)?.groupValues?.get(1)?.toDouble()
    }

    // -----------------------------
    // 3. debited / sent / trf / paid
    // -----------------------------
    fun extractActionBasedAmount(text: String): Double? {
        val regex = Regex(
            "(debited(?: by)?|debit|sent|trf|paid|credited)\\s*([0-9]+(?:\\.[0-9]+)?)",
            RegexOption.IGNORE_CASE
        )
        val match = regex.find(text)
        return match?.groupValues?.get(2)?.toDouble()
    }

    // -----------------------------
    // 4. Fallback safe numeric parsing
    // -----------------------------
    fun extractFallbackAmount(text: String): Double? {
        if (text.isBlank()) return null

        // 1) Try action-based quick match (makes fallback able to handle "debited by 170.0" cases)
        val actionNumRegex = Regex(
            "(?:debited(?: by)?|debit|trf|sent|paid)\\s*([0-9]+(?:\\.[0-9]+)?)",
            RegexOption.IGNORE_CASE
        )
        actionNumRegex.find(text)?.let { m ->
            return m.groupValues[1].toDoubleOrNull()
        }

        // 2) Otherwise, gather plain numeric candidates
        val numberRegex = Regex("\\b([0-9]+(?:\\.[0-9]+)?)\\b")
        val candidates = numberRegex.findAll(text).map { it.value }.toList()
        if (candidates.isEmpty()) return null

        // Helper to detect if candidate is part of an account token like X1118 or *1110
        fun isPartOfAccount(candidate: String, fullText: String): Boolean {
            // Look for patterns like X1118, x1118, *1110 nearby
            val accPatterns = listOf(Regex("(?i)[x*]\\s*${Regex.escape(candidate)}"), Regex("(?i)${Regex.escape(candidate)}[A-Za-z]"))
            return accPatterns.any { it.containsMatchIn(fullText) }
        }

        val filtered = candidates.filter { num ->
            // Skip years (like 2025)
            if (num.length == 4 && (num.startsWith("20") || num.toIntOrNull() in 1900..2100)) return@filter false

            // If this candidate is embedded in or directly adjacent to account-like token (X1118, *1110), skip it
            if (isPartOfAccount(num, text)) return@filter false

            true
        }

        // Return first filtered candidate as Double (if any)
        return filtered.firstOrNull()?.toDoubleOrNull()
    }


    // -----------------------------
    // 5. Orchestration
    // -----------------------------
    fun extractAmount(text: String?): Double? {
        if (text == null) return null
        if (containsIgnoredKeywords(text)) return null

        return extractCurrencyAmount(text)
            ?: extractActionBasedAmount(text)
            ?: extractFallbackAmount(text)
    }
}
