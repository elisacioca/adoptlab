package com.example.adoptmypet.presentation.feed

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.adoptmypet.R

class FeedActivity : AppCompatActivity() {

    lateinit var adapter: FeedAdapter
    private val viewModel by lazy {
        ViewModelProvider(this).get(FeedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_feed)
        setupAdapter()

        observeEvents()
    }

    private fun observeEvents() {
        viewModel.listOfPets.observe(this, Observer {
            if (it.isNullOrEmpty().not()) {
                adapter = FeedAdapter(viewModel.listOfPets.value ?: emptyList())
            }
        })
     }

    private fun setupAdapter() {
        adapter = FeedAdapter(viewModel.listOfPets.value ?: emptyList())
    }
}
