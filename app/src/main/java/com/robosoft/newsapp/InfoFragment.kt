package com.robosoft.newsapp

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robosoft.newsapp.data.model.Article
import com.robosoft.newsapp.databinding.FragmentInfoBinding
import com.robosoft.newsapp.databinding.FragmentNewsBinding
import com.robosoft.newsapp.presentation.adapter.NewsAdapter
import com.robosoft.newsapp.presentation.viewmodel.NewsViewModel


class InfoFragment : Fragment() {
    private lateinit var fragmentInfoBinding: FragmentInfoBinding
    private  lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var country = "us"
    private var page = 2
    private var isScrolling = false
    private var isLoading = false
    private var isLastPage = false
    private var pages = 0
    private  var pageSize = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentInfoBinding = FragmentInfoBinding.bind(view)
        val args : InfoFragmentArgs by navArgs()
        val article = args.selectedArticle
        fragmentInfoBinding.wvInfo.apply {
            webViewClient = WebViewClient()
            if(article.url!="") {
                loadUrl(article.url)
                (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
                (activity as AppCompatActivity).supportActionBar?.title = article.url

            }
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    //do what you want here
                   backButton();
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewModel= (activity as MainActivity).viewModel
        newsAdapter= (activity as MainActivity).newsAdapter
        newsAdapter.setOnItemClickListener {
            fragmentInfoBinding.wvInfo.apply {
                webViewClient = WebViewClient()
                if(it.url!="") {
                    loadUrl(it.url)
                    (activity as AppCompatActivity).supportActionBar?.title = it.url
                    fragmentInfoBinding.mainScroll.smoothScrollTo(0,0)
                }
            }
        }
        initRecyclerView()
        viewNewsList()

    }

    private fun backButton() {
        NavHostFragment.findNavController(this).navigateUp();
    }


    private fun viewNewsList() {

        viewModel.getNewsHeadLines(country,page,pageSize)
        viewModel.newsHeadLines.observe(viewLifecycleOwner,{response->
            when(response){
                is com.robosoft.newsapp.data.util.Resource.Success->{
                    hideProgressBar()
                    response.data?.let {
                        Log.i("MYTAG","came here ${it.articles.toList().size}")
                        newsAdapter.differ.submitList(it.articles.toList())

                        if(it.totalResults%10 == 0) {
                            pages = it.totalResults / 10
                        }else{
                            pages = it.totalResults/10+1
                        }
                        isLastPage = page == pages
                    }
                }
                is com.robosoft.newsapp.data.util.Resource.Error->{
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity,"An error occurred : $it", Toast.LENGTH_LONG).show()
                    }
                }

                is com.robosoft.newsapp.data.util.Resource.Loading->{
                    showProgressBar()
                }

            }
        })
    }


    private fun initRecyclerView() {
        // newsAdapter = NewsAdapter()
        fragmentInfoBinding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@InfoFragment.onScrollListener)
        }

    }

    private fun showProgressBar(){
        isLoading = true
        fragmentInfoBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        isLoading = false
        fragmentInfoBinding.progressBar.visibility = View.INVISIBLE
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }

        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = fragmentInfoBinding.rvNews.layoutManager as LinearLayoutManager
            val sizeOfTheCurrentList = layoutManager.itemCount
            val visibleItems = layoutManager.childCount
            val topPosition = layoutManager.findFirstVisibleItemPosition()

            val hasReachedToEnd = topPosition+visibleItems >= sizeOfTheCurrentList
            val shouldPaginate = !isLoading && !isLastPage && hasReachedToEnd && isScrolling
            if(shouldPaginate){
                page++
                viewModel.getNewsHeadLines(country,page,pageSize)
                isScrolling = false

            }

        }


    }


}


