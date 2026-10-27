package com.example.productivity

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.productivity.databinding.ActivityTodoNewBinding

class ToDoNewActivity : AppCompatActivity() {
    companion object {
        val TAG = "ToDoNewActivity"
        val EXTRA_TODO_ENTRY = "todo entry"
    }

    private lateinit var binding: ActivityTodoNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTodoNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toDoEntry = intent.getParcelableExtra(EXTRA_TODO_ENTRY) ?: ToDo()

        var spinnerItems = ToDo.EMOJI.entries.map { it.emoji }
        binding.spinnerToDoNewLength.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        var position = spinnerItems.indexOf(toDoEntry.length)
        if(position < 0) {
            position = 0
        }
        binding.spinnerToDoNewLength.setSelection(position)

        binding.buttonToDoNewSave.setOnClickListener {
            if(binding.editTextToDoNewNameEntry.text.toString().isEmpty()){
                Toast.makeText(this, "Please name your task", Toast.LENGTH_SHORT).show()
            }
            else{
                toDoEntry.ownerId = Backendless.UserService.CurrentUser().userId
                toDoEntry.name = binding.editTextToDoNewNameEntry.text.toString()
                toDoEntry.length = ToDo.EMOJI.entries.find { it.emoji == binding.spinnerToDoNewLength.selectedItem.toString()}!!.name
                saveToBackendless(toDoEntry)
                finish()
            }
        }

        binding.floatingActionButtonToDoNewExit.setOnClickListener{
            finish()
        }
    }

    private fun saveToBackendless(toDoEntry: ToDo) {
        // code here to save to backendless
        Backendless.Data.of(ToDo::class.java).save(toDoEntry, object : AsyncCallback<ToDo?> {
            override fun handleResponse(response: ToDo?) {
                Toast.makeText(this@ToDoNewActivity, "${toDoEntry} has been successfully created", Toast.LENGTH_SHORT).show()
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d("ToDoNewActivity", "handleFault: ${fault.message}")
            }
        })
    }

}