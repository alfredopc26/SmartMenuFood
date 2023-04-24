package com.example.facilRecetas.ui.main.view


import android.os.Bundle
import android.view.MenuItem

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.example.facilRecetas.R
import com.example.facilRecetas.databinding.ActivityMainBinding
import com.example.facilRecetas.ui.main.fragment.*
import com.example.facilRecetas.utils.services.LoadingDialog


class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog (this)

        // Drawer
        drawerLayout = findViewById(R.id.drawerLayout)

        // bottom navigation bar
        supportFragmentManager.beginTransaction().replace(R.id.fragments_container, HomeFragment())
            .commit()
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_menu -> replaceFragment(FoodFragment())
                R.id.navigation_smartmenu -> replaceFragment(BasketFragment())
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