package co.techmagic.hr.presentation.ui.manager.quotes

import android.content.Context
import co.techmagic.hr.R
import java.util.*


class QuotesManagerImpl(val context: Context) : QuotesManager {

    private val quotes: List<Pair<String, String>>// first - quot, second - author
    private val random = Random()

    init {
        val quotesWithAuthors = context.resources.getStringArray(R.array.quotes)
        quotes = quotesWithAuthors.map {
            val quotAndAuthor = it.split("-")
            return@map quotAndAuthor[0].trim() to quotAndAuthor[1].trim()
        }
    }

    override fun getRandomQuote() = quotes[getRandomQuotIndex()]

    override fun getRandomFormatedQuote(): String {
        val quote = getRandomQuote()

        return StringBuilder()
                .append("\"")
                .append(quote.first)
                .append("\"")
                .append("\n")
                .append(" – ")
                .append(quote.second)
                .toString()
    }

    private fun getRandomQuotIndex() = random.nextInt(quotes.size - 1)
}