package com.sirelon.githubapi.feature.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.sirelon.githubapi.feature.base.BaseViewModel
import com.sirelon.githubapi.feature.repository.RepoRepository
import com.sirelon.githubapi.feature.repository.Repository
import com.sirelon.githubapi.feature.search.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * Created on 2019-09-05 21:29 for GithubAPi.
 */
class SearchRepoViewModel(
    private val searchRepository: SearchRepository,
    private val itemsRepository: RepoRepository
) : BaseViewModel() {

    // just debug flag for which approach to use: flow or simple suspend methods with async
    val searchViaFlow = true

    private val searchTrigger = MutableLiveData<String>()

    val repositoryListLiveData: LiveData<List<Repository>> =
        searchTrigger.switchMap(this::searchByQuery)

    // I need to handle errors, so convertion to LiveData I have implemeneted in ViewModel, not in repository
    private fun searchByQuery(it: String): LiveData<List<Repository>> {
        if (searchViaFlow) {
            return searchRepository
                .searchViaFlows(it)
                .catch { onError(it) }
                .asLiveData()
        } else {
            return liveData {
                try {
                    val list = searchRepository.searchViaAsync(it)
                    emit(list)
                } catch (e: Exception) {
                    onError(e)
                    MutableLiveData<List<Repository>>()
                }
            }
        }
    }

    fun onSearchTyped(string: String?) {
        string ?: return

        // Post, instead of set will discard all previous data if it not commited
        searchTrigger.postValue(string)

//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                runCatching { searchRepository.searchRepositories(string) }
//            }
//
//            result.exceptionOrNull()?.let(this@SearchRepoViewModel::onError)
//        }
    }

    fun markAsViewed(item: Repository) {
        viewModelScope.launch(Dispatchers.IO) {
            itemsRepository.markAsViewed(item)
        }
    }
}