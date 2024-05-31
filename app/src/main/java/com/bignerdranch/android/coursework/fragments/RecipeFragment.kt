package com.bignerdranch.android.coursework.fragments

import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.bignerdranch.android.coursework.databinding.FragmentInstructionBinding
import com.bignerdranch.android.coursework.models.Result


private const val TAG = "RecipeFragment"

class RecipeFragment(private val bundle: Result): Fragment() {
    private lateinit var binding: FragmentInstructionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInstructionBinding.inflate(inflater, container, false)

        binding.instructionWebView.webViewClient = object : WebViewClient(){}
        binding.instructionWebView.loadUrl(bundle.sourceUrl)

        return binding.root
    }
}