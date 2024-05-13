package com.bignerdranch.android.coursework

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.coursework.requestDatabase.Request
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
class RequestListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    interface Callbacks{
        fun onRequestSelected(requestId: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var requestRecyclerView: RecyclerView
    private var adapter: RequestAdapter? = RequestAdapter(emptyList())
    private val requestListViewModel:
            RequestListViewModel by lazy {
                ViewModelProviders.of(this).get(RequestListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_request_list, container, false)
        requestRecyclerView = view.findViewById(R.id.request_recycler_view) as RecyclerView
        requestRecyclerView.layoutManager = LinearLayoutManager(context)
        requestRecyclerView.adapter = adapter
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestListViewModel.requestListLiveData.observe(
            viewLifecycleOwner,
            Observer{ requests ->
                requests?.let {
                    Log.i(TAG, "Got request${requests.size}")
                    updateUI(requests)
                }
            })
    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_request -> {
                val request = Request()
                requestListViewModel.addRequest(request)
                callbacks?.onRequestSelected(request.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }*/
    private fun updateUI(requests: List<Request>) {
        adapter = RequestAdapter(requests)
        requestRecyclerView.adapter = adapter
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
            callbacks?.onRequestSelected(request.id)
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