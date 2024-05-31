package com.bignerdranch.android.coursework.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.coursework.IngredientsAdapter
import com.bignerdranch.android.coursework.databinding.FragmentIngredientsBinding
import com.bignerdranch.android.coursework.models.Result

private const val TAG = "IngredientsFragment"
class IngredientsFragment : Fragment() {

    private lateinit var binding: FragmentIngredientsBinding
    private val mAdapter: IngredientsAdapter by lazy {
        IngredientsAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        setupRecyclerView()

        val myBundle: Result = arguments as Result

        myBundle.extendedIngredients.let {
            mAdapter.setData(it)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.ingredientsRecyclerview.adapter = mAdapter
        binding.ingredientsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }
}