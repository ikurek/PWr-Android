package com.ikurek.pwr.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ikurek.pwr.R
import kotlinx.android.synthetic.main.news_row.view.*
import java.util.zip.Inflater

/**
 * Created by igor on 15.10.17.
 */
class NewsAdapter(private var listOfNews : ArrayList<NewsModel>, private var listener: (NewsModel) -> Unit) : RecyclerView.Adapter<NewsAdapter.NewsRowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsRowViewHolder =
            NewsRowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_row, parent, false))

    override fun onBindViewHolder(holderNewsRow: NewsRowViewHolder, position: Int) =
            holderNewsRow.bind(listOfNews[position], listener)

    override fun getItemCount(): Int = listOfNews.size

    class NewsRowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(news : NewsModel, listener: (NewsModel) -> Unit) {
            itemView.title_news_row.text = news.title
            itemView.date_news_row.text = news.date
            itemView.setOnClickListener { listener(news) }
        }
    }
}