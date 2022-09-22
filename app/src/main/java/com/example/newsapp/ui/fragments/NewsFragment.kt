package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsListAdapter
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.util.ResponseState
import com.example.newsapp.viewmodels.NewsViewModel

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsListAdapter: NewsListAdapter

    private val TAG = "NewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        setupRecyclerView()

        newsListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article" ,it)
            }
            findNavController().navigate(
                R.id.action_newsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel = (activity as MainActivity).viewModel
        viewModel.news.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseState.Success -> {
                    hideProgressBar()
                    response.data?.let { news ->
                        newsListAdapter.differ.submitList(news.articles)
                    }
                }
                is ResponseState.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }
                is ResponseState.Loading -> {
                    showProgressBar()
                }
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
       newsListAdapter = NewsListAdapter()
       binding.newsList.apply {
           adapter = newsListAdapter
           layoutManager = LinearLayoutManager(activity)
       }
    }

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}