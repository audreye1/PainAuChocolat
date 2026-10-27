package com.example.productivity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.productivity.ToDoDetailActivity.Companion.EXTRA_TODO
import com.example.productivity.databinding.ActivitySleepTrackerDetailBinding
import com.example.productivity.databinding.ActivityTodoDetailBinding

class SleepTrackerDetailActivity : AppCompatActivity() {
    companion object{
        val EXTRA_SLEEPTRACKER = "sleepTracker"
        val TAG = "SleepTrackerDetailActivity"
    }
    private lateinit var binding: ActivitySleepTrackerDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySleepTrackerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sleepTracker = intent.getParcelableExtra<SleepTracker>(EXTRA_SLEEPTRACKER)
        binding.textViewSleepTrackerDetailDate.text = sleepTracker?.date
        binding.textViewSleepTrackerDetailTime.text = sleepTracker?.time
        binding.textViewSleepTrackerDetailJournal.text = sleepTracker?.journal
        binding.textViewSleepTrackerDetailBedtime.text = sleepTracker?.bedtime
        binding.floatingActionButtonSleepTrackerDetailExit.setOnClickListener {
            finish()
        }
    }
}