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
import com.bignerdranch.android.coursework.data.network.FoodRecipeApi
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.UUID

private const val TAG = "SearchFragment"

class SearchFragment : Fragment(), RBtnClick {

    interface Callbacks {
        fun onRecipeSelected(bundle: Result)
    }
    private var callbacks: Callbacks? = null
    private val dataModel: DataModel by activityViewModels()
    lateinit var binding: FragmentSearchBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private val mRecipeAdapter by lazy {
        RecipeAdapter(requireContext(), this@SearchFragment, emptyList<Result>())
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

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
        binding.recipeRecyclerView.adapter = mRecipeAdapter
        binding.recipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

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
                binding.recipeRecyclerView.adapter = RecipeAdapter(requireContext(), this, recipeItems)
                binding.recipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recipeRecyclerView.visibility = View.VISIBLE
            }
        )
        binding.searchingButton.setOnClickListener {
            recipeViewModel.fetchRecipe(binding.searchQuery.text.toString())
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
    override fun getRecipeOnClick(bundle: Result) {
        Log.d(TAG, "getRecipeOnClick: $bundle")
        callbacks?.onRecipeSelected(bundle)
    }



    companion object {
        fun newInstance() = SearchFragment()
    }
}