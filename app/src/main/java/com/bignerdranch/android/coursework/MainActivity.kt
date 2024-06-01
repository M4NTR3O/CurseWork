package com.bignerdranch.android.coursework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.coursework.databinding.ActivityMainBinding
import com.bignerdranch.android.coursework.fragments.FavoriteRecipesFragment
import com.bignerdranch.android.coursework.fragments.FavouritesFragment
import com.bignerdranch.android.coursework.fragments.MainFragment
import com.bignerdranch.android.coursework.fragments.RecipeFragment
import com.bignerdranch.android.coursework.fragments.RequestListFragment
import com.bignerdranch.android.coursework.fragments.SearchFragment
import com.bignerdranch.android.coursework.models.Result
import com.bignerdranch.android.coursework.requestDatabase.Request
import java.util.UUID

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), SearchFragment.Callbacks, RequestListFragment.Callbacks, FavoriteRecipesFragment.Callbacks {

    private lateinit var binding: ActivityMainBinding
    private val dataModel: DataModel by viewModels()
    private val requestListViewModel:
            RequestListViewModel by lazy {
        ViewModelProviders.of(this).get(RequestListViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(MainFragment())
        binding.bottomNavigationView.setOnItemSelectedListener{
            item ->
            when(item.itemId) {
                R.id.favourites -> {
                    replaceFragment(FavoriteRecipesFragment())
                    true
                }

                R.id.main -> {
                    replaceFragment(MainFragment())
                    true
                }

                R.id.search -> {
                    replaceFragment(SearchFragment())
                    true
                }

                else -> {
                    true
                }
            }
            true
        }
        
        dataModel.message.observe(this, {
            replaceFragment(SearchFragment())
            binding.bottomNavigationView.selectedItemId = R.id.search
        })
    }

    override fun onRequestSelected(request: Request) {
        replaceFragment(SearchFragment())
        requestListViewModel.deleteRequest(request)
        binding.bottomNavigationView.selectedItemId = R.id.search
        dataModel.message.value = request.text
        var newRequest = Request()
        newRequest.text = request.text
        requestListViewModel.addRequest(newRequest)
    }

    override fun onRecipeSelected(bundle: Bundle)
    {
        intent = DetailsActivity.newIntent(this@MainActivity, bundle)
        startActivity(intent)
    }

    override fun onRowClick(bundle: Bundle){
        intent = DetailsActivity.newIntent(this@MainActivity, bundle)
        startActivity(intent)
    }


    private fun replaceFragment(fragment: Fragment){
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit()
        Log.d(TAG, "change to ${fragment}")
    }
}