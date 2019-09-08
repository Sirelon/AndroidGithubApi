package com.sirelon.githubapi.feature.search

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.sirelon.githubapi.feature.repository.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created on 2019-09-07 23:40 for GithubAPi.
 */
class SearchDataSourceFactory(
    private val searchRepository: SearchRepository,
    private val onError: (error: Throwable) -> Unit
) :
    DataSource.Factory<Int, Repository>() {

    private val searchCriteria: SearchCriteria = SearchCriteria("", 1, ByType.TITLE, BySort.STARS)

    private var dataSource = dataSource(searchRepository, searchCriteria)

    override fun create(): DataSource<Int, Repository> = dataSource

    fun update(search: String) {
        // Not need to invalidate if query params the same
        if (search == searchCriteria.searchQuery) return

        dataSource.invalidate()
        dataSource =
            dataSource(searchRepository, searchCriteria.copy(searchQuery = search, page = 0))
    }

    private fun dataSource(searchRepository: SearchRepository, searchCriteria: SearchCriteria) =
        SearchGroupDataSource(searchRepository, searchCriteria, onError)
}

class SearchGroupDataSource(
    private val searchRepository: SearchRepository,
    private val searchCriteria: SearchCriteria,
    private val onError: (error: Throwable) -> Unit
) : PositionalDataSource<Repository>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Repository>) {
        changeCriteria(params.startPosition, params.loadSize)

        Log.i("Sirelon", "loadRange ${searchCriteria.page}")
        // TODO
        GlobalScope.launch {
            val list = searchRepository.searchByCriteria(searchCriteria)
            callback.onResult(list)
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Repository>) {
        changeCriteria(params.requestedStartPosition, params.pageSize)
        Log.i("Sirelon", "loadInitial ${searchCriteria.page}")
        // TODO
        GlobalScope.launch {
            val list = searchRepository.searchByCriteria(searchCriteria)
            callback.onResult(list, 0)
        }
    }

    private fun changeCriteria(startPosition: Int, requestedPageSize: Int) {
        // Server start page calculation from 1, not from 0.
        searchCriteria.page = (startPosition / requestedPageSize) + 1
        // 'cause we merge two streams by 15 items on each, so for server send half of requested size for each request
        searchCriteria.pageLimit = requestedPageSize / 2
    }
}
