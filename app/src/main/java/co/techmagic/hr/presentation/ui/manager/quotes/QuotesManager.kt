package co.techmagic.hr.presentation.ui.manager.quotes

interface QuotesManager {
    fun getQuote(): Pair<String, String>
}