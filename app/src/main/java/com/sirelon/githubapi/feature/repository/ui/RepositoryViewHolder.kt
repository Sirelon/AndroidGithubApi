package com.sirelon.githubapi.feature.repository.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sirelon.githubapi.feature.repository.Repository
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_repository.*

/**
 * Created on 2019-09-08 14:03 for GithubAPi.
 */
class RepositoryViewHolder(override val containerView: View) : LayoutContainer,
    RecyclerView.ViewHolder(containerView) {

    fun bind(repo: Repository?) {
        // if repo null, it means that we are going to show placeholder for item.
        repo ?: return
        repoName.text = repo.name
        repoDescription.text = repo.description
        repoStars.text = "${repo.starCount}"
    }

}