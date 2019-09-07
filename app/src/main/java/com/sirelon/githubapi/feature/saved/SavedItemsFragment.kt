package com.sirelon.githubapi.feature.saved

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sirelon.githubapi.R
import com.sirelon.githubapi.feature.base.BaseFragment
import com.sirelon.githubapi.feature.repository.ui.RepositoryAdapter
import com.sirelon.githubapi.utils.openBrowser
import kotlinx.android.synthetic.main.fragment_search_repositories.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedItemsFragment : BaseFragment(R.layout.fragment_saved_items) {

    private val viewModel by viewModel<SavedItemsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subsribeForErrors(viewModel)

        val searchAdapter = RepositoryAdapter {
            activity?.openBrowser(it.url)
        }
        with(repositoriesList) {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = searchAdapter
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
        }

        viewModel.allRepositories.observe(this, searchAdapter::submitList)
    }

}