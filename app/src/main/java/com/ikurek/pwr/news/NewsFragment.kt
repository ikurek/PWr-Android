package com.ikurek.pwr.news

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ikurek.pwr.R
import com.ikurek.pwr.Utils
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.android.synthetic.main.fragment_news.view.*


class NewsFragment : Fragment() {

    private var hasData = false
    private var listOfNews: ArrayList<NewsModel> = arrayListOf()
    private lateinit var utils: Utils

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_news, container, false)
        utils = Utils(activity)
        setupRecyclerView(view)

        //Check if fragment was loaded in the past
        //If not, download data
        if (!hasData) {
            getNews(view)
            hasData = true
        }

        return view
    }

    /**
     * Configures view and onClick actions for recyclerView
     * Setups swipeToRefresh and fastScroller for it
     * @param view object of type [View] required to perform UI operations
     */
    private fun setupRecyclerView(view: View) {
        view.recycler_view_news_fragment.layoutManager = LinearLayoutManager(activity)
        view.recycler_view_news_fragment.adapter = NewsAdapter(listOfNews) {
            Utils(activity).openUrlInCustomTab(it.url)
        }
        view.swipe_refresh_news_fragment.setOnRefreshListener {
            getNews(view)
        }

        view.fast_scroller_news_fragment.setRecyclerView(view.recycler_view_news_fragment)
    }

    /**
     * Parses RSS feed
     * Multiple parser instances run on different threads
     * Counter defines, when all of them are finished
     * @param view object of type [View] required to perform UI operations
     */
    private fun getNews(view: View) {

        Log.i("getNews()", "Beginning data refresh...")

        //Set refreshView as currently refreshing
        view.swipe_refresh_news_fragment.isRefreshing = true

        //Initialize counter that validates ammount of parsed feeds
        var finishedFeedsCounter = 0

        //Prepare array of urls
        var urls: ArrayList<String> = arrayListOf("http://pwr.edu.pl/rss/pl/24.xml", "http://weka.pwr.edu.pl/rss/pl/5.xml")

        //Run parser instance for every url
        for (url: String in urls) {
            //Run parser
            val parser = Parser()
            parser.execute(url)
            parser.onFinish(object : Parser.OnTaskCompleted {

                //Handle downloaded data
                override fun onTaskCompleted(list: ArrayList<Article>) {

                    //FIXME: Only temporary!
                    val name: String = when (url) {
                        "http://pwr.edu.pl/rss/pl/24.xml" -> "Politechnika Wrocławska"
                        "http://weka.pwr.edu.pl/rss/pl/5.xml" -> "Wydział Elektroniki"
                        else -> ""
                    }

                    for (article: Article in list) {
                        val news = NewsModel(article.title, article.link, article.pubDate, name)
                        if (!listOfNews.contains(news)) {
                            Log.i("getNews()", "Found news: ${news.title}")
                            listOfNews.add(news)
                            listOfNews = utils.sortArrayListOfNewsByDate(listOfNews)
                            view.recycler_view_news_fragment.adapter.notifyDataSetChanged()
                        }
                    }
                    finishedFeedsCounter++
                    if (finishedFeedsCounter >= urls.size) {
                        view.swipe_refresh_news_fragment.isRefreshing = false
                        Log.i("getNews()", "Data refresh finished!")

                    }

                }

                //Handle connection errors
                override fun onError() {
                    view.swipe_refresh_news_fragment.isRefreshing = false
                    Toast.makeText(activity, "Serwer PWr padł :-(", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
