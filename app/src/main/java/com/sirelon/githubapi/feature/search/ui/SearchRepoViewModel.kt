package com.sirelon.githubapi.feature.search.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sirelon.githubapi.feature.base.BaseViewModel
import com.sirelon.githubapi.feature.repository.RepoRepository
import com.sirelon.githubapi.feature.repository.Repository
import com.sirelon.githubapi.feature.search.SearchRepository
import com.sirelon.githubapi.utils.throttle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created on 2019-09-05 21:29 for GithubAPi.
 */
// just debug flag for which approach to use: flow or simple suspend methods with async
private const val SEARCH_VIA_FLOW = true

class SearchRepoViewModel(
    private val searchRepository: SearchRepository,
    private val itemsRepository: RepoRepository
) : BaseViewModel() {

    private val searchQueryChannel: SendChannel<String>

    val repositoryListLiveData = MutableLiveData<List<Repository>>()

    init {
        searchQueryChannel = Channel(Channel.CONFLATED)

        viewModelScope.launch {
            searchQueryChannel
                .throttle()
                .consumeEach { searchByQuery(it) }
        }
    }

    fun onSearchTyped(string: String?) {
        string ?: return

        // Post, instead of set will discard all previous data if it not commited
        searchQueryChannel.offer(string)
    }

    fun markAsViewed(item: Repository) {
        viewModelScope.launch(Dispatchers.IO) {
            itemsRepository.markAsViewed(item)
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchQueryChannel.close()
    }

    // I need to handle errors, so choose how need to load data is here, in ViewModel, not in repository
    private suspend fun searchByQuery(query: String) {
        if (SEARCH_VIA_FLOW) {
            return searchRepository
                .searchViaFlows(query)
                .catch { onError(it) }
                .collect { repositoryListLiveData.postValue(it) }
        } else {
            val result = runCatching { searchRepository.searchViaAsync(query) }
            result.exceptionOrNull()?.let(this::onError)
            result.getOrNull()?.let(repositoryListLiveData::postValue)
        }
    }
}