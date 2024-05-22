package com.bignerdranch.android.coursework

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
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
        val spoonacularLiveData: LiveData<List<Result>> = SpoonacularFetchr().getRecipes()
        spoonacularLiveData.observe(
            this,
            Observer { recipeItems ->
                Log.d(TAG, "Response received:$recipeItems")
            })
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
        })
        recipeViewModel.recipeItemLiveData.observe(
            viewLifecycleOwner,
            Observer { recipeItems ->
                binding.recipeRecyclerView.adapter = RecipeAdapter(recipeItems)
                binding.recipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recipeRecyclerView.visibility = View.VISIBLE
            }
        )
    }

    private class RecipeHolder(itemTextView: TextView) : RecyclerView.ViewHolder(itemTextView)
    {
        val bindTitle: (CharSequence) -> Unit = itemTextView::setText
    }

    private class RecipeAdapter(private val recipeItems: List<Result>) : RecyclerView.Adapter<RecipeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
            val textView = TextView(parent.context)
            return RecipeHolder(textView)
        }
        override fun getItemCount(): Int = recipeItems.size
        override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
            val recipeItem = recipeItems[position]
            holder.bindTitle(recipeItem.title)
        }
    }


    companion object {
        fun newInstance() = SearchFragment()
    }
}