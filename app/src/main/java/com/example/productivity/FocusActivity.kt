package com.example.productivity

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.backendless.Backendless
import com.example.productivity.databinding.ActivityFocusBinding


class FocusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFocusBinding
    var background: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFocusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.floatingActionButtonFocusExit.setOnClickListener {
            finish()
        }

        val hours = intent.getLongExtra("HOURS_TO_FOCUS",0)

        object : CountDownTimer(hours, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                var s = seconds.toString()
                if (seconds < 10) {
                    s = "0$s"
                }
                binding.textViewFocusTimer.text = minutes.toString() + ":" + s
            }

            override fun onFinish() {
                binding.textViewFocusTimer.text = "Done!"
                binding.textViewFocusMotivation.text = "You did it!!!"

                //update background
                val user = Backendless.UserService.CurrentUser()
                if(user.getProperty("avatar").equals("chef")){
                    if(background.equals("plain_dough_chef")){
                        binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this@FocusActivity, getResources().getIdentifier("plain_baked_chef","drawable",getPackageName())))
                    }
                    else if(background.equals("choc_dough_chef")){
                        binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this@FocusActivity, getResources().getIdentifier("choc_baked_chef","drawable",getPackageName())))
                    }
                    else if(background.equals("straw_dough_chef")){
                        binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this@FocusActivity, getResources().getIdentifier("straw_baked_chef","drawable",getPackageName())))
                    }
                }
                else if(user.getProperty("avatar").equals("frog")){
                    Log.d("focus", "hello it worked")
                    if(background.equals("plain_dough_frog")){
                        binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this@FocusActivity, getResources().getIdentifier("plain_baked_frog","drawable",getPackageName())))
                    }
                    else if(background.equals("choc_dough_frog")){
                        binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this@FocusActivity, getResources().getIdentifier("choc_baked_frog","drawable",getPackageName())))
                    }
                    else if(background.equals("straw_dough_frog")){
                        binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this@FocusActivity, getResources().getIdentifier("straw_baked_frog","drawable",getPackageName())))
                    }
                }
                else if(user.getProperty("avatar").equals("french")){
                    if(background.equals("plain_dough_french")){
                        binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this@FocusActivity, getResources().getIdentifier("plain_baked_french","drawable",getPackageName())))
                    }
                    else if(background.equals("choc_dough_french")){
                        binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this@FocusActivity, getResources().getIdentifier("choc_baked_french","drawable",getPackageName())))
                    }
                    else if(background.equals("straw_dough_french")){
                        binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this@FocusActivity, getResources().getIdentifier("straw_baked_french","drawable",getPackageName())))
                    }
                }
                else if(user.getProperty("avatar").equals("sir")){
                    if(background.equals("plain_dough_dapper")){
                        binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this@FocusActivity, getResources().getIdentifier("plain_baked_dapper","drawable",getPackageName())))
                    }
                    else if(background.equals("choc_dough_dapper")){
                        binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this@FocusActivity, getResources().getIdentifier("choc_baked_dapper","drawable",getPackageName())))
                    }
                    else if(background.equals("straw_dough_dapper")){
                        binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this@FocusActivity, getResources().getIdentifier("straw_baked_dapper","drawable",getPackageName())))
                    }
                }
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        val user = Backendless.UserService.CurrentUser()

        //check avatar --> change initial background
        if(user.getProperty("avatar").equals("chef")){
            val list = listOf("plain_dough_chef", "choc_dough_chef", "straw_dough_chef")
            val image = list.random()
            binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this, getResources().getIdentifier("$image","drawable",getPackageName())))
            background = image
        }
        else if(user.getProperty("avatar").equals("frog")){
            val list = listOf("plain_dough_frog", "choc_dough_frog", "straw_dough_frog")
            val image = list.random()
            binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this, getResources().getIdentifier("$image","drawable",getPackageName())))
            background = image
        }
        else if(user.getProperty("avatar").equals("french")){
            val list = listOf("plain_dough_french", "choc_dough_french", "straw_dough_french")
            val image = list.random()
            binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this, getResources().getIdentifier("$image","drawable",getPackageName())))
            background = image
        }
        else if(user.getProperty("avatar").equals("sir")){
            val list = listOf("plain_dough_dapper", "choc_dough_dapper", "straw_dough_dapper")
            val image = list.random()
            binding.imageViewFocusBackground.setImageDrawable(AppCompatResources.getDrawable(this, getResources().getIdentifier("$image","drawable",getPackageName())))
            background = image
        }

    }
}