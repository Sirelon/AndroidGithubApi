package com.sirelon.githubapi.feature.repository.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sirelon.githubapi.feature.repository.Repository
import com.sirelon.githubapi.utils.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_repository.*
import java.util.Collections


/**
 * Created on 2019-09-07 14:56 for GithubAPi.
 */
class SavedItemsAdapter(private val onItemClick: (repo: Repository) -> Unit) :
    RecyclerView.Adapter<SavedItemsAdapter.ViewHolder>() {

    private val items = mutableListOf<Repository>()

    fun getCurrentList() = items.toList()

    fun submitList(list: List<Repository>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(com.sirelon.githubapi.R.layout.item_saved_repository))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = getItem(position)
        with(holder) {
            itemView.setOnClickListener { onItemClick(repo) }
            repoName.text = repo.name
            repoDescription.text = repo.description
            repoStars.text = "${repo.starCount}"
        }
    }

    fun getItem(position: Int) = items[position]

    class ViewHolder(override val containerView: View) : LayoutContainer,
        RecyclerView.ViewHolder(containerView)

}