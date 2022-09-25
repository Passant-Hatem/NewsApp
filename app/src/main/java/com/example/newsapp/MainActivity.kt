package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.dp.room.ArticleDataBase
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.viewmodels.news.NewsViewModel
import com.example.newsapp.viewmodels.news.NewsViewModelFactory
import com.example.newsapp.viewmodels.saved.SavedNewsViewModel
import com.example.newsapp.viewmodels.saved.SavedNewsViewModelFactory
import com.example.newsapp.viewmodels.search.SearchViewModel
import com.example.newsapp.viewmodels.search.SearchViwModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var newsViewModel: NewsViewModel
    lateinit var searchViewModel: SearchViewModel
    lateinit var savedNewsViewModel: SavedNewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navController = setupNavController()
        attachNavUIToController(navController)

        val newsRepository = NewsRepo(ArticleDataBase(this))

        //set news view model
        val newsViewModelProviderFactory = NewsViewModelFactory(application, newsRepository)
        newsViewModel =
            ViewModelProvider(this, newsViewModelProviderFactory)[NewsViewModel::class.java]

        //set search view model
        val searchViewModelProviderFactory = SearchViwModelFactory(application, newsRepository)
        searchViewModel =
            ViewModelProvider(this, searchViewModelProviderFactory)[SearchViewModel::class.java]

        //set saved news view model
        val savedViewModelProviderFactory = SavedNewsViewModelFactory(newsRepository)
        savedNewsViewModel =
            ViewModelProvider(this, savedViewModelProviderFactory)[SavedNewsViewModel::class.java]
    }

    private fun attachNavUIToController(navController: NavController) {
        val navView: BottomNavigationView = binding.bottomNav

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.newsFragment,
                R.id.savedNews,
                R.id.searchFragment,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setupNavController(): NavController {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        return navHostFragment.navController
    }
}