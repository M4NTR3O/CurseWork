package com.bignerdranch.android.coursework

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.coursework.databinding.FragmentSearchBinding
import com.bignerdranch.android.coursework.databinding.ListItemRequestBinding
import com.bignerdranch.android.coursework.models.Result
import com.bignerdranch.android.coursework.network.FoodRecipeApi
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "SearchFragment"

class SearchFragment : Fragment() {

    private val dataModel: DataModel by activityViewModels()
    lateinit var binding: FragmentSearchBinding
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*        val spoonacularLiveData: LiveData<List<Result>> = SpoonacularFetchr().searchRecipes("")
        spoonacularLiveData.observe(
            this,
            Observer { recipeItems ->
                Log.d(TAG, "Response received:$recipeItems")
            })*/
        recipeViewModel =
            ViewModelProviders.of(this).get(RecipeViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.recipeRecyclerView.layoutManager = GridLayoutManager(context, 1)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataModel.message.observe(activity as LifecycleOwner,{
            binding.searchQuery.setText(it)
            recipeViewModel.fetchRecipe(binding.searchQuery.text.toString())
        })
        recipeViewModel.recipeItemLiveData.observe(
            viewLifecycleOwner,
            Observer { recipeItems ->
                binding.recipeRecyclerView.adapter = RecipeAdapter(requireContext(), recipeItems)
                binding.recipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recipeRecyclerView.visibility = View.VISIBLE
            }
        )
        binding.searchingButton.setOnClickListener {
            recipeViewModel.fetchRecipe(binding.searchQuery.text.toString())
        }
    }

    private class RecipeHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val recipeImage: ImageView = itemView.findViewById(R.id.recipe_imageView)
        val titleText: TextView = itemView.findViewById(R.id.title_textView)
        val tvDescription: TextView = itemView.findViewById(R.id.description_textView)
        val tvHeart: TextView = itemView.findViewById(R.id.heart_textView)
        val tvClock: TextView = itemView.findViewById(R.id.clock_textView)
        val leafImageView: ImageView = itemView.findViewById(R.id.leaf_imageView)
        val tvLeafVegan: TextView = itemView.findViewById(R.id.leaf_textView)
        val recipeRowLayout : ConstraintLayout = itemView.findViewById(R.id.recipesRowLayout)
    }

    private class RecipeAdapter(private val context : Context, private val recipeItems: List<Result>) : RecyclerView.Adapter<RecipeHolder>()  {

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
                /*recipeRowLayout.setOnClickListener {
                    listener.getRecipeOnClick(currentRecipe)
                }*/
            }
        }
    }


    companion object {
        fun newInstance() = SearchFragment()
    }
}