package uz.texnopos.socialnetwork.ui.comment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.socialnetwork.databinding.ActivityCommentBinding

class CommentActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityCommentBinding
    private val myAdapter = CommentListAdapter()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.rvComment.adapter = myAdapter
        var id = ""
        id = intent.getStringExtra("postId")?: ""
        setComments(id)
    }

    private fun setComments(id: String){
        db.collection("posts").document(id).get()
            .addOnSuccessListener {
                if(it.exists()){
                   it.get("comments")?.let { comments ->
                       myAdapter.models = comments as List<Map<String, String>>
                    }
                }
            }
    }
}