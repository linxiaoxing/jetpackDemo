package com.example.navigationcomponentstutorials.fragment.blankfragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigationcomponentstutorials.R
import com.example.navigationcomponentstutorials.adapter.PostListAdapter
import com.example.navigationcomponentstutorials.databinding.FragmentPostListHorizontalBinding
import com.example.navigationcomponentstutorials.viewmodel.PostsCoroutineViewModel

class PostHorizontalFragment : BaseDataBindingFragment<FragmentPostListHorizontalBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_post_list_horizontal

    private val viewModel by viewModels<PostsCoroutineViewModel>()
    private val listAdapter by lazy {
        PostListAdapter(R.layout.row_post_horizontal, viewModel::onClick)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        val binding = dataBinding!!

        // 🔥 Set lifecycle for data binding
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        binding.recyclerView1.apply {
            // Set Layout manager
            this.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

            // Set RecyclerViewAdapter
            this.adapter = listAdapter
        }

        viewModel.getPosts()
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
                    R.id.action_horizontalPostFragment_to_postDetailFragment,
                    bundle
                )
            }
        })
    }
}