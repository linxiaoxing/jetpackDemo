package com.example.navigationcomponentstutorials.fragment.blankfragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.navigationcomponentstutorials.R
import com.example.navigationcomponentstutorials.adapter.PostListAdapter
import com.example.navigationcomponentstutorials.databinding.FragmentPostListStaggeredBinding
import com.example.navigationcomponentstutorials.viewmodel.PostsCoroutineViewModel

class PostStaggeredFragment : BaseDataBindingFragment<FragmentPostListStaggeredBinding>() {


    override fun getLayoutRes(): Int = R.layout.fragment_post_list_staggered

    private val viewModel by viewModels<PostsCoroutineViewModel>()
    private val listAdapter by lazy {
        PostListAdapter(R.layout.row_post_staggered, viewModel::onClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getPosts()

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

        binding.recyclerView.apply {

            // Set Layout manager
            this.layoutManager =
                StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)

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

                /*
         🔥 This is navController we get from findNavController not the one required
         for navigating nested fragments
      */
                requireActivity().findNavController(R.id.mainNavHostFragment)
                    .navigate(R.id.action_mainFragment_to_postDetailFragment, bundle)

//                val mainNavController =
//                    Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment)
//                mainNavController.navigate(R.id.action_mainFragment_to_postDetailFragment, bundle)
            }
        })

    }


}