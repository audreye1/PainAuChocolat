package com.example.productivity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.productivity.databinding.ActivityFocusSetupBinding

class FocusSetUpActivity  : AppCompatActivity(){
    private lateinit var binding: ActivityFocusSetupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFocusSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.floatingActionButtonFocusSetupExit.setOnClickListener {
            finish()
        }

        binding.buttonFocusStart.setOnClickListener {
            var input = binding.editTextFocusSetupTimeEntry.text.toString()
            var isInt = input.toIntOrNull()
            if (isInt != null && input.isNotEmpty() && isInt != 0) {
                var time = input.toLong()
                if (isInt <= 1){
                    Toast.makeText(this, "Focusing for " + time + " minute!", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Focusing for " + time + " minutes!", Toast.LENGTH_SHORT).show()
                }

                time *= 60000

                val EXTRA_LONG_ID = "HOURS_TO_FOCUS"

                val intent = Intent(this, FocusActivity::class.java)
                intent.putExtra(EXTRA_LONG_ID, time) // Attach the Long input
                startActivity(intent)

            } else if (isInt == null || isInt == 0) {
                Toast.makeText(this, "Please enter a positive integer", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a time", Toast.LENGTH_SHORT).show()
            }
        }
    }}
