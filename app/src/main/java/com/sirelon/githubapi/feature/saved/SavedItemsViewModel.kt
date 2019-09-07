package com.sirelon.githubapi.feature.saved

import com.sirelon.githubapi.feature.base.BaseViewModel
import com.sirelon.githubapi.feature.repository.RepoRepository

class SavedItemsViewModel(private val repository: RepoRepository) : BaseViewModel() {
    val allRepositories = repository.loadAll()
}