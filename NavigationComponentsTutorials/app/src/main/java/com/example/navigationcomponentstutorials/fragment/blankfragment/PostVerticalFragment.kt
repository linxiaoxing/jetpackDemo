package com.example.navigationcomponentstutorials.fragment.blankfragment

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigationcomponentstutorials.R
import com.example.navigationcomponentstutorials.adapter.PostListAdapter
import com.example.navigationcomponentstutorials.databinding.FragmentPostListVerticalBinding
import com.example.navigationcomponentstutorials.viewmodel.PostsCoroutineViewModel

class PostVerticalFragment : BaseDataBindingFragment<FragmentPostListVerticalBinding>() {


    override fun getLayoutRes(): Int = R.layout.fragment_post_list_vertical

    private val viewModel by viewModels<PostsCoroutineViewModel>()

    private val listAdapter by lazy {
        PostListAdapter(R.layout.row_post_vertical, viewModel::onClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        viewModel.getPosts()
    }


    private fun bindViews() {

        val binding = dataBinding!!

        // 🔥 Set lifecycle for data binding
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        binding.recyclerView.apply {
            // Set Layout manager
            this.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            // Set RecyclerViewAdapter
            this.adapter = listAdapter
        }

        subscribePostViewState()
        subscribeGoToDetailScreen()
    }

    private fun subscribePostViewState() {
        viewModel.postViewState.observe(viewLifecycleOwner, Observer {
            viewModel.postViewState.value?.let {
                listAdapter.submitList(it.data)
            }
        })
    }
    private fun subscribeGoToDetailScreen() {

        viewModel.goToDetailScreen.observe(viewLifecycleOwner, Observer {
          it.getContentIfNotHandled()?.let { post ->
              val bundle = bundleOf("post" to post)
              findNavController().navigate(
                  R.id.action_verticalPostFragment_to_postDetailFragment,
                  bundle
              )
          }
        })
    }
}