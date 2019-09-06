package com.sirelon.githubapi.feature.search.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sirelon.githubapi.feature.base.BaseViewModel
import com.sirelon.githubapi.feature.search.RepoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created on 2019-09-05 21:29 for GithubAPi.
 */
class SearchRepoViewModel(private val repository: RepoRepository) : BaseViewModel() {

    private val searchTrigger = MutableLiveData<String>()
    val repositoryListLiveData = repository.loadAll()

    fun onSearchTyped(string: String?) {
        string ?: return

        // Post, instead of set will discard all previous data if it not commited
        searchTrigger.postValue(string)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { repository.searchRepositories(string) }
            }

            result.exceptionOrNull()?.let(this@SearchRepoViewModel::onError)
        }
    }
}