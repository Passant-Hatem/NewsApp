package com.example.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.MainActivity
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.viewmodels.NewsViewModel

class SavedNews : Fragment() {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)

        viewModel = (activity as MainActivity).viewModel

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}