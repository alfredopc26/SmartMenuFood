package com.example.smartmenufood.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.smartmenufood.R
import com.example.smartmenufood.ui.main.fragment.GuidePageOneFragment


class GuideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        val fragment = GuidePageOneFragment()
        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.fragmentContainer,fragment)
        trans.commit()

        val skip = findViewById<TextView>(R.id.skipTV)
        skip.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


    }
}