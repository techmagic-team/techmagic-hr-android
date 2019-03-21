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
            val quoteAndAuthor = it.split("-")
            return@map Quote(quoteAndAuthor[QUOTE_TEXT_POSITION], quoteAndAuthor[QUOTE_AUTHOR_POSITION])
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