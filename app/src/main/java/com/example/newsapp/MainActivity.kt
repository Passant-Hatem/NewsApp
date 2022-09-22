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
import com.example.newsapp.viewmodels.NewsViewModel
import com.example.newsapp.viewmodels.NewsViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = setupNavController()
        attachNavUIToController(navController)

        val newsRepository = NewsRepo(ArticleDataBase(this))
        val viewModelProviderFactory = NewsViewModelFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

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