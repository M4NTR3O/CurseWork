package com.bignerdranch.android.coursework

import android.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.coursework.databinding.ActivityMainBinding
import com.bignerdranch.android.coursework.models.Result

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), SearchFragment.Callbacks {

    private lateinit var binding: ActivityMainBinding
    private val dataModel: DataModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(MainFragment())
        binding.bottomNavigationView.setOnItemSelectedListener{
            item ->
            when(item.itemId) {
                R.id.favourites -> {
                    replaceFragment(FavouritesFragment())
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

    override fun onRecipeSelected(bundle: Result)
    {
        val fragment = RecipeFragment(bundle)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun replaceFragment(fragment: Fragment){
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit()
    }
}