package uz.texnopos.socialnetwork.ui.post

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.socialnetwork.R
import uz.texnopos.socialnetwork.data.PostModel
import uz.texnopos.socialnetwork.databinding.FragmentGetPostBinding
import uz.texnopos.socialnetwork.ui.comment.CommentActivity
import uz.texnopos.socialnetwork.ui.comment.adding.AddCommentActivity

class GetPostFragment : Fragment(R.layout.fragment_get_post) {

    private lateinit var viewBinding: FragmentGetPostBinding
    private val myAdapter = PostAdapter()
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentGetPostBinding.bind(view)
        myAdapter.setOnItemClickListener {
            val intent = Intent(requireContext(), CommentActivity::class.java)
            intent.putExtra("postId", it.id)
            startActivity(intent)
        }
        myAdapter.setOnCommentClickListener {
            val intent = Intent(requireContext(), AddCommentActivity::class.java)
            intent.putExtra("PostId", it.id)
            startActivity(intent)
        }

        myAdapter.setOnLike {
            db.collection("users").document(mAuth.currentUser!!.uid).get()
                .addOnSuccessListener { doc ->
                    val likes:MutableMap<String, Boolean> = doc.get("likes") as MutableMap<String, Boolean>
                    if(likes.containsKey(it.id)){
                        if(likes[it.id] == false){
                            likes[it.id] = true
                            db.collection("users").document(mAuth.currentUser!!.uid).update("likes", likes)
                            postLiked(it)
                        } else {
                            likes[it.id] = false
                            db.collection("users").document(mAuth.currentUser!!.uid).update("likes", likes)
                            decreasePostLiked(it)
                        }
                    } else {
                        likes[it.id] = true
                        db.collection("users").document(mAuth.currentUser!!.uid).update("likes", likes)
                        postLiked(it)
                    }
                }
        }

        viewBinding.rvPost.adapter = myAdapter
        getAllPost()
    }

    private fun postLiked(postModel: PostModel){
        db.collection("posts").document(postModel.id).get()
            .addOnSuccessListener {
                val likeCount = it.get("likes") as Long
                db.collection("posts").document(postModel.id).update("likes", likeCount + 1)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Siz bul postqa like bastiniz", Toast.LENGTH_LONG).show()
                    }
            }
    }

    private fun decreasePostLiked(postModel: PostModel){
        db.collection("posts").document(postModel.id).get()
            .addOnSuccessListener {
                val likeCount = it.get("likes") as Long
                db.collection("posts").document(postModel.id).update("likes", likeCount - 1)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Siz bul postqa like bastiniz", Toast.LENGTH_LONG).show()
                    }
            }
    }

    private fun getAllPost(){
        val result: MutableList<PostModel> = mutableListOf()
        db.collection("posts").addSnapshotListener { value, error ->
            if(error != null){
                Toast.makeText(requireContext(), error.message.toString(), Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            result.clear()
            db.collection("posts").get().addOnSuccessListener {
                it.documents.forEach { doc ->
                    val model = doc.toObject(PostModel::class.java)
                    model?.id = doc.id
                    model?.let {
                        result.add(model)
                    }
                }
                myAdapter.models = result
                Log.d("magliwmat", result.toString())
            }
        }
    }
}