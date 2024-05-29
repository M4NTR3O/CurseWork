package com.bignerdranch.android.coursework

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.coursework.models.Result
import com.squareup.picasso.Picasso

interface RBtnClick
{
    fun getRecipeOnClick(bundle : Result)
}

class RecipeAdapter(private val context : Context, private val listener : SearchFragment, private val recipeItems: List<Result>) : RecyclerView.Adapter<RecipeAdapter.RecipeHolder>()  {

    inner class RecipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private lateinit var recipe: Result
        val recipeImage: ImageView = itemView.findViewById(R.id.recipe_imageView)
        val titleText: TextView = itemView.findViewById(R.id.title_textView)
        val tvDescription: TextView = itemView.findViewById(R.id.description_textView)
        val tvHeart: TextView = itemView.findViewById(R.id.heart_textView)
        val tvClock: TextView = itemView.findViewById(R.id.clock_textView)
        val leafImageView: ImageView = itemView.findViewById(R.id.leaf_imageView)
        val tvLeafVegan: TextView = itemView.findViewById(R.id.leaf_textView)
        val recipeRowLayout : ConstraintLayout = itemView.findViewById(R.id.recipesRowLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_recipes, parent, false)
        return RecipeHolder(view)
    }
    override fun getItemCount(): Int = recipeItems.size
    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        holder.apply {
            val currentRecipe = recipeItems[position]

            titleText.text = currentRecipe.title
            tvDescription.text = currentRecipe.summary
            tvHeart.text = currentRecipe.aggregateLikes.toString()
            tvClock.text = currentRecipe.readyInMinutes.toString()

            if (currentRecipe.vegan){
                tvLeafVegan.setTextColor(ContextCompat.getColor(context, R.color.green))
                leafImageView.setColorFilter(ContextCompat.getColor(context, R.color.green))
            }
            Picasso.get()
                .load(currentRecipe.image)
                .placeholder(R.drawable.baseline_search_24)
                .into(recipeImage)
            recipeRowLayout.setOnClickListener {
                listener.getRecipeOnClick(currentRecipe)
            }
        }
    }
}