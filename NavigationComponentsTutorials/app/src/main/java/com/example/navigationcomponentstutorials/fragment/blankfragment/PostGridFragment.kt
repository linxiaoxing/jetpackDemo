package com.example.navigationcomponentstutorials.fragment.blankfragment

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.navigationcomponentstutorials.R
import com.example.navigationcomponentstutorials.adapter.PostListAdapter
import com.example.navigationcomponentstutorials.databinding.FragmentPostListGridBinding
import com.example.navigationcomponentstutorials.viewmodel.PostsCoroutineViewModel

class PostGridFragment : BaseDataBindingFragment<FragmentPostListGridBinding>() {

    override fun getLayoutRes(): Int = R.layout.fragment_post_list_grid

    private val viewModel by viewModels<PostsCoroutineViewModel>()
    private val listAdapter by lazy {
        PostListAdapter(R.layout.row_post_grid, viewModel::onClick)
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
            this.layoutManager = GridLayoutManager(requireContext(), 3)

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

                /**
                 * This is the navController belong to ViewPagerContainerFragment
                 */

                /**
                 * This is the navController belong to ViewPagerContainerFragment
                 */
                parentFragment?.parentFragment?.findNavController()
                    ?.navigate(R.id.action_view_pager_dest_to_postDetailFragment, bundle)

            }
        })

    }
}