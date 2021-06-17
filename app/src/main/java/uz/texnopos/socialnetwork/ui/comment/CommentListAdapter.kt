package uz.texnopos.socialnetwork.ui.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.socialnetwork.databinding.ItemCommentBinding

class CommentListAdapter: RecyclerView.Adapter<CommentListAdapter.CommentListViewHolder>() {

    inner class CommentListViewHolder(private val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun populateModel(comment: Map<String, String>) {
            binding.tvComment.text = comment["comment_text"]
            binding.tvUsername.text = "@${comment["username"]}"
        }
    }

    var models: List<Map<String, String>> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListViewHolder {
        val itemBinding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CommentListViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int {
        return models.size
    }

}