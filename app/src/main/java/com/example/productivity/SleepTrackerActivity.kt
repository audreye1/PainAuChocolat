package com.example.productivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.productivity.databinding.ActivitySleepTrackerBinding
import kotlin.collections.toMutableList

class SleepTrackerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySleepTrackerBinding
    private lateinit var adapter: SleepTrackerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySleepTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.floatingActionButtonSleepTrackerNew.setOnClickListener{
            val intent = Intent(this, SleepTrackerNewActivity::class.java)
            startActivity(intent)
        }
        binding.floatingActionButtonSleepTrackerExit.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("SleepTrackerActivity", "onStart: ")

        Backendless.UserService.CurrentUser(true, object : AsyncCallback<BackendlessUser?> {
            override fun handleResponse(response: BackendlessUser?) {
                val whereClause = "ownerId = '${response?.userId}'"
                val queryBuilder = DataQueryBuilder.create()
                queryBuilder.setWhereClause(whereClause)

                Backendless.Data.of(SleepTracker::class.java)
                    .find(queryBuilder, object : AsyncCallback<List<SleepTracker>> {
                        override fun handleResponse(foundSleepTrackerEntry: List<SleepTracker>) {
                            Log.d("SleepTrackerActivity", "handleResponse: $foundSleepTrackerEntry")
                            adapter = SleepTrackerAdapter(foundSleepTrackerEntry.toMutableList())
                            binding.recyclerViewSleepTrackerSleepTrackerItems.adapter = adapter
                            binding.recyclerViewSleepTrackerSleepTrackerItems.layoutManager =
                                LinearLayoutManager(this@SleepTrackerActivity)
                        }

                        override fun handleFault(fault: BackendlessFault) {
                            // an error has occurred, the error code can be retrieved with fault.getCode()
                            Log.d("SleepTrackerActivity", "handleFault: ${fault.message}")
                        }
                    })

            }

            override fun handleFault(fault: BackendlessFault?) {
                Log.d("SleepTrackerActivity", "get current user: handleFault: ${fault?.message}")

            }
        })
    }

}