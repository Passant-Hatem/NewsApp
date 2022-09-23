package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsListAdapter
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapp.util.CustomAnimation
import com.example.newsapp.util.ResponseState
import com.example.newsapp.viewmodels.NewsViewModel


class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsListAdapter: NewsListAdapter

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    private var isError = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
                val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                val isNotAtBeginning = firstVisibleItemPosition >= 0
                val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
                val shouldPaginate =
                    isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                            isTotalMoreThanVisible && isScrolling
                if (shouldPaginate) {
                    viewModel.getNews("us")
                    isScrolling = false
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        }

        newsListAdapter = NewsListAdapter()
        binding.newsList.apply {
            adapter = newsListAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(scrollListener)
        }

        newsListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_newsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel = (activity as MainActivity).viewModel
        viewModel.newsList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResponseState.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { news ->
                        newsListAdapter.differ.submitList(news.articles.toList())
                        val totalPages = news.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.newsPages == totalPages
                        if (isLastPage)
                            binding.newsList.setPadding(0, 0, 0, 0)
                    }
                }
                is ResponseState.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        showErrorMessage(message)
                    }
                }
                is ResponseState.Loading -> {
                    showProgressBar()
                }
            }
        }

        binding.itemErrorMessage.btnRetry.setOnClickListener {
            it.startAnimation(CustomAnimation.buttonAnime())
            viewModel.getNews("us")
        }

        return binding.root
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        binding.itemErrorMessage.root.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        binding.itemErrorMessage.root.visibility = View.VISIBLE
        binding.itemErrorMessage.tvErrorMessage.text = message
        isError = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}