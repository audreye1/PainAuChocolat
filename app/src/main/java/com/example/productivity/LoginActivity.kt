package com.example.productivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.productivity.databinding.ActivityLoginBinding
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault

class LoginActivity :  AppCompatActivity() {
    companion object{
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
        val EXTRA_USER_ID = "userid"
        val EXTRA_CURRENCY = "currency"
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val SavedInstanceState = null
        super.onCreate(SavedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButtonLoginExit.setOnClickListener{
            finish()
        }

        //register with backendless
        Backendless.initApp(this, Constants.APP_ID, Constants.API_KEY)
        binding.buttonLoginLogin.setOnClickListener{
            val username = binding.editTextLoginUsername.text.toString()
            val password = binding.editTextLoginPassword.text.toString()

            Backendless.UserService.login(
                username,
                password,
                object : AsyncCallback<BackendlessUser?> {
                    override fun handleResponse(user: BackendlessUser?) {
                        Toast.makeText(this@LoginActivity,
                            "${Backendless.UserService.CurrentUser().getProperty("name")} has logged in", Toast.LENGTH_LONG). show()
                        val restaurantIntent = Intent(this@LoginActivity, RestaurantActivity::class.java)
                        restaurantIntent.putExtra(EXTRA_USER_ID, user?.userId)
                        val currentCurrency = Backendless.UserService.CurrentUser().getProperty("currency") as? Int ?: 0
                        restaurantIntent.putExtra(EXTRA_CURRENCY,currentCurrency)
                        startActivity(restaurantIntent)
                        finish()
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        if (fault != null) {
                            Toast.makeText(this@LoginActivity,
                                "${fault.message}", Toast.LENGTH_LONG).show()
                        }
                        if (fault != null) {
                            Log.d("LoginActivity", "handleFault: ${fault.message}")
                        }
                    }
                }
            )
        }
    }
}