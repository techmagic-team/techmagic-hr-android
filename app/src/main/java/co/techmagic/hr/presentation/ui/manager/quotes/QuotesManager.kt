package co.techmagic.hr.presentation.ui.manager.quotes

interface QuotesManager {
    fun getRandomQuote(): Quot

    fun getRandomFormatedQuote() : String
}