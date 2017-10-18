package com.ikurek.pwr

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.crashlytics.android.Crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.ikurek.pwr.buildings.BuildingsFragment
import com.ikurek.pwr.jsos.JSOSFragment
import com.ikurek.pwr.news.NewsFragment
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mainFragment = NewsFragment()
    private val utils = Utils(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupNavigationDrawer()
        utils.swapFragments(mainFragment, false)
        Fabric.with(this, Crashlytics())
        FirebaseMessaging.getInstance().subscribeToTopic("jsos_status")
    }

    /**
     * Cases to handle onBackPressed:
     * 1. Close drawer when open
     * 2. Call super if backstack of fragmentManager is empty
     * 3. Exit the app otherwise
     * I love how much I need to handle here using those fucking fragments...
     */
    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            fragmentManager.backStackEntryCount != 0 -> super.onBackPressed()
            else -> showExitDialog()
        }
    }


    /**
     * Handles navigation drawer transactions
     * Usually, just fires swapFragment
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_news -> {
                utils.swapFragments(mainFragment, false)
            }
            R.id.nav_buildings -> {
                utils.swapFragments(BuildingsFragment(), false)
            }
            R.id.nav_jsosstatus -> {
                utils.swapFragments(JSOSFragment(), false)
            }

            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * A basic navigation drawer configuration
     * Enables toggle, adds listeners
     * Sets 1st navigation drawer as selected on startup
     */
    private fun setupNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.nav_news)
    }

    //TODO: I'll fix this when I know, what 1st startup should do
    private fun setupForFirstStart() {
        //Load RSS Feeds to sharedpreferences
        val sharedPreferences = getSharedPreferences("com.ikurek.pwr.general", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("first_start", true)) {
            //TODO: Handle 1st start actions here
            sharedPreferences.edit().putBoolean("first_start", false).apply()
        }
    }

    /**
     * Shows a dialog, that ensures user wanted to close the app
     * Usefull for handling accidental back presses, etc
     */
    private fun showExitDialog() {
        val alertDialogExit = AlertDialog.Builder(this).create()
        alertDialogExit.setTitle(getString(R.string.action_exit))
        alertDialogExit.setMessage(getString(R.string.description_confirm_quit))

        //Button hides dialog
        alertDialogExit.setButton(
                AlertDialog.BUTTON_POSITIVE,
                getString(R.string.action_cancel))
        { dialog, _ -> dialog!!.dismiss() }

        //Button exits app
        alertDialogExit.setButton(
                AlertDialog.BUTTON_NEGATIVE,
                getString(R.string.action_exit))
        { dialog, _ -> dialog!!.dismiss(); this.finishAffinity() }

        alertDialogExit.show()
    }

}
