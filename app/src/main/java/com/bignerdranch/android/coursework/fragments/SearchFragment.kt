package com.bignerdranch.android.coursework.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.coursework.DataModel
import com.bignerdranch.android.coursework.R
import com.bignerdranch.android.coursework.RBtnClick
import com.bignerdranch.android.coursework.RecipeAdapter
import com.bignerdranch.android.coursework.RecipeViewModel
import com.bignerdranch.android.coursework.RequestListViewModel
import com.bignerdranch.android.coursework.databinding.FragmentSearchBinding
import com.bignerdranch.android.coursework.models.Result
import com.bignerdranch.android.coursework.requestDatabase.Request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?

        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                requireActivity().runOnUiThread {
                    updateUI(true)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                requireActivity().runOnUiThread {
                    updateUI(false)
                }
            }
        }
    }

    private fun updateUI(isConnected: Boolean) {
        if (isConnected) {
            binding.recipeRecyclerView.visibility = View.VISIBLE
            binding.composeContainer.visibility = View.GONE
        } else {
            binding.recipeRecyclerView.visibility = View.GONE
            binding.composeContainer.visibility = View.VISIBLE
            binding.composeContainer.setContent {
                InternetStatusFragment()
            }
        }
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
        val isConnected = isInternetAvailable(requireContext())
        updateUI(isConnected)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onStop() {
        super.onStop()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isInternetAvailable(requireContext())){
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
        }
        binding.searchingButton.setOnClickListener {
            recipeViewModel.fetchRecipe(binding.searchQuery.text.toString())
            requestListViewModel.deleteRequestCount()
            var request = Request()
            request.text = binding.searchQuery.text.toString()
            CoroutineScope(Dispatchers.IO).launch{
                val newRequest = requestListViewModel.getRequest(request)
                if (newRequest != null) {
                    requestListViewModel.deleteRequest(newRequest)
                    requestListViewModel.addRequest(request)
                }
                else{
                    requestListViewModel.addRequest(request)
                }
            }
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

    @Composable
    fun InternetStatusFragment() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                tonalElevation = 4.dp
            ) {
                Text(
                    text = getString(R.string.noConnection),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }



    companion object {
        fun newInstance() = SearchFragment()
    }
}