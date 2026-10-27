package com.example.productivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.backendless.Backendless
import com.example.productivity.databinding.ActivityRestaurantBinding

class RestaurantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.imageViewRestaurantAvatar.setOnClickListener{
            val intent = Intent(this, AvatarListActivity::class.java)
            startActivity(intent)
        }

        binding.imageViewRestaurantList.setOnClickListener{
            val intent = Intent(this, ToDoListActivity::class.java)
            startActivity(intent)
        }

        binding.imageViewRestaurantUpgrade.setOnClickListener {
            val intent = Intent(this, UpgradeListActivity::class.java)
            startActivity(intent)
        }

        binding.buttonRestaurantFocus.setOnClickListener {
            val intent = Intent(this, FocusSetUpActivity::class.java)
            startActivity(intent)
        }

        binding.imageViewRestaurantSleepTracker.setOnClickListener {
            val intent = Intent(this, SleepTrackerActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        val user = Backendless.UserService.CurrentUser()
        Log.d("restaurant", (user.getProperty("background").toString()))
        binding.textViewRestaurantCurrency.setText(" Currency: " + user.getProperty("currency") + " ")
        if (user.getProperty("background").equals("rundown")){
            binding.imageViewRestaurantBackground.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rundown_exterior))
        }
        else if (user.getProperty("background").equals("basic")){
            binding.imageViewRestaurantBackground.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.basic_exterior))
        }
        else if (user.getProperty("background").equals("elevated")){
            binding.imageViewRestaurantBackground.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.elevated_exterior))
        }
        else if (user.getProperty("background").equals("classy")){
            binding.imageViewRestaurantBackground.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.classy_exterior))
        }
    }
}

