package uz.texnopos.socialnetwork.ui.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.socialnetwork.R
import uz.texnopos.socialnetwork.data.PostModel
import uz.texnopos.socialnetwork.databinding.FragmentAddPostBinding
import uz.texnopos.socialnetwork.databinding.FragmentProfileBinding
import java.text.DateFormat
import java.util.*

class AddPostFragment : Fragment(R.layout.fragment_add_post) {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var viewBinding: FragmentAddPostBinding
    private val calendar = Calendar.getInstance()
    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentAddPostBinding.bind(view)

        binding = FragmentProfileBinding.bind(LayoutInflater.from(requireActivity())
            .inflate(R.layout.fragment_profile, null, false))

        viewBinding.btnSend.setOnClickListener {
            setLoading(true)
            if(!viewBinding.etPostText.text.isNullOrEmpty()){
                val map: MutableMap<String, Any?> = mutableMapOf()
                val createdAt = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.time)
                map["userId"] = mAuth.currentUser?.uid!!.toString()
                map["text"] = viewBinding.etPostText.text.toString()
                map["likes"] = 0
                map["dislikes"] = 0
                map["comments"] = arrayListOf<String>()
                map["createdAt"] = createdAt
                map["theme"] = viewBinding.etPostTheme.text.toString()
                db.collection("users").document(mAuth.currentUser?.uid!!.toString()).get()
                    .addOnSuccessListener {
                        map["username"] = it.get("username")
                        Log.d("paydalaniwshi", it.get("username").toString())
                        db.collection("posts").document().set(map)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Your post is published !", Toast.LENGTH_SHORT).show()
                                setLoading(false)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), e.localizedMessage!!.toString(), Toast.LENGTH_LONG).show()
                                setLoading(false)
                            }
                    }
            }
        }
    }

    private fun setLoading(isLoading: Boolean){
        viewBinding.loading.visibility = if(isLoading) View.VISIBLE else View.GONE
        viewBinding.etPostText.isEnabled = !isLoading
        viewBinding.btnSend.isEnabled = !isLoading
    }

}