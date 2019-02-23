package co.techmagic.hr.presentation.ui.manager.quotes

import android.content.Context
import co.techmagic.hr.R
import java.util.*


class AndroidResQuotesManager(val context: Context) : QuotesManager {

    companion object {
        private const val QUOTE_TEXT_POSITION = 0
        private const val QUOTE_AUTHOR_POSITION = 1
    }

    private val quotes: List<Quote>
    private val random = Random()

    init {
        val quotesWithAuthors = context.resources.getStringArray(R.array.quotes)
        quotes = quotesWithAuthors.map {
            val quotAndAuthor = it.split("-")
            return@map Quote(quotAndAuthor[QUOTE_TEXT_POSITION], quotesWithAuthors[QUOTE_AUTHOR_POSITION])
        }
    }

    override fun getRandomQuote() = quotes[getRandomQuotIndex()]

    override fun getRandomFormatedQuote(): String {
        val quote = getRandomQuote()

        return StringBuilder()
                .append("\"")
                .append(quote.quotText)
                .append("\"")
                .append("\n")
                .append(" – ")
                .append(quote.quotAuthor)
                .toString()
    }

    private fun getRandomQuotIndex() = random.nextInt(quotes.size - 1)
}