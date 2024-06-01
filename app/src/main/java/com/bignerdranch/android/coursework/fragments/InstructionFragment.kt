package com.bignerdranch.android.coursework.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.bignerdranch.android.coursework.databinding.FragmentInstructionBinding
import com.bignerdranch.android.coursework.models.Result

class InstructionFragment(var args: Result) : Fragment() {

    private lateinit var binding: FragmentInstructionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInstructionBinding.inflate(inflater, container, false)

        val myBundle: Result = args

        binding.instructionWebView.webViewClient = object : WebViewClient(){}
        binding.instructionWebView.loadUrl(myBundle.sourceUrl)

        return binding.root
    }
}