package com.bignerdranch.android.coursework.fragments

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.coursework.DataModel
import com.bignerdranch.android.coursework.RBtnClick
import com.bignerdranch.android.coursework.RecipeAdapter
import com.bignerdranch.android.coursework.RecipeViewModel
import com.bignerdranch.android.coursework.RequestListViewModel
import com.bignerdranch.android.coursework.databinding.FragmentSearchBinding
import com.bignerdranch.android.coursework.models.Result
import com.bignerdranch.android.coursework.requestDatabase.Request

private const val TAG = "SearchFragment"

class SearchFragment : Fragment(), RBtnClick {

    interface Callbacks {
        fun onRecipeSelected(bundle: Bundle)
    }
    private var callbacks: Callbacks? = null
    private val dataModel: DataModel by activityViewModels()
    lateinit var binding: FragmentSearchBinding
    private lateinit var recipeViewModel: RecipeViewModel
    private val mRecipeAdapter by lazy {
        RecipeAdapter(requireContext(), this@SearchFragment, emptyList<Result>())
    }
    private val requestListViewModel:
            RequestListViewModel by lazy {
        ViewModelProviders.of(this).get(RequestListViewModel::class.java)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            requestListViewModel.deleteRequestCount()
            var request = Request()
            request.text = binding.searchQuery.text.toString()
            requestListViewModel.addRequest(request)
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
    override fun getRecipeOnClick(bundle: Result) {
        var newBundle = Bundle()
        newBundle.putParcelable("result", bundle)
        Log.d(TAG, "getRecipeOnClick: $newBundle")
        callbacks?.onRecipeSelected(newBundle)
    }



    companion object {
        fun newInstance() = SearchFragment()
    }
}