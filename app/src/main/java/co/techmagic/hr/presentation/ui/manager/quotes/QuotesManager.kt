package co.techmagic.hr.presentation.ui.manager.quotes

interface QuotesManager {
    fun getRandomQuote(): Pair<String, String>

    fun getRandomFormatedQuote() : String
}