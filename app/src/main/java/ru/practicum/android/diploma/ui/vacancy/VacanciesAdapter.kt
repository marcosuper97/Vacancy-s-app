package ru.practicum.android.diploma.ui.vacancy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.LoadingItemBinding
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.domain.models.VacanciesPreview
import ru.practicum.android.diploma.util.LoadingItem
import ru.practicum.android.diploma.util.RecyclerViewConstants

class VacanciesAdapter(private val onVacancyClicked: (String) -> Unit) :
    ListAdapter<Any, RecyclerView.ViewHolder>(VacancyDiffCallback()) {

    var isLoadingNextPage: Boolean = false

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is VacanciesPreview -> RecyclerViewConstants.ITEM_TYPE_VACANCY
            is LoadingItem -> RecyclerViewConstants.ITEM_TYPE_LOADING
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecyclerViewConstants.ITEM_TYPE_VACANCY -> {
                val binding = VacancyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VacanciesViewHolder(binding)
            }
            RecyclerViewConstants.ITEM_TYPE_LOADING -> {
                val binding = LoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VacanciesViewHolder -> {
                val vacancy = getItem(position) as? VacanciesPreview ?: return //  безопасное приведение и проверка
                holder.bind(vacancy)
                holder.itemView.setOnClickListener {
                    onVacancyClicked(vacancy.vacancyId)
                }
            }
            is LoadingViewHolder -> {
                holder.bind(isLoadingNextPage)
            }
        }
    }

    class LoadingViewHolder(val binding: LoadingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(isLoading: Boolean) {
            binding.loadingProgressBar.isVisible = isLoading
        }
    }

    class VacancyDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is VacanciesPreview && newItem is VacanciesPreview -> oldItem.vacancyId == newItem.vacancyId
                oldItem is LoadingItem && newItem is LoadingItem -> true
                else -> false
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }
    }
}
