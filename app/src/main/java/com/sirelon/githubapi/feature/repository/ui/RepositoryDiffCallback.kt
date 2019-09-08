package com.sirelon.githubapi.feature.repository.ui

import androidx.recyclerview.widget.DiffUtil
import com.sirelon.githubapi.feature.repository.Repository

/**
 * Created on 2019-09-08 14:02 for GithubAPi.
 */
object RepositoryDiffCallback : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean =
        oldItem == newItem
}