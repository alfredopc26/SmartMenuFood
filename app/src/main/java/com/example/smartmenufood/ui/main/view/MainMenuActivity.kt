package com.example.smartmenufood.ui.main.view


import android.os.Bundle
import android.view.MenuItem

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.example.smartmenufood.R
import com.example.smartmenufood.databinding.ActivityMainBinding
import com.example.smartmenufood.ui.main.fragment.FavoriteFragment
import com.example.smartmenufood.ui.main.fragment.HomeFragment
import com.example.smartmenufood.ui.main.fragment.MenuFragment
import com.example.smartmenufood.ui.main.fragment.SmartMenuFragment


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
        drawerLayout = findViewById(R.id.drawerLayout)


        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // bottom navigation bar
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_menu -> replaceFragment(MenuFragment())
                R.id.navigation_smartmenu -> replaceFragment(SmartMenuFragment())
                R.id.navigation_favorite -> replaceFragment(FavoriteFragment())
            }
            true
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragments_container, fragment)
        fragmentTransaction.commit()
    }

}