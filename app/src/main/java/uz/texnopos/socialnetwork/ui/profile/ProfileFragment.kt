package uz.texnopos.socialnetwork.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.socialnetwork.R
import uz.texnopos.socialnetwork.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var viewBinding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentProfileBinding.bind(view)
        showData()
        viewBinding.btnSave.setOnClickListener {
            setLoading(true)
            val map: MutableMap<String, Any?> = mutableMapOf()

            map["username"] = viewBinding.etUserName.text.toString()

            map["email"] = viewBinding.etEmailAddress.text.toString()
            map["phone"] = viewBinding.etPhoneNumber.text.toString()
            map["information"] = viewBinding.etInformation.text.toString()
            db.collection("users").document(mAuth.currentUser!!.uid).set(map)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Your profile data has been changed successfully !", Toast.LENGTH_SHORT).show()
                    setLoading(false)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                    setLoading(false)
                }
        }
    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading) viewBinding.loading.visibility = View.VISIBLE
        else viewBinding.loading.visibility = View.GONE
        viewBinding.etUserName.isEnabled = !isLoading
        viewBinding.etPhoneNumber.isEnabled = !isLoading
        viewBinding.etEmailAddress.isEnabled = !isLoading
        viewBinding.etInformation.isEnabled = !isLoading
        viewBinding.btnSave.isEnabled = !isLoading
    }

    private fun showData(){
        setLoading(true)
        db.collection("users").document(mAuth.currentUser?.uid!!).get()
            .addOnSuccessListener {
                viewBinding.etUserName.setText(it.get("username").toString())
                viewBinding.etEmailAddress.setText(it.get("email").toString())
                viewBinding.etPhoneNumber.setText(it.get("phone").toString())
                viewBinding.etInformation.setText(it.get("information").toString())
                setLoading(false)
            }
    }
    
}