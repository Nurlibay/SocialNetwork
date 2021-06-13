package uz.texnopos.socialnetwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import uz.texnopos.socialnetwork.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityLoginBinding
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.btnLogin.setOnClickListener {
            if(viewBinding.etLoginEmail.text.toString().isNotEmpty() && viewBinding.etLoginPassword.text.toString().isNotEmpty()){
                viewBinding.loading.visibility = View.VISIBLE
                mAuth.signInWithEmailAndPassword(viewBinding.etLoginEmail.text.toString(), viewBinding.etLoginPassword.text.toString())
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            viewBinding.loading.visibility = View.GONE
                            val currentUser = mAuth.currentUser
                            updateUI(currentUser)
                        } else {
                            Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                            viewBinding.loading.visibility = View.GONE
                            //updateUI(null)
                        }
                    }
            }
            else {
                Toast.makeText(this, "Please enter the email and password !", Toast.LENGTH_SHORT).show()
            }
        }
        viewBinding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}