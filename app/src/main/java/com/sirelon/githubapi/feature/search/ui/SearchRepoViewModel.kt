package com.sirelon.githubapi.feature.search.ui

import android.util.Log
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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

/**
 * Created on 2019-09-05 21:29 for GithubAPi.
 */
class SearchRepoViewModel(
    searchRepository: SearchRepository,
    private val itemsRepository: RepoRepository
) : BaseViewModel() {

    val repositoryListLiveData: LiveData<PagedList<Repository>>

    // to this channel will be posted typed by user keywords for search
    private val searchKeywordChannel: SendChannel<String>

    // This Job used for stopping search. I pass it to pagination datasource, where on scope of this job api would be called.
    // And if this job would be cancelled, all its children will be cancelled as well.
    // BUT. As this Job is Supervisor (see https://kotlinlang.org/docs/reference/coroutines/exception-handling.html#supervision ) -- it would not be cancelled if any of it's children throws exception
    private val searchParentJob = SupervisorJob()

    init {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(30)
            .setPrefetchDistance(1)
            .build()

        val contextWithErrorHandling =
        // Run api calls in IO
            // Actually, it's not really needed, 'cause pagination already has mechanism for run its operation in parallel. see setFetchExecutor on LivePagedListBuilder
            Dispatchers.IO +
                    // parent job for ability to cancel children jobs
                    searchParentJob +
                    // Exception handler.
                    CoroutineExceptionHandler { _, throwable ->
                        onError(throwable)
                    }

        // Create dataSource factory
        val dataSourceFactory =
            SearchDataSourceFactory(searchRepository, CoroutineScope(contextWithErrorHandling))

        // Create LiveData with congif and datasource
        repositoryListLiveData = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()

        searchKeywordChannel = Channel(Channel.CONFLATED)

        // Listening all typed strings
        viewModelScope.launch {
            searchKeywordChannel
                // this is util method, which use "window" mechanism: collect all items, which were passed by some time, and post back only the most recent
                .throttle()
                .consumeEach { dataSourceFactory.update(it) }
        }
    }

    fun onSearchTyped(string: String?) {
        string ?: return

        // Non blocking. Post to channel
        searchKeywordChannel.offer(string)
    }

    fun markAsViewed(item: Repository) {
        viewModelScope.launch(Dispatchers.IO) {
            itemsRepository.markAsViewed(item)
        }
    }

    fun stopSearch() {
        if (!searchParentJob.isCancelled)
            searchParentJob.cancel()
    }

    override fun onCleared() {
        super.onCleared()

        Log.i("Sirelon", "SearchRepo: Oncleared")
        stopSearch()
        searchKeywordChannel.close()
    }
}