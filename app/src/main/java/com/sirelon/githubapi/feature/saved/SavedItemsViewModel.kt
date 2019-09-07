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

    fun updatePriorityForList(currentList: MutableList<Repository>) {
        //TODO: Find way to update not whole list, but only items, which were changed
        viewModelScope.launch(Dispatchers.IO) {
            // Just set for ech item its position on the list
            currentList.forEachIndexed { index, item -> item.priority = index + 1 }
            repository.updateRepositoriesList(currentList)
        }
    }
}