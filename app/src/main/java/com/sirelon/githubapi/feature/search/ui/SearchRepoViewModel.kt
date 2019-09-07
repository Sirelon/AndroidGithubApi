package com.sirelon.githubapi.feature.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.sirelon.githubapi.feature.base.BaseViewModel
import com.sirelon.githubapi.feature.repository.Repository
import com.sirelon.githubapi.feature.search.RepoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created on 2019-09-05 21:29 for GithubAPi.
 */
class SearchRepoViewModel(private val repository: RepoRepository) : BaseViewModel() {

    private val searchTrigger = MutableLiveData<String>()
    //    val repositoryListLiveData = repository.loadAll()
    val repositoryListLiveData: LiveData<List<Repository>> = searchTrigger.switchMap {
        liveData<List<Repository>>(Dispatchers.IO) {
            val result = runCatching { repository.searchRepositories(it) }
            val list = result.getOrNull()
            if (list != null) {
                emit(list)
            } else {
                result.exceptionOrNull()?.let(this@SearchRepoViewModel::onError)
            }
        }
    }

    fun onSearchTyped(string: String?) {
        string ?: return

        // Post, instead of set will discard all previous data if it not commited
        searchTrigger.postValue(string)

//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                runCatching { repository.searchRepositories(string) }
//            }
//
//            result.exceptionOrNull()?.let(this@SearchRepoViewModel::onError)
//        }
    }

    fun markAsViewed(item: Repository) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markAsViewed(item)
        }
    }
}