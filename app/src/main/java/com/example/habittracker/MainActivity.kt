package com.example.habittracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.habittracker.databinding.ActivityMainBinding
import com.example.habittracker.ui.fragments.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    lateinit var navController: NavController
    private lateinit var toolbar: Toolbar
    lateinit var mainFragment: MainFragment

    private lateinit var drawer: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        initToolbar()
        initDrawer()
        setContentView(view)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    private fun initToolbar() {
        toolbar = viewBinding.mainToolbar
        drawer = viewBinding.drawerLayout
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }
    }

    fun initDrawer() {
        val navigation = viewBinding.navigationView
        navigation.setNavigationItemSelectedListener { item: MenuItem ->
            try {
                when (item.itemId) {
                    R.id.drawer_about -> navController.navigate(R.id.action_mainFragment_to_aboutFragment)
                    R.id.drawer_home -> navController.navigate(R.id.action_aboutFragment_to_mainFragment)
                }
            } catch (e: IllegalArgumentException) {

            }
            drawer.closeDrawer(GravityCompat.START)
            Log.i(TAG, "initToolbar: ${item.itemId}")
            true
        }

        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    fun getToolbar(): Toolbar = toolbar

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onBackPressed() {
        if (mainFragment.onBackPressed())
            super.onBackPressed()
    }

    fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}