package uz.texnopos.socialnetwork.ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.socialnetwork.data.PostModel
import uz.texnopos.socialnetwork.databinding.ListItemBinding

class PostAdapter: RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(private val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(postModel: PostModel){
            binding.tvPostTheme.text = postModel.theme
            binding.tvUsername.text = postModel.username
            binding.tvPostText.text = postModel.text
            binding.tvLikeCount.text = postModel.likes.toString()
            binding.tvDislikeCount.text = postModel.dislikes.toString()
            binding.root.setOnClickListener {
                onItemClicked.invoke(postModel)
            }
            binding.ivComment.setOnClickListener {
                onCommentClicked.invoke(postModel)
            }
            binding.likeImage.setOnClickListener {
                onLike.invoke(postModel)
            }
            binding.dislikeImage.setOnClickListener {
                onDislike.invoke(postModel)
            }
        }
    }

    private var onLike: (postModel: PostModel) -> Unit = {}
    fun setOnLike(onLike: (postModel: PostModel) -> Unit){
        this.onLike = onLike
    }

    private var onDislike: (postModel: PostModel) -> Unit = {}
    fun setOnDislike(onDislike: (postModel: PostModel) -> Unit){
        this.onDislike = onDislike
    }

    private var onCommentClicked: (postModel: PostModel) -> Unit = {}
    fun setOnCommentClickListener(onCommentClicked: (postModel: PostModel) -> Unit){
        this.onCommentClicked = onCommentClicked
    }

    private var onItemClicked: (postModel: PostModel) -> Unit = {}
    fun setOnItemClickListener(onItemClicked: (postModel: PostModel) -> Unit){
        this.onItemClicked = onItemClicked
    }

    var models: List<PostModel> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int {
        return models.size
    }
}