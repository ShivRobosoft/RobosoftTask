package com.robosoft.newsapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robosoft.newsapp.data.model.Article
import com.robosoft.newsapp.databinding.FragmentNewsBinding
import com.robosoft.newsapp.presentation.adapter.NewsAdapter
import com.robosoft.newsapp.presentation.viewmodel.NewsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : Fragment() {
    private  lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var fragmentNewsBinding: FragmentNewsBinding
    private var country = "us"
    private var page = 1
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
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNewsBinding = FragmentNewsBinding.bind(view)
        viewModel= (activity as MainActivity).viewModel
        newsAdapter= (activity as MainActivity).newsAdapter
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("selected_article",it)
            }
            findNavController().navigate(
                R.id.action_newsFragment_to_infoFragment,
                bundle
            )
        }

        (activity as AppCompatActivity).supportActionBar?.title ="Robosoft News"


        initRecyclerView()
        viewNewsList()

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
                        val articels = it.articles.toList()

                        if(fragmentNewsBinding.tvTitle.text.isEmpty()){
                            doLayoutChanges(articels.get(0))
                        }

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

    private fun doLayoutChanges(article: Article) {
         fragmentNewsBinding.tvTitle.text = article.title
         fragmentNewsBinding.tvDescription.text =article.description
         fragmentNewsBinding.tvSource.text =article.source.name
         Glide.with(fragmentNewsBinding.ivArticleImage.context).
         load(article.urlToImage).
         into(fragmentNewsBinding.ivArticleImage)
    }

    private fun initRecyclerView() {
        // newsAdapter = NewsAdapter()
        fragmentNewsBinding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@NewsFragment.onScrollListener)
        }

    }

    private fun showProgressBar(){
        isLoading = true
        fragmentNewsBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        isLoading = false
        fragmentNewsBinding.progressBar.visibility = View.INVISIBLE
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
            val layoutManager = fragmentNewsBinding.rvNews.layoutManager as LinearLayoutManager
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