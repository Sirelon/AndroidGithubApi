package com.sirelon.githubapi.feature.repository.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sirelon.githubapi.R
import com.sirelon.githubapi.feature.repository.Repository
import com.sirelon.githubapi.utils.inflate

/**
 * Created on 2019-09-05 22:09 for GithubAPi.
 */
class SavedRepositoryAdapter(private val onItemClick: (repo: Repository) -> Unit) :
    ListAdapter<Repository, RepositoryViewHolder>(RepositoryDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RepositoryViewHolder(parent.inflate(R.layout.item_repository))

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repo = getItem(position)
        holder.bind(repo)
        holder.itemView.setOnClickListener { onItemClick(repo) }
    }
}