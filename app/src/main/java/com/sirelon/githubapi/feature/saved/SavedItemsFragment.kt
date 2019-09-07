package com.sirelon.githubapi.feature.saved

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sirelon.githubapi.feature.base.BaseFragment
import com.sirelon.githubapi.feature.search.ui.SearchRepositoryAdapter
import com.sirelon.githubapi.utils.hideKeyboard
import com.sirelon.githubapi.utils.openBrowser
import kotlinx.android.synthetic.main.fragment_search_repositories.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SavedItemsFragment : BaseFragment(com.sirelon.githubapi.R.layout.fragment_saved_items) {

    private val viewModel by viewModel<SavedItemsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.hideKeyboard()

        subsribeForErrors(viewModel)

        val searchAdapter = SearchRepositoryAdapter {
            activity?.openBrowser(it.url)
        }

        val itemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                ItemTouchHelper.END
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    val position = viewHolder.adapterPosition
                    val currentList = searchAdapter.currentList.toMutableList()
                    val item = currentList.getOrNull(position) ?: return false

                    val positionTarget = target.adapterPosition
                    val itemTarget = currentList.getOrNull(positionTarget) ?: return false

                    currentList[position] = itemTarget
                    currentList[positionTarget] = item
//
                    searchAdapter.submitList(currentList)

//                    searchAdapter.notifyItemMoved(position, positionTarget)

//                    viewModel.changePriorityForItem(item)

                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val item = searchAdapter.currentList.getOrNull(position) ?: return
                    viewModel.removeItem(item)
                }
            }

        with(repositoriesList) {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
            setHasFixedSize(true)
//            itemAnimator = DefaultItemAnimator()

            val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }

        viewModel.allRepositories.observe(this, searchAdapter::submitList)
    }

}