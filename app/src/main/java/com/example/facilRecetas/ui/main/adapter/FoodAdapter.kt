package com.example.facilRecetas.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.example.facilRecetas.R
import com.example.facilRecetas.data.models.Food
import com.example.facilRecetas.data.models.Recette
import com.example.facilRecetas.persistence.RecetteEntity

class FoodAdapter(private var foodList: List<Recette>): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class FoodViewHolder(itemView: View,listener: OnItemClickListener):RecyclerView.ViewHolder(itemView){
        val food_name = itemView.findViewById<TextView>(R.id.foodName)
        val food_image = itemView.findViewById<ImageView>(R.id.foodImage)
        val food_area = itemView.findViewById<TextView>(R.id.foodArea)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(adapterPosition)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return FoodViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]
        holder.food_name.text = food.name
        holder.food_area.text = food.area
        Picasso.get().load(food.image).into(holder.food_image)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    fun filterList(filteredList: ArrayList<Recette>) {
        foodList = filteredList
        notifyDataSetChanged()

    }
}
