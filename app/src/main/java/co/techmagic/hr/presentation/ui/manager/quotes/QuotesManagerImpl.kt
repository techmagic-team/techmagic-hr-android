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
            return@map quotAndAuthor[0] to quotAndAuthor[1]
        }
    }

    override fun getQuote() = quotes[getRandomQuotIndex()]

    private fun getRandomQuotIndex() = random.nextInt(quotes.size - 1)
}