package uz.texnopos.socialnetwork.ui.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import uz.texnopos.socialnetwork.MainActivity
import uz.texnopos.socialnetwork.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityRegisterBinding
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.btnRegister.setOnClickListener {
            if(viewBinding.etRegisterEmail.text.toString().isNotEmpty() && viewBinding.etRegisterPassword.text.toString().isNotEmpty()){
                viewBinding.loading.visibility = View.VISIBLE
                mAuth.createUserWithEmailAndPassword(viewBinding.etRegisterEmail.text.toString(), viewBinding.etRegisterPassword.text.toString())
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            val user = mAuth.currentUser
                            viewBinding.loading.visibility = View.GONE
                            updateUI(user)
                        } else {
                            Toast.makeText(this, "Authentication is failed !", Toast.LENGTH_LONG).show()
                            viewBinding.loading.visibility = View.GONE
                        }
                    }
            }
             else {
                Toast.makeText(this, "Please enter the email and password !", Toast.LENGTH_SHORT).show()
            }
        }
        viewBinding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}