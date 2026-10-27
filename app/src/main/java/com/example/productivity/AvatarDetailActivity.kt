package com.example.productivity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.productivity.databinding.ActivityAvatarDetailBinding

class AvatarDetailActivity : AppCompatActivity() {
    companion object{
        val EXTRA_AVATAR = "avatar"
        val TAG = "AvatarDetailActivity"
    }

    private lateinit var binding: ActivityAvatarDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvatarDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val avatar = intent.getParcelableExtra<Avatar>(EXTRA_AVATAR)
        val image = getResources().getIdentifier(avatar?.image,"drawable",getPackageName())
        binding.textViewAvatarActivityName.text = avatar?.name
        binding.textViewAvatarActivityAvatarDescription.text=avatar?.description
        binding.imageViewAvatarActivityImage.setImageDrawable(AppCompatResources.getDrawable(this, image))
        binding.floatingActionButtonAvatarExit.setOnClickListener {
            finish()
        }

        binding.buttonAvatarDetailSelect.setText("Select Avatar")
        val user = Backendless.UserService.CurrentUser()
        if (avatar?.name.equals("Chef Remy")){
            if(user.getProperty("avatar").toString().equals("chef")){
                binding.imageViewAvatarActivityImage.setImageDrawable(AppCompatResources.getDrawable(this, getResources().getIdentifier("chef_happy","drawable",getPackageName())))
                binding.buttonAvatarDetailSelect.isEnabled = false
            }
            else{
                binding.buttonAvatarDetailSelect.isEnabled = true
            }
        }
        else if (avatar?.name.equals("Petite Ribbíte")){
            if (user.getProperty("hasFrog") as? Boolean ?: false){
                if(user.getProperty("avatar").toString().equals("frog")){
                    binding.imageViewAvatarActivityImage.setImageDrawable(AppCompatResources.getDrawable(this, getResources().getIdentifier("frog_happy","drawable",getPackageName())))
                    binding.buttonAvatarDetailSelect.isEnabled = false
                }
                else{
                    binding.buttonAvatarDetailSelect.isEnabled = true
                }
            }
            else{
                binding.buttonAvatarDetailSelect.setText("5000 Gold")
            }
        }
        else if (avatar?.name.equals("Oui Oui Baguette")){
            if (user.getProperty("hasFrench") as? Boolean ?: false){
                if(user.getProperty("avatar").toString().equals("french")){
                    binding.imageViewAvatarActivityImage.setImageDrawable(AppCompatResources.getDrawable(this, getResources().getIdentifier("french_happy","drawable",getPackageName())))
                    binding.buttonAvatarDetailSelect.isEnabled = false
                }
                else{
                    binding.buttonAvatarDetailSelect.isEnabled = true
                }
            }
            else{
                binding.buttonAvatarDetailSelect.setText("10000 Gold")
            }
        }
        else if (avatar?.name.equals("Monsieur Quaso")){
            if (user.getProperty("hasSir") as? Boolean ?: false){
                if(user.getProperty("avatar").toString().equals("sir")){
                    binding.imageViewAvatarActivityImage.setImageDrawable(AppCompatResources.getDrawable(this, getResources().getIdentifier("sir_happy","drawable",getPackageName())))
                    binding.buttonAvatarDetailSelect.isEnabled = false
                }
                else{
                    binding.buttonAvatarDetailSelect.isEnabled = true
                }
            }
            else{
                binding.buttonAvatarDetailSelect.setText("20000 Gold")
            }
        }

        //Select button is pressed
        binding.buttonAvatarDetailSelect.setOnClickListener {
            if (avatar?.name.equals("Chef Remy")){
                saveUser("avatar", "chef")
                binding.imageViewAvatarActivityImage.setImageDrawable(AppCompatResources.getDrawable(this, getResources().getIdentifier("chef_happy","drawable",getPackageName())))
                binding.buttonAvatarDetailSelect.isEnabled = false
            }
            else if (avatar?.name.equals("Petite Ribbíte")){
                if (user.getProperty("hasFrog") as? Boolean ?: false){
                    saveUser("avatar", "frog")
                    binding.imageViewAvatarActivityImage.setImageDrawable(AppCompatResources.getDrawable(this, getResources().getIdentifier("frog_happy","drawable",getPackageName())))
                    binding.buttonAvatarDetailSelect.isEnabled = false
                }
                else{
                    if ((user.getProperty("currency").toString().toIntOrNull() ?: 0) - 5000 < 0){
                        Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val currentCurrency = user.getProperty("currency") as? Int ?: 0
                        saveUser("currency", currentCurrency - 5000)
                        saveUser("avatar", "frog")
                        binding.buttonAvatarDetailSelect.isEnabled = false
                        saveUser("hasFrog", true)
                        Toast.makeText(this, "Selected Petite Ribbíte!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (avatar?.name.equals("Oui Oui Baguette")){
                if (user.getProperty("hasFrench") as? Boolean ?: false){
                    saveUser("avatar", "french")
                    binding.imageViewAvatarActivityImage.setImageDrawable(AppCompatResources.getDrawable(this, getResources().getIdentifier("french_happy","drawable",getPackageName())))
                    binding.buttonAvatarDetailSelect.isEnabled = false
                }
                else{
                    if ((user.getProperty("currency").toString().toIntOrNull() ?: 0) - 10000 < 0){
                        Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val currentCurrency = user.getProperty("currency") as? Int ?: 0
                        saveUser("currency", currentCurrency - 10000)
                        saveUser("avatar", "french")
                        binding.buttonAvatarDetailSelect.isEnabled = false
                        saveUser("hasFrench", true)
                        Toast.makeText(this, "Selected Oui Oui Baguette!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (avatar?.name.equals("Monsieur Quaso")){
                if (user.getProperty("hasSir") as? Boolean ?: false){
                    saveUser("avatar", "sir")
                    binding.imageViewAvatarActivityImage.setImageDrawable(AppCompatResources.getDrawable(this, getResources().getIdentifier("sir_happy","drawable",getPackageName())))
                    binding.buttonAvatarDetailSelect.isEnabled = false
                }
                else{
                    if ((user.getProperty("currency").toString().toIntOrNull() ?: 0) - 20000 < 0){
                        Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val currentCurrency = user.getProperty("currency") as? Int ?: 0
                        saveUser("currency", currentCurrency - 20000)
                        saveUser("avatar", "sir")
                        binding.buttonAvatarDetailSelect.isEnabled = false
                        saveUser("hasSir", true)
                        Toast.makeText(this, "Selected Monsieur Quaso!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

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