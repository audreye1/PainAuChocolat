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
import com.example.productivity.databinding.ActivityAvatarListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AvatarListActivity : AppCompatActivity() {
    companion object{
        val TAG = "AvatarListActivity"
    }
    private lateinit var binding: ActivityAvatarListBinding
    private lateinit var adapter: AvatarAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAvatarListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var gson = Gson()
        val inputStream = resources.openRawResource(R.raw.avatars)
        val jsonString = inputStream.bufferedReader().use {
            it.readText()
        }
        val type = object : TypeToken<List<Avatar>>() { }.type
        val avatarList = gson.fromJson<List<Avatar>>(jsonString, type)
        Log.d(TAG, "onCreate: $avatarList")

        adapter = AvatarAdapter(avatarList)
        binding.recyclerViewAvatarListAvatars.adapter = adapter
        binding.recyclerViewAvatarListAvatars.layoutManager = LinearLayoutManager(this)

        binding.floatingActionButtonAvatarListExit.setOnClickListener{
            finish()
        }

        // make backendless call to retrieve all data
        val userId = intent.getStringExtra(LoginActivity.EXTRA_USER_ID)
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.setWhereClause(whereClause)

        Backendless.Data.of(Avatar::class.java)
            .find(queryBuilder, object : AsyncCallback<List<Avatar>> {
                override fun handleResponse(foundAvatarEntry: List<Avatar>) {
                    Log.d("AvatarListActivity", "handleResponse: $foundAvatarEntry")
                    adapter = AvatarAdapter(foundAvatarEntry.toMutableList())
                    binding.recyclerViewAvatarListAvatars.adapter = adapter
                    binding.recyclerViewAvatarListAvatars.layoutManager =
                        LinearLayoutManager(this@AvatarListActivity)

                }

                override fun handleFault(fault: BackendlessFault) {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                    Log.d("AvatarListActivity", "handleFault: ${fault.message}")
                }
            })
    }
}