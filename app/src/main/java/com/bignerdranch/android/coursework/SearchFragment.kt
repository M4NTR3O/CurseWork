package com.bignerdranch.android.coursework

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bignerdranch.android.coursework.databinding.FragmentSearchBinding
import com.bignerdranch.android.coursework.network.FoodRecipeApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TAG = "SearchFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val dataModel: DataModel by activityViewModels()
    lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val spoonacularLiveData: LiveData<String> = SpoonacularFetchr().getRecipes()
        spoonacularLiveData.observe(
            this,
            Observer { responseString ->Log.d(TAG, "Response received: $responseString") }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataModel.message.observe(activity as LifecycleOwner,{
            binding.searchQuery.setText(it)
        })
    }

    companion object {

    }
}