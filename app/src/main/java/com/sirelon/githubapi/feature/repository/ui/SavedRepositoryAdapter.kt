package com.sirelon.githubapi.feature.repository.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sirelon.githubapi.R
import com.sirelon.githubapi.feature.repository.Repository
import com.sirelon.githubapi.utils.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_repository.*

/**
 * Created on 2019-09-05 22:09 for GithubAPi.
 */
class SavedRepositoryAdapter(private val onItemClick: (repo: Repository) -> Unit) :
    ListAdapter<Repository, SavedRepositoryAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.item_repository))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = getItem(position) ?: return
        with(holder) {
            itemView.setOnClickListener { onItemClick(repo) }
            repoName.text = repo.name
            repoDescription.text = repo.description
            repoStars.text = "${repo.starCount}"
        }
    }

    class ViewHolder(override val containerView: View) : LayoutContainer,
        RecyclerView.ViewHolder(containerView)
}

private object DiffCallback : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean =
        oldItem == newItem
}
