package com.sirelon.githubapi.feature.saved

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.sirelon.githubapi.R
import com.sirelon.githubapi.feature.base.BaseFragment
import com.sirelon.githubapi.feature.repository.ui.SavedRepositoryAdapter
import com.sirelon.githubapi.utils.hideKeyboard
import com.sirelon.githubapi.utils.openBrowser
import kotlinx.android.synthetic.main.fragment_saved_items.*
import kotlinx.android.synthetic.main.fragment_search_repositories.repositoriesList
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs


class SavedItemsFragment : BaseFragment(com.sirelon.githubapi.R.layout.fragment_saved_items) {

    private val viewModel by viewModel<SavedItemsViewModel>()
    private val savedAdapter = SavedRepositoryAdapter {
        activity?.openBrowser(it.url)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.hideKeyboard()

        subsribeForErrors(viewModel)

        with(repositoriesList) {
            layoutManager = LinearLayoutManager(context)
            adapter = savedAdapter
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()

            // Drag&drop, swipeToDelete interactions.
            val itemTouchCallback = createItemTouchCallback()
            val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }

        emptyView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_search_repo))

        viewModel.allRepositories.observe(this) {
            TransitionManager.beginDelayedTransition(savedRoot)
            if (it.isEmpty()) {
                emptyView.visibility = View.VISIBLE
            } else {
                emptyView.visibility = View.GONE
            }
            savedAdapter.submitList(it)
        }
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
                val currentList = savedAdapter.currentList.toMutableList()
                val item = currentList.getOrNull(position) ?: return false

                val positionTarget = target.adapterPosition
                val itemTarget = currentList.getOrNull(positionTarget) ?: return false

                //FIXME: Incorect reording when drag fast
                currentList[position] = itemTarget
                currentList[positionTarget] = item
                savedAdapter.submitList(currentList)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = savedAdapter.currentList.getOrNull(position) ?: return
                viewModel.removeItem(item)
                // TODO: Show some snackbar with "undo"
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                // Here we will update ordering on database
                viewModel.updatePriorityForList(savedAdapter.currentList)
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