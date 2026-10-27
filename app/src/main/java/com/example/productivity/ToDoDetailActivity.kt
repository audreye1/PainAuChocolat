package com.example.productivity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.productivity.databinding.ActivityTodoDetailBinding

class ToDoDetailActivity: AppCompatActivity() {
    companion object{
        val EXTRA_TODO = "toDo"
    }
    private lateinit var binding: ActivityTodoDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toDo = intent.getParcelableExtra<ToDo>(EXTRA_TODO)
        binding.textViewToDoDetailName.text = toDo?.name
        if(toDo?.length.equals("SHORT")){
            binding.textViewToDoDetailLength.text = ToDo.EMOJI.valueOf("SHORT").emoji.replace("Short ", "")
        }
        else if(toDo?.length.equals("MEDIUM")){
            binding.textViewToDoDetailLength.text = ToDo.EMOJI.valueOf("MEDIUM").emoji.replace("Medium ", "")
        }
        else if(toDo?.length.equals("LONG")){
            binding.textViewToDoDetailLength.text = ToDo.EMOJI.valueOf("LONG").emoji.replace("Long ", "")
        }
        binding.floatingActionButtonToDoDetailExit.setOnClickListener {
            finish()
        }
        binding.buttonToDoDetailComplete.setOnClickListener {
            Toast.makeText(this, "Keep it up!", Toast.LENGTH_SHORT).show()
            if(toDo?.length.equals("SHORT")){
                updateCurrency(50, object : AsyncCallback<BackendlessUser> {
                    override fun handleResponse(response: BackendlessUser) {
                        response.getProperty("currency")
                        deleteFromBackendless()
                        finish()
                    }
                    override fun handleFault(fault: BackendlessFault) {
                        Log.d("ToDoDetailActivity", "Error adding currency short")
                    }
                })
            }
            else if(toDo?.length.equals("MEDIUM")){
                updateCurrency(100, object : AsyncCallback<BackendlessUser> {
                    override fun handleResponse(response: BackendlessUser) {
                        response.getProperty("currency")
                        deleteFromBackendless()
                        finish()
                    }
                    override fun handleFault(fault: BackendlessFault) {
                        Log.d("ToDoDetailActivity", "Error adding currency medium")
                    }
                })
            }
            else if(toDo?.length.equals("LONG")){
                updateCurrency(200, object : AsyncCallback<BackendlessUser> {
                    override fun handleResponse(response: BackendlessUser) {
                        response.getProperty("currency")
                        deleteFromBackendless()
                        finish()
                    }
                    override fun handleFault(fault: BackendlessFault) {
                        Log.d("ToDoDetailActivity", "Error adding currency long")
                    }
                })
            }
        }
    }

    fun updateCurrency(amountChange: Int, callback: AsyncCallback<BackendlessUser>) {
        val user = Backendless.UserService.CurrentUser()
        val currentCurrency = user.getProperty("currency") as? Int ?: 0
        val newTotal = currentCurrency + amountChange
        user.setProperty("currency", newTotal)
        Backendless.UserService.update(user, callback)
    }

    private fun deleteFromBackendless() {
        Log.d("ToDoDetailActivity", "deleteFromBackendless: Trying to delete EXTRA_TODO")
        val currentToDo = intent.getParcelableExtra<ToDo>(EXTRA_TODO)
        Backendless.Data.of(ToDo::class.java).remove(currentToDo, object : AsyncCallback<Long> {
            override fun handleResponse(response: Long) {
                finish()
            }
            override fun handleFault(fault: BackendlessFault) {
                Log.e("Backendless", "Error: ${fault.message}")
            }
        })
    }

}