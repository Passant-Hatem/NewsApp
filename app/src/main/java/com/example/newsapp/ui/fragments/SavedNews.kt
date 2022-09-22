package com.example.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsListAdapter
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.viewmodels.NewsViewModel

class SavedNews : Fragment() {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: NewsViewModel

    lateinit var newsListAdapter: NewsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)

        viewModel = (activity as MainActivity).viewModel

        newsListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article" ,it)
            }
            findNavController().navigate(
                R.id.action_savedNews_to_articleFragment,
                bundle
            )
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        newsListAdapter = NewsListAdapter()
        binding.savedNewsList.apply {
            adapter = newsListAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}