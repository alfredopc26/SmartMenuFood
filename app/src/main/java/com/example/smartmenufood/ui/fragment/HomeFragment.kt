package com.example.smartmenufood.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmenufood.R
import com.example.smartmenufood.ui.home.BlogAdapter
import com.example.smartmenufood.ui.home.Recette
import com.example.smartmenufood.ui.main.view.MainMenuActivity
import com.google.android.material.button.MaterialButton

class HomeFragment : Fragment() {
    //************* Recommended food *************//
    private lateinit var recommendedFoodRecyclerView: RecyclerView
    private lateinit var recommendedFoodAdapter: BlogAdapter

    private lateinit var recetteArray:ArrayList<Recette>
    private lateinit var recetteArrayHeader: TextView

    private lateinit var username: TextView
    lateinit var usernameString : String

    lateinit var user : HashMap<String, String>

    private lateinit var healthy: MaterialButton
    private lateinit var noBioLL: LinearLayout
    private lateinit var bioLL: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navDrawerButton = view.findViewById<Button>(R.id.menu)
        username= view.findViewById(R.id.title_username)
        noBioLL=view.findViewById(R.id.noIsBioLL)



    }
}