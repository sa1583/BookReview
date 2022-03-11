package fastcampus.aop.part2.bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.part2.bookreview.databinding.ItemHistoryBinding
import fastcampus.aop.part2.bookreview.model.History

class HistoryAdapter(val historyDeleteClickListener: (String) -> Unit) :
    ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {
    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyModel: History) {
            binding.historyKeywordTextView.text = historyModel.keyword

            binding.historyKeywordDeleteButton.setOnClickListener {
                historyDeleteClickListener(historyModel.keyword.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.HistoryItemViewHolder {
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }
        }
    }
}