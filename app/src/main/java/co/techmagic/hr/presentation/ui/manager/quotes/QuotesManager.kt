package co.techmagic.hr.presentation.ui.manager.quotes

interface QuotesManager {
    fun getRandomQuote(): Quote

    fun getRandomFormattedQuote() : String
}