package com.sirelon.githubapi.feature.saved

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sirelon.githubapi.feature.base.BaseFragment
import com.sirelon.githubapi.feature.search.ui.SearchRepositoryAdapter
import com.sirelon.githubapi.utils.hideKeyboard
import com.sirelon.githubapi.utils.openBrowser
import kotlinx.android.synthetic.main.fragment_search_repositories.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs


class SavedItemsFragment : BaseFragment(com.sirelon.githubapi.R.layout.fragment_saved_items) {

    private val viewModel by viewModel<SavedItemsViewModel>()
    private val searchAdapter = SearchRepositoryAdapter {
        activity?.openBrowser(it.url)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.hideKeyboard()

        subsribeForErrors(viewModel)

        with(repositoriesList) {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()

            // Drag&drop, swipeToDelete interactions.
            val itemTouchCallback = createItemTouchCallback()
            val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }

        viewModel.allRepositories.observe(this, searchAdapter::submitList)
    }

    private fun createItemTouchCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(
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

                //FIXME: Incorect reording when drag fast
                currentList[position] = itemTarget
                currentList[positionTarget] = item
                searchAdapter.submitList(currentList)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = searchAdapter.currentList.getOrNull(position) ?: return
                viewModel.removeItem(item)
                // TODO: Show some snackbar with "undo"
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                // Here we will update ordering on database
                viewModel.updatePriorityForList(searchAdapter.currentList)
            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                actionState: Int, isCurrentlyActive: Boolean
            ) {

                val itemView = viewHolder.itemView

                // TODO: highlight somehow item, which is dragging

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val width = itemView.width.toFloat()
                    val alpha = 1.0f - abs(dX) / width
                    itemView.alpha = alpha
                    itemView.translationX = dX
                } else {
                    super.onChildDraw(
                        c, recyclerView, viewHolder, dX, dY,
                        actionState, isCurrentlyActive
                    )
                }
            }
        }
    }
}