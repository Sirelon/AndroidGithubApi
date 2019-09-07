package com.sirelon.githubapi

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sirelon.githubapi.database.AppDataBase
import com.sirelon.githubapi.feature.auth.AppSession
import com.sirelon.githubapi.feature.auth.AuthActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


private const val ITEM_ID = 1231;

class MainActivity : AppCompatActivity() {

    private val appSession by inject<AppSession>()
    private val database by inject<AppDataBase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!appSession.isLoggedIn()) {
            startAuthScreen()
            return
        }

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_search_repo,
                R.id.navigation_saved_items,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun startAuthScreen() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, ITEM_ID, 1, "Logout")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == ITEM_ID) {
            AlertDialog.Builder(this).setMessage("Are you sure?")
                .setPositiveButton("Yes") { _, _ -> logout() }
                .setNegativeButton("No", null)
                .show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        appSession.invalidate()
        // Clear database.
        // GlobalScope need to be use, 'cause we finish this activity. But it's not necessary wait that time.
        GlobalScope.launch { database.clearAllTables() }
        startAuthScreen()
        finish()
    }
}
