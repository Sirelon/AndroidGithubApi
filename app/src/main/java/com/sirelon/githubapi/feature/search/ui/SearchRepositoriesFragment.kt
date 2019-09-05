package com.sirelon.githubapi.feature.search.ui

import android.os.Bundle
import android.view.View
import com.sirelon.githubapi.R
import com.sirelon.githubapi.feature.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchRepositoriesFragment : BaseFragment(R.layout.fragment_search_repositories) {

    private val viewModel by viewModel<SearchRepoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}