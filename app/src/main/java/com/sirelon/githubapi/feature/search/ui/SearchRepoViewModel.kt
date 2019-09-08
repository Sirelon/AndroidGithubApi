package com.sirelon.githubapi.feature.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sirelon.githubapi.feature.base.BaseViewModel
import com.sirelon.githubapi.feature.repository.RepoRepository
import com.sirelon.githubapi.feature.repository.Repository
import com.sirelon.githubapi.feature.search.SearchDataSourceFactory
import com.sirelon.githubapi.feature.search.SearchRepository
import com.sirelon.githubapi.utils.throttle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

/**
 * Created on 2019-09-05 21:29 for GithubAPi.
 */
class SearchRepoViewModel(
    private val searchRepository: SearchRepository,
    private val itemsRepository: RepoRepository
) : BaseViewModel() {

    private val searchQueryChannel: SendChannel<String>

    val repositoryListLiveData: LiveData<PagedList<Repository>>

    init {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(30)
            .setPrefetchDistance(1)
            .build()

        val dataSourceFactory = SearchDataSourceFactory(searchRepository, this::onError)

        repositoryListLiveData = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
            .build()

        searchQueryChannel = Channel(Channel.CONFLATED)

        viewModelScope.launch {
            searchQueryChannel
                .throttle()
                .consumeEach { dataSourceFactory.update(it) }
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
}