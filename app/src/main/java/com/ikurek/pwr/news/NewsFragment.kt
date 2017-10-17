package com.ikurek.pwr.news


import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ikurek.pwr.R
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.android.synthetic.main.fragment_news.view.*


class NewsFragment : Fragment() {

    val TAG = "NewsFragment"
    var listOfNews: ArrayList<NewsModel> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_news, container, false)

        setupRecyclerView(view)
        getNews(view)
        return view
    }

    private fun setupRecyclerView(view : View) {
        view.recycler_view_news_fragment.layoutManager = LinearLayoutManager(activity)
        view.recycler_view_news_fragment.adapter = NewsAdapter(listOfNews) {
            Toast.makeText(activity, "Clicked news: ${it.title}", Toast.LENGTH_LONG).show()
        }
        view.swipe_refresh_news_fragment.setOnRefreshListener {
            getNews(view)
        }
    }

    private fun getNews(view: View) {

        view.swipe_refresh_news_fragment.isRefreshing = true

        if (!listOfNews.isEmpty()) {
            listOfNews.clear()
        }

        val parser = Parser()
        parser.execute("http://pwr.edu.pl/rss/pl/24.xml")
        parser.onFinish(object : Parser.OnTaskCompleted {

            override fun onTaskCompleted(list: ArrayList<Article>) {
                for (article: Article in list) {
                    listOfNews.add(NewsModel(article.title, article.link, article.pubDate.toString()))
                }
                view.recycler_view_news_fragment.adapter.notifyDataSetChanged()
                view.swipe_refresh_news_fragment.isRefreshing = false
            }

            override fun onError() {
                view.swipe_refresh_news_fragment.isRefreshing = false
                Toast.makeText(activity, "Serwer PWr padł :-(", Toast.LENGTH_LONG).show()
            }
        })


    }

}
