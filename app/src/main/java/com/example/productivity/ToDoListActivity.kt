package com.example.productivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.productivity.databinding.ActivityTodoListBinding


class ToDoListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoListBinding
    private lateinit var adapter: ToDoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.floatingActionButtonToDoListNew.setOnClickListener{
            val intent = Intent(this, ToDoNewActivity::class.java)
            startActivity(intent)
        }


        binding.floatingActionButtonToDoListExit.setOnClickListener{
            finish()
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar_todolist_menu)
        setSupportActionBar(toolbar)

    }

    override fun onStart() {
        super.onStart()
        Log.d("ToDoListActivity", "onStart: ")

        Backendless.UserService.CurrentUser(true, object : AsyncCallback<BackendlessUser?> {
            override fun handleResponse(response: BackendlessUser?) {
                val whereClause = "ownerId = '${response?.userId}'"
                val queryBuilder = DataQueryBuilder.create()
                queryBuilder.setWhereClause(whereClause)

                Backendless.Data.of(ToDo::class.java)
                    .find(queryBuilder, object : AsyncCallback<List<ToDo>> {
                        override fun handleResponse(foundToDoEntry: List<ToDo>) {
                            Log.d("ToDoListActivity", "handleResponse: $foundToDoEntry")
                            adapter = ToDoAdapter(foundToDoEntry.toMutableList())
                            binding.recyclerViewToDoListToDoItems.adapter = adapter
                            binding.recyclerViewToDoListToDoItems.layoutManager =
                                LinearLayoutManager(this@ToDoListActivity)
                        }

                        override fun handleFault(fault: BackendlessFault) {
                            // an error has occurred, the error code can be retrieved with fault.getCode()
                            Log.d("ToDoListActivity", "handleFault: ${fault.message}")
                        }
                    })

            }

            override fun handleFault(fault: BackendlessFault?) {
                // error handling logic
                Log.d("ToDoListActivity", "get current user: handleFault: ${fault?.message}")

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_todolist, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_by_name -> {
                adapter.toDoList = adapter.toDoList.sortedBy { it.name } as MutableList<ToDo>
                adapter.notifyDataSetChanged()
                true
            }
            R.id.sort_by_length -> {
                adapter.toDoList = adapter.toDoList.sortedBy {it.length}.reversed() as MutableList<ToDo>
                adapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}