package uz.texnopos.socialnetwork

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.socialnetwork.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val bottomNavigationView = viewBinding.bottomNavView
        val navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.addPostFragment, R.id.profileFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        db.collection("users").document(mAuth.currentUser?.uid!!).get()
            .addOnCompleteListener {
                Log.d("tekseriw", it.result.toString())
                if(it.isSuccessful && !it.result?.exists()!!){
                    val map: MutableMap<String, Any?> = mutableMapOf()
                    map["email"] = mAuth.currentUser?.email.toString()
                    db.collection("users").document(mAuth.currentUser?.uid!!).set(map)
                        .addOnSuccessListener {
                            Log.d("user", "User has been added successfully !")
                        }
                        .addOnFailureListener { e ->
                            Log.d("user", e.localizedMessage!!.toString())
                        }
                }
            }
    }
}