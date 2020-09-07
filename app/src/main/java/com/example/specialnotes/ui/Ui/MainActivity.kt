package com.example.specialnotes.ui.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.specialnotes.R
import com.example.specialnotes.ui.db.ArticleDatabase
import com.example.specialnotes.ui.repositories.NewsRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository= NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory= NewsViewModelProviderFactory(application,repository)
        viewModel= ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavView.setupWithNavController(navController)

        val upBottomNavView= findViewById<BottomNavigationView>(R.id.bo)
        upBottomNavView.setOnNavigationItemSelectedListener {item->
            when(item.itemId) {
                R.id.AccountNAvigation -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.home -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.share -> {
                // Respond to navigation item 2 click
                true
               }
                else -> false
            }
        }

    }
}