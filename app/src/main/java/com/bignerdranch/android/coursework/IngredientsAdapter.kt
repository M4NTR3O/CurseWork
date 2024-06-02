package com.bignerdranch.android.coursework

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.coursework.models.ExtendedIngredient
import com.squareup.picasso.Picasso

class IngredientsAdapter(private val context: Context) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    private var ingredientList = emptyList<ExtendedIngredient>()

    inner class IngredientsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientImageView: ImageView = itemView.findViewById(R.id.ingredient_imageView)
        val ingredientName: TextView = itemView.findViewById(R.id.ingredient_name)
        val ingredientAmount: TextView = itemView.findViewById(R.id.ingredient_amount)
        val ingredientUnit: TextView = itemView.findViewById(R.id.ingredient_unit)
        val ingredientConsistency: TextView = itemView.findViewById(R.id.ingredient_consistency)
        val ingredientOriginal: TextView = itemView.findViewById(R.id.ingredient_original)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.ingredients_row_layout, parent, false)
        return IngredientsViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.apply {
            val currentIngredient = ingredientList[position]
            Picasso.get()
                .load("https://img.spoonacular.com/ingredients_100x100/" + currentIngredient.image)
                .placeholder(R.drawable.baseline_search_24)
                .into(ingredientImageView)

            ingredientName.text = currentIngredient.name
            ingredientAmount.text = currentIngredient.amount.toString()
            ingredientUnit.text = currentIngredient.unit
            ingredientConsistency.text = currentIngredient.consistency
            ingredientOriginal.text = currentIngredient.original

        }
    }

    override fun getItemCount(): Int = ingredientList.size

    fun setData(newIngredientList: List<ExtendedIngredient>) {
        val ingredientDiffUtil = RecipesDiffUtil(ingredientList, newIngredientList)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientDiffUtil)
        ingredientList = newIngredientList
        diffUtilResult.dispatchUpdatesTo(this)
    }

}