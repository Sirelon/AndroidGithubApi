package com.sirelon.githubapi.feature.search.ui

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.sirelon.githubapi.R
import com.sirelon.githubapi.feature.repository.Repository
import com.sirelon.githubapi.feature.repository.ui.RepositoryDiffCallback
import com.sirelon.githubapi.feature.repository.ui.RepositoryViewHolder
import com.sirelon.githubapi.utils.inflate

/**
 * Created on 2019-09-05 22:09 for GithubAPi.
 */
class SearchRepositoryPagedAdapter(private val onItemClick: (repo: Repository) -> Unit) :
    PagedListAdapter<Repository, RepositoryViewHolder>(RepositoryDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RepositoryViewHolder(parent.inflate(R.layout.item_repository))

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repo = getItem(position) ?: return
        holder.bind(repo)
        holder.itemView.setOnClickListener { onItemClick(repo) }
    }
}
