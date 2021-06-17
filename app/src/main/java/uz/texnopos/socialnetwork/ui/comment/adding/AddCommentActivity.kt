package uz.texnopos.socialnetwork.ui.comment.adding

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.socialnetwork.data.PostModel
import uz.texnopos.socialnetwork.databinding.ActivityAddCommentBinding

class AddCommentActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityAddCommentBinding
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private var postId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddCommentBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        postId = intent.getStringExtra("PostId")?: ""
        viewBinding.btnAddComment.setOnClickListener {
            if(!viewBinding.etComment.text.isNullOrEmpty()){
                viewBinding.loading.visibility = View.VISIBLE
                db.collection("posts").document(postId).get()
                    .addOnSuccessListener {
                        if(it.exists()){
                            val post = it.toObject(PostModel::class.java)
                            var username = ""
                            db.collection("users").document(mAuth.currentUser!!.uid).get()
                                .addOnSuccessListener { user ->
                                    username = user.get("username").toString()
                                    post?.comments?.add(mapOf("username" to username, "comment_text" to viewBinding.etComment.text.toString()))

                                    val newPost = mutableMapOf<String, Any?>()
                                    newPost["id"] = post?.id
                                    newPost["theme"] = post?.theme
                                    newPost["username"] = post?.username
                                    newPost["text"] = post?.text
                                    newPost["likes"] = post?.likes
                                    newPost["dislikes"] = post?.dislikes
                                    newPost["userId"] = post?.userId
                                    newPost["comments"] = post?.comments

                                    db.collection("posts").document(postId).set(newPost)
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "Pikriniz qosildi", Toast.LENGTH_LONG).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(this, "Pikrinizdi qosiwda qatelik juz berdi", Toast.LENGTH_LONG).show()
                                        }
                                        .addOnCompleteListener {
                                            viewBinding.loading.visibility = View.GONE
                                            finish()
                                        }
                                }
                        }
                    }
            }
        }
    }
}