package com.ikurek.pwr

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import com.ikurek.pwr.news.NewsModel
import java.util.*


/**
 * Created by igor on 18.10.17.
 */
class Utils(private val activity: Activity) {

    /**
     * Swaps current fragment with another one
     * @param fragment A [Fragment] object that will replace current view
     * @param shouldAddToBackStack A simple boolean that defines, if a fragment should be added to backstack
     */
    fun swapFragments(fragment: Fragment, shouldAddToBackStack: Boolean) {
        val transaction = activity.fragmentManager.beginTransaction().replace(R.id.fragment_layout, fragment)

        if (shouldAddToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()

    }

    /**
     * Opens given url using Android built-in URI handling system
     * @param url Adress of website to open
     */
    fun openUrlInBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(browserIntent)
    }

    /**
     * Opens url using Chrome Custom Tabs technology
     * TODO: Should check, if Chrome custom tabs are enabled on device
     * @param url Adress of website to open
     */
    fun openUrlInCustomTab(url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(activity.resources.getColor(R.color.colorPrimary))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(activity, Uri.parse(url))
    }

    /**
     * Used by [NewsFragment] to sort array of [NewsModel] objects by date
     * This is just a coustom implementation of Kotlin's native [Comparator] class
     * @param listOfNews ArrayList of [NewsModel] objects to sort
     * @return sorted ArrayList of [NewsModel] objects
     */
    fun sortArrayListOfNewsByDate(listOfNews: ArrayList<NewsModel>): ArrayList<NewsModel> {
        Collections.sort(listOfNews) { x, y -> y.date.compareTo(x.date) }
        return listOfNews
    }


}