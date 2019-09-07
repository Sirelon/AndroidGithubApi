package com.sirelon.githubapi.feature.saved

import androidx.lifecycle.viewModelScope
import com.sirelon.githubapi.feature.base.BaseViewModel
import com.sirelon.githubapi.feature.repository.RepoRepository
import com.sirelon.githubapi.feature.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedItemsViewModel(private val repository: RepoRepository) : BaseViewModel() {
    val allRepositories = repository.loadAll()

    fun removeItem(item: Repository) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.remove(item)
        }
    }
}