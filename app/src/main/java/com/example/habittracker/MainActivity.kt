package com.example.habittracker

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.habittracker.databinding.ActivityMainBinding
import com.example.habittracker.models.Habit
import com.example.habittracker.models.HabitList
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    lateinit var storage: MutableList<Habit>
    lateinit var navController: NavController
    private lateinit var toolbar: Toolbar

    private lateinit var drawer: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        initToolbar()
        initDrawer()
        storage = mutableListOf()
        setContentView(view)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.apply {
            storage = (getSerializable(STORAGE) as HabitList).habits as MutableList<Habit>
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putSerializable(STORAGE, HabitList(storage))
        }
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val STORAGE = "storage"
    }
}