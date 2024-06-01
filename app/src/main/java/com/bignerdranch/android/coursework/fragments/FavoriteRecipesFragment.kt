package com.bignerdranch.android.coursework.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.coursework.BtnClick
import com.bignerdranch.android.coursework.FavoriteRecipeAdapter
import com.bignerdranch.android.coursework.MainViewModel
import com.bignerdranch.android.coursework.R
import com.bignerdranch.android.coursework.databinding.FragmentFavouritesBinding
import com.bignerdranch.android.coursework.models.Result
import com.google.android.material.snackbar.Snackbar

class FavoriteRecipesFragment: Fragment(), BtnClick {
    interface Callbacks {
        fun onRowClick(bundle: Bundle)
    }
    private var callbacks: Callbacks? = null
    private lateinit var binding: FragmentFavouritesBinding
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
    private val favoriteAdapter: FavoriteRecipeAdapter by lazy {
        FavoriteRecipeAdapter(
            requireContext(),
            this@FavoriteRecipesFragment,
            requireActivity(),
            mainViewModel
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as FavoriteRecipesFragment.Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        setupRecyclerView()

        mainViewModel.readFavoriteRecipe.observe(viewLifecycleOwner, Observer { favoriteEntity ->
            if (favoriteEntity.isEmpty()) {
                binding.noDataImageView.visibility = View.VISIBLE
                binding.noDataTextView.visibility = View.VISIBLE
            } else {
                binding.noDataImageView.visibility = View.INVISIBLE
                binding.noDataTextView.visibility = View.INVISIBLE
            }
            favoriteAdapter.setData(favoriteEntity)
        })


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipe_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll_favorite_recipes_menu){
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar()
        }

        return super.onOptionsItemSelected(item)
    }
    private fun showSnackBar(){
        Snackbar.make(
            binding.root,
            "All recipes removed",
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }
    private fun setupRecyclerView() {
        binding.favoriteRecipesRecyclerView.adapter = favoriteAdapter
        binding.favoriteRecipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onRowClick(result: Result) {
        var newBundle = Bundle()
        newBundle.putParcelable("result", result)
        callbacks?.onRowClick(newBundle)
    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteAdapter.clearContextualActionMode()
    }
}