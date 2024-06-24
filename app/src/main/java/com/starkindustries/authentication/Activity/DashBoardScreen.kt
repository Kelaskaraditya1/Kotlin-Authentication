package com.starkindustries.authentication.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.starkindustries.authentication.R
import com.starkindustries.authentication.databinding.ActivityDashBoardScreenBinding
class DashBoardScreen : AppCompatActivity() {
    lateinit var binding:ActivityDashBoardScreenBinding
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dash_board_screen)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_dash_board_screen)
        auth=FirebaseAuth.getInstance()
        user= auth.currentUser!!
        binding.getUserInfo.setOnClickListener()
        {
            val user = FirebaseAuth.getInstance().currentUser
            if(user!=null)
            {
                user.let {
                    for(profile in it.providerData)
                    {
                        val profileData=it.providerId.toString().trim()
                        val userId=it.uid.toString().trim()
                        val name=it.displayName.toString().trim()
                        val email = it.email.toString().trim()
                        val photoUrl = it.photoUrl.toString().trim()
                        Log.d("userInfo","ProfileData:"+profileData+" Userid:"+userId+" Name:"+name+" Email"+email+" photoUrl"+photoUrl)
                    }
                }
            }
        }
        binding.updateUserProfile.setOnClickListener()
        {
            var profileUpdate=UserProfileChangeRequest.Builder()
                .setDisplayName("Patrick Batemen")
                .setPhotoUri(Uri.parse("https://th.bing.com/th/id/OIP.j52SeexZEeUwsooT-GP5PQHaNK?rs=1&pid=ImgDetMain"))
                .build()
            user.updateProfile(profileUpdate).addOnCompleteListener(){
                if(it.isSuccessful)
                {
                    Toast.makeText(applicationContext, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                    getuserProfile()
                }
            }.addOnFailureListener(){
                Log.d("errorListner","There is some error")
            }
        }
        binding.updateUserEmail.setOnClickListener()
        {
            if(user!=null)
            {
               user.updateEmail("patrickbatemen003@gmail.com").addOnCompleteListener()
               {
                   Toast.makeText(applicationContext, "Email Update Sucessfully", Toast.LENGTH_SHORT).show()
                   finish()
               }
                   .addOnFailureListener()
                   {
                       Log.d("errorListner"," "+it.message.toString().trim())
                   }
                startActivity(Intent(this@DashBoardScreen,LoginScreen::class.java))

            }
        }
        binding.sendEmailVerification.setOnClickListener()
        {
            if(!user.isEmailVerified)
            {
                user.sendEmailVerification().addOnCompleteListener()
                {
                    Toast.makeText(applicationContext, "Eamail verification send sucessfully", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener()
                {
                    Toast.makeText(applicationContext, "Error in sending Email verification", Toast.LENGTH_SHORT).show()
                }
            }
            else
                Toast.makeText(applicationContext, "Email already verified", Toast.LENGTH_SHORT).show()

        }
        binding.setPassword.setOnClickListener()
        {
            user.updatePassword("kelaskaraditya1").addOnCompleteListener()
            {
                if(it.isSuccessful)
                    Toast.makeText(applicationContext, "Password updated successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener()
            {
                Log.d("errorListner","Failed to update Password"+it.message.toString().trim())
            }
        }
        binding.sendPasswordReset.setOnClickListener()
        {
            auth.sendPasswordResetEmail("kelaskaraditya1@gmail.com").addOnCompleteListener()
            {
                if(it.isSuccessful)
                    Toast.makeText(applicationContext, "Password Reset Email send successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener()
            {
                Log.d("errorListner"," "+it.message.toString().trim())
            }
        }
        binding.logout.setOnClickListener()
        {
            auth.signOut()
            startActivity(Intent(this,LoginScreen::class.java))
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun getuserProfile()
    {
        if(user!=null)
        {
            user.let {
                for(profile in it.providerData)
                {
                    val profileData=it.providerId.toString().trim()
                    val userId=it.uid.toString().trim()
                    val name=it.displayName.toString().trim()
                    val email = it.email.toString().trim()
                    val photoUrl = it.photoUrl.toString().trim()
                    Log.d("userInfo","ProfileData:"+profileData+" Userid:"+userId+" Name:"+name+" Email"+email+" photoUrl"+photoUrl)
                }
            }
        }
    }
}