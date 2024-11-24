package com.bignerdranch.android.coursework.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.coursework.R
import com.bignerdranch.android.coursework.RecipeAdapter
import com.bignerdranch.android.coursework.RecipeViewModel
import com.bignerdranch.android.coursework.RequestListViewModel
import com.bignerdranch.android.coursework.databinding.FragmentMainBinding
import com.bignerdranch.android.coursework.databinding.FragmentRequestListBinding
import com.bignerdranch.android.coursework.requestDatabase.Request
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "RequestListFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [RequestListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RequestListFragment() : Fragment() {
    interface Callbacks{
        fun onRequestSelected(request: Request)
    }

    private var callbacks: Callbacks? = null
    lateinit var binding: FragmentRequestListBinding
    private var adapter: RequestAdapter? = RequestAdapter(emptyList())
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
            binding.requestRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.requestRecyclerView.adapter = adapter
            binding.requestRecyclerView.visibility = View.VISIBLE
            binding.composeContainer.visibility = View.GONE
        } else {
            binding.requestRecyclerView.visibility = View.GONE
            binding.composeContainer.visibility = View.VISIBLE
            binding.composeContainer.setContent {
                InternetStatusFragment(requireContext())
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "Оно пыталось...")
        binding = FragmentRequestListBinding.inflate(inflater, container, false)
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
        requestListViewModel.requestListLiveData.observe(
            viewLifecycleOwner,
            Observer{ requests ->
                requests?.let {
                    Log.d(TAG, "Оно пыталось...")
                    adapter = RequestAdapter(requests)
                    binding.requestRecyclerView.adapter = adapter
                    binding.requestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    if (isInternetAvailable(requireContext())){
                        binding.requestRecyclerView.visibility = View.VISIBLE
                    }
                    else{
                        binding.requestRecyclerView.visibility = View.GONE
                    }
                }
            })
    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    @Composable
    fun InternetStatusFragment(context: Context) {
        // Состояние для подключения
        val internetStateFlow = remember { MutableStateFlow(isInternetAvailable(context)) }
        val isInternetAvailable by internetStateFlow.asStateFlow().collectAsState()

        // Запускаем проверку подключения с интервалом
        LaunchedEffect(Unit) {
            launch {
                while (true) {
                    internetStateFlow.value = isInternetAvailable(context)
                    kotlinx.coroutines.delay(1000) // Проверка каждые 3 секунды
                }
            }
        }

        // Интерфейс
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Если интернета нет
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

    private inner class RequestHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var request: Request

        private val titleTextView: TextView = itemView.findViewById(R.id.request_title)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(request: Request){
            this.request = request
            titleTextView.text = this.request.text
        }

        override fun onClick(v: View?) {
            callbacks?.onRequestSelected(request)
        }
    }
    private inner class RequestAdapter(var request: List<Request>): RecyclerView.Adapter<RequestHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : RequestHolder {
            val view = layoutInflater.inflate(R.layout.list_item_request, parent, false)
            return RequestHolder(view)
        }
        override fun getItemCount() = request.size
        override fun onBindViewHolder(holder: RequestHolder, position: Int) {
            val crime = request[position]
            holder.bind(crime)
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RequestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(): RequestListFragment {
            return RequestListFragment()
        }
    }
}