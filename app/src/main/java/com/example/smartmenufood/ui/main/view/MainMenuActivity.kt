package com.example.smartmenufood.ui.main.view


import android.view.Menu

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.smartmenufood.MainActivity
import com.google.android.material.navigation.NavigationView
import com.example.smartmenufood.R
import com.example.smartmenufood.databinding.ActivityMainBinding
import com.example.smartmenufood.ui.fragment.*


class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Drawer
        drawerLayout = findViewById(R.id.container)
        navigationView = findViewById(R.id.nav_view)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_menu -> replaceFragment(MenuFragment())
                R.id.navigation_smartmenu -> replaceFragment(SmartMenuFragment())
                R.id.navigation_favorite -> replaceFragment(FavoriteFragment())
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment) {
         val fragmentTransaction = supportFragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.nav_view, fragment)
         fragmentTransaction.commit()
     }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}