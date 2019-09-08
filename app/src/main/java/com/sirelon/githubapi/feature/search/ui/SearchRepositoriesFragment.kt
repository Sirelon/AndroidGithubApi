package com.sirelon.githubapi.feature.search.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.sirelon.githubapi.R
import com.sirelon.githubapi.feature.base.BaseFragment
import com.sirelon.githubapi.utils.onTextChange
import kotlinx.android.synthetic.main.fragment_search_repositories.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchRepositoriesFragment : BaseFragment(R.layout.fragment_search_repositories) {

    private val viewModel by viewModel<SearchRepoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subsribeForErrors(viewModel)

        val searchAdapter =
            SearchRepositoryPagedAdapter(viewModel::markAsViewed)
        with(repositoriesList) {
            layoutManager = GridLayoutManager(context, 2)
            adapter = searchAdapter
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
        }

        viewModel.repositoryListLiveData.observe(this, searchAdapter::submitList)
        searchInput.editText?.onTextChange {
            viewModel.onSearchTyped(it?.toString())
        }
    }
}