package uz.texnopos.socialnetwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.texnopos.socialnetwork.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}