package co.techmagic.hr.presentation.ui.manager.quotes

import android.content.Context
import co.techmagic.hr.R
import java.util.*


class AndroidResQuotesManager(val context: Context) : QuotesManager {

    companion object {
        private const val QUOTE_SEPARATOR = '-'
    }

    private val quotes: List<Quote>
    private val random = Random()

    init {
        val quotesWithAuthors = context.resources.getStringArray(R.array.quotes)
        quotes = quotesWithAuthors.map {
            val separatorLastIndex = it.lastIndexOf(QUOTE_SEPARATOR)
            return@map Quote(it.substring(0, separatorLastIndex), it.substring(separatorLastIndex + 1))
        }
    }

    override fun getRandomQuote() = quotes[getRandomQuotIndex()]

    override fun getRandomFormattedQuote(): String {
        val quote = getRandomQuote()

        return StringBuilder()
                .append("\"")
                .append(quote.quoteText)
                .append("\"")
                .append("\n")
                .append(" – ")
                .append(quote.quoteAuthor)
                .toString()
    }

    private fun getRandomQuotIndex() = random.nextInt(quotes.size - 1)
}