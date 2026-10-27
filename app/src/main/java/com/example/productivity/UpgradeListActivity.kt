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
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.productivity.databinding.ActivityUpgradeListBinding

class UpgradeListActivity : AppCompatActivity() {

    companion object{
        val TAG = "UpgradeListActivity"
    }

    private lateinit var binding: ActivityUpgradeListBinding
    private lateinit var adapter: UpgradeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpgradeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var gson = Gson()
        val inputStream = resources.openRawResource(R.raw.upgrade)
        val jsonString = inputStream.bufferedReader().use {
            it.readText()
        }
        val type = object : TypeToken<List<Upgrade>>() { }.type
        val upgradeList = gson.fromJson<List<Upgrade>>(jsonString, type)
        Log.d(TAG, "onCreate: $upgradeList")

        adapter = UpgradeAdapter(upgradeList)
        binding.recyclerViewUpgradeUpgrades.adapter = adapter
        binding.recyclerViewUpgradeUpgrades.layoutManager = LinearLayoutManager(this)

        binding.floatingActionButtonUpgradeExit.setOnClickListener{
            finish()
        }

        // make backendless call to retrieve all data
        val userId = intent.getStringExtra(LoginActivity.EXTRA_USER_ID)
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.setWhereClause(whereClause)

        Backendless.Data.of(Upgrade::class.java)
            .find(queryBuilder, object : AsyncCallback<List<Upgrade>> {
                override fun handleResponse(foundUpgradeEntry: List<Upgrade>) {
                    Log.d("UpgradeListActivity", "handleResponse: $foundUpgradeEntry")
                    adapter = UpgradeAdapter(foundUpgradeEntry.toMutableList())
                    binding.recyclerViewUpgradeUpgrades.adapter = adapter
                    binding.recyclerViewUpgradeUpgrades.layoutManager =
                        LinearLayoutManager(this@UpgradeListActivity)

                }

                override fun handleFault(fault: BackendlessFault) {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                    Log.d("UpgradeListActivity", "handleFault: ${fault.message}")
                }
            })
    }
}