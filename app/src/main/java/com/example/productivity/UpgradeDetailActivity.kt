package com.example.productivity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.backendless.Backendless
import com.example.productivity.databinding.ActivityUpgradeDetailBinding
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault

class UpgradeDetailActivity : AppCompatActivity() {
    companion object{
        val EXTRA_UPGRADE = "upgrade"
        val TAG = "UpgradeDetailActivity"
    }

    private lateinit var binding: ActivityUpgradeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpgradeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val upgrade = intent.getParcelableExtra<Upgrade>(EXTRA_UPGRADE)
        val image = getResources().getIdentifier(upgrade?.image,"drawable",getPackageName())
        binding.textViewUpgradeDetailName.text = upgrade?.name
        binding.textViewUpgradeDetailUpgradeDescription.text=upgrade?.description
        binding.imageViewUpgradeDetailImage.setImageDrawable(AppCompatResources.getDrawable(this, image))
        binding.floatingActionButtonUpgradeDetailExit.setOnClickListener {
            finish()
        }

        binding.buttonUpgradeDetailSelect.setText("Use Bakery")
        val user = Backendless.UserService.CurrentUser()
        if (upgrade?.name.equals("Rundown")){
            if(user.getProperty("background").toString().equals("rundown")){
                binding.buttonUpgradeDetailSelect.isEnabled = false
            }
            else{
                binding.buttonUpgradeDetailSelect.isEnabled = true
            }
        }
        else if (upgrade?.name.equals("Basic")){
            if (user.getProperty("hasBasic") as? Boolean ?: false){
                if(user.getProperty("background").toString().equals("basic")){
                    binding.buttonUpgradeDetailSelect.isEnabled = false
                }
                else{
                    binding.buttonUpgradeDetailSelect.isEnabled = true
                }
            }
            else{
                binding.buttonUpgradeDetailSelect.setText("5000 Gold")
            }
        }
        else if (upgrade?.name.equals("Elevated")){
            if (user.getProperty("hasElevated") as? Boolean ?: false){
                if(user.getProperty("background").toString().equals("elevated")){
                    binding.buttonUpgradeDetailSelect.isEnabled = false
                }
                else{
                    binding.buttonUpgradeDetailSelect.isEnabled = true
                }
            }
            else{
                binding.buttonUpgradeDetailSelect.setText("10000 Gold")
            }
        }
        else if (upgrade?.name.equals("Classy")){
            if (user.getProperty("hasClassy") as? Boolean ?: false){
                if(user.getProperty("background").toString().equals("classy")){
                    binding.buttonUpgradeDetailSelect.isEnabled = false
                }
                else{
                    binding.buttonUpgradeDetailSelect.isEnabled = true
                }
            }
            else{
                binding.buttonUpgradeDetailSelect.setText("20000 Gold")
            }
        }

        //Use Bakery button is clicked
        binding.buttonUpgradeDetailSelect.setOnClickListener {
            if (upgrade?.name.equals("Rundown")){
                saveUser("background", "rundown")
                binding.buttonUpgradeDetailSelect.isEnabled = false
            }
            else if (upgrade?.name.equals("Basic")){
                if (user.getProperty("hasBasic") as? Boolean ?: false){
                    saveUser("background", "basic")
                    binding.buttonUpgradeDetailSelect.isEnabled = false
                }
                else{
                    if ((user.getProperty("currency").toString().toIntOrNull() ?: 0) - 5000 < 0){
                        Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val currentCurrency = user.getProperty("currency") as? Int ?: 0
                        saveUser("currency", currentCurrency - 5000)
                        saveUser("background", "basic")
                        binding.buttonUpgradeDetailSelect.isEnabled = false
                        saveUser("hasBasic", true)
                        Toast.makeText(this, "Purchased Basic Bakery!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (upgrade?.name.equals("Elevated")){
                if (user.getProperty("hasElevated") as? Boolean ?: false){
                    saveUser("background", "elevated")
                    binding.buttonUpgradeDetailSelect.isEnabled = false
                }
                else{
                    if ((user.getProperty("currency").toString().toIntOrNull() ?: 0) - 10000 < 0){
                        Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val currentCurrency = user.getProperty("currency") as? Int ?: 0
                        saveUser("currency", currentCurrency - 10000)
                        saveUser("background", "elevated")
                        binding.buttonUpgradeDetailSelect.isEnabled = false
                        saveUser("hasElevated", true)
                        Toast.makeText(this, "Purchased Elevated Bakery!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (upgrade?.name.equals("Classy")){
                if (user.getProperty("hasClassy") as? Boolean ?: false){
                    saveUser("background", "classy")
                    binding.buttonUpgradeDetailSelect.isEnabled = false
                }
                else{
                    if ((user.getProperty("currency").toString().toIntOrNull() ?: 0) - 20000 < 0){
                        Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val currentCurrency = user.getProperty("currency") as? Int ?: 0
                        saveUser("currency", currentCurrency - 20000)
                        saveUser("background", "classy")
                        binding.buttonUpgradeDetailSelect.isEnabled = false
                        saveUser("hasClassy", true)
                        Toast.makeText(this, "Purchased Classy Bakery!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    //update to Backendless table
    fun saveUser(property: String, value: Any){
        val user = Backendless.UserService.CurrentUser()
        user.setProperty(property, value)

        Backendless.UserService.update(user, object : AsyncCallback<BackendlessUser> {
            override fun handleResponse(updatedUser: BackendlessUser) {
                // User updated successfully
                Log.i("UserUpdate", "User updated: ${updatedUser.email}")
            }
            override fun handleFault(fault: BackendlessFault) {
                // Update failed
                Log.e("UserUpdate", "Failed to update: ${fault.message}")
            }
        })
    }
}