package com.ikurek.pwr.news


import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.ikurek.pwr.R
import kotlinx.android.synthetic.main.fragment_news.view.*


class NewsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_news, container, false)

        setupRecyclerView(view)

        return view
    }

    private fun setupRecyclerView(view : View) {
        var listOfNews : ArrayList<NewsModel> = arrayListOf()
        listOfNews.add(NewsModel("test", "test", "test"))
        listOfNews.add(NewsModel("test2", "test", "test"))
        listOfNews.add(NewsModel("test3", "test", "test"))

        view.recycler_view_news_fragment.layoutManager = LinearLayoutManager(activity)
        view.recycler_view_news_fragment.adapter = NewsAdapter(listOfNews) {
            Toast.makeText(activity, "Clicked news: ${it.title}", Toast.LENGTH_LONG).show()
        }
    }

}
