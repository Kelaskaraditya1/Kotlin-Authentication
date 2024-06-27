package com.starkindustries.authentication.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.starkindustries.authentication.R
import com.starkindustries.authentication.databinding.ActivityRegisterScreenBinding
class RegisterScreen : AppCompatActivity() {
    lateinit var binding:ActivityRegisterScreenBinding
    lateinit var auth:FirebaseAuth
    lateinit var firebaseDatabaseInstance:FirebaseDatabase
    lateinit var firebaseDatabase:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_screen)
        auth=FirebaseAuth.getInstance()
        binding=DataBindingUtil.setContentView(this,R.layout.activity_register_screen)
        binding.registerButton.setOnClickListener()
        {
            auth.createUserWithEmailAndPassword(binding.email.text.toString().trim(),binding.password.text.toString().trim()).addOnCompleteListener(){
                if(it.isSuccessful) {
                    var userProfileChangeRequest:UserProfileChangeRequest=UserProfileChangeRequest.Builder()
                        .setDisplayName(binding.username.text.toString().trim())
                        .build()
                    auth.currentUser?.updateProfile(userProfileChangeRequest)?.addOnCompleteListener()
                    {
                        Toast.makeText(applicationContext, "User creted Sucessfully", Toast.LENGTH_SHORT).show()
                    }
                        ?.addOnFailureListener()
                        {
                            Log.d("errorListner"," "+it.message.toString().trim())
                        }
                    startActivity(Intent(this, NotesSectionActivity::class.java))
                    finish()
                }
            }.addOnFailureListener(){
                Log.d("errorListner","The error is: "+it.message.toString().trim())
            }
        }
        binding.LoginButton.setOnClickListener()
        {
            startActivity(Intent(this,LoginScreen::class.java))
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}