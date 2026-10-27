package com.example.productivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.productivity.databinding.ActivitySleepTrackerNewBinding
import java.util.Calendar

class SleepTrackerNewActivity : AppCompatActivity() {
    companion object {
        val TAG = "SleepTrackerNewActivity"
        val EXTRA_SLEEPTRACKER_ENTRY = "sleeptracker entry"
    }

    private lateinit var binding: ActivitySleepTrackerNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySleepTrackerNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //ideas from: https://www.youtube.com/watch?v=uSLUPF-xsyQ
        val sleepTrackerEntry = intent.getParcelableExtra(EXTRA_SLEEPTRACKER_ENTRY) ?: SleepTracker()
        val date = findViewById(R.id.datePicker_sleepTrackerNew_dateEntry) as DatePicker
        val calendar: Calendar = Calendar.getInstance()

        date.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
            {view, year, monthOfYear, dayOfMonth -> Toast.makeText(this, "" + date.month+"/"+date.dayOfMonth+"/"+date.year, Toast.LENGTH_SHORT).show()})


        binding.buttonSleepTrackerNewSave.setOnClickListener {
            val year = date.year
            val month = date.month
            val day = date.dayOfMonth

            if (binding.editTextSleepTrackerNewBedtimeEntry.text.toString().isEmpty() or binding.editTextSleepTrackerNewTimeEntry.text.toString().isEmpty() or binding.editTextSleepTrackerNewJournalEntry.text.toString().isEmpty()){
                Toast.makeText(this, "Please fill out all forms", Toast.LENGTH_SHORT).show()
            }
            else{
                sleepTrackerEntry.ownerId = Backendless.UserService.CurrentUser().userId
                sleepTrackerEntry.date = "$month/$day/$year"
                sleepTrackerEntry.bedtime = "Went to sleep at " + binding.editTextSleepTrackerNewBedtimeEntry.text.toString()
                if(binding.editTextSleepTrackerNewTimeEntry.text.toString().equals("0") or binding.editTextSleepTrackerNewTimeEntry.text.toString().equals("1")){
                    sleepTrackerEntry.time = binding.editTextSleepTrackerNewTimeEntry.text.toString() + " hour"
                }
                else{
                    sleepTrackerEntry.time = binding.editTextSleepTrackerNewTimeEntry.text.toString() + " hours"
                }
                sleepTrackerEntry.journal = binding.editTextSleepTrackerNewJournalEntry.text.toString()
                saveToBackendless(sleepTrackerEntry)
                finish()
            }
        }

        binding.floatingActionButtonSleepTrackerNewExit.setOnClickListener{
            finish()
        }

    }

    private fun saveToBackendless(sleepTrackerEntry: SleepTracker) {
        // code here to save to backendless
        Backendless.Data.of(SleepTracker::class.java).save(sleepTrackerEntry, object : AsyncCallback<SleepTracker?> {
            override fun handleResponse(response: SleepTracker?) {
                Toast.makeText(this@SleepTrackerNewActivity, "${sleepTrackerEntry} has been successfully created", Toast.LENGTH_SHORT).show()
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d("SleepTrackerNewActivity", "handleFault: ${fault.message}")
            }
        })
    }

}