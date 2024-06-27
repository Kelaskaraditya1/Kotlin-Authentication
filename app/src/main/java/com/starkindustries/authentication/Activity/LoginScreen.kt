package com.starkindustries.authentication.Activity
import android.app.Activity
import android.content.ContentProviderClient
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.starkindustries.authentication.R
import com.starkindustries.authentication.databinding.ActivityLoginScreenBinding
class LoginScreen : AppCompatActivity() {
    lateinit var binding:ActivityLoginScreenBinding
    lateinit var auth:FirebaseAuth
    internal lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_login_screen)
        auth=FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_id))
            .requestEmail()
            .build()
        googleSignInClient=GoogleSignIn.getClient(this,gso)
        binding.registerbtn.setOnClickListener()
        {
            startActivity(Intent(this,RegisterScreen::class.java))
            finish()
        }
        binding.loginbtn.setOnClickListener()
        {
            var view = this.currentFocus
            if(view!=null)
            {
                var manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(view.windowToken,0)
            }
            auth.signInWithEmailAndPassword(binding.loginText.text.toString().trim(),binding.passwordText.text.toString().trim()).addOnCompleteListener(){
                if(it.isSuccessful)
                {
                    startActivity(Intent(this,NotesSectionActivity::class.java))
                    finish()
                }
            }.addOnFailureListener()
            {
                Toast.makeText(applicationContext, "Either Email or Password is incorrect", Toast.LENGTH_SHORT).show()
            }
        }
        binding.googleSigninButton.setOnClickListener()
        {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, SIGN_IN_REQUEST_CODE)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
//    override fun onStart() {
//        super.onStart()
//        if(auth.currentUser!=null)
//        {
//            Toast.makeText(applicationContext, "Welcome back "+auth.currentUser?.displayName.toString().trim(), Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this,NotesSectionActivity::class .java))
//            finish()
//        }
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== SIGN_IN_REQUEST_CODE)
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try
            {
                val account = task.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credentials).addOnCompleteListener()
                {
                    if(it.isSuccessful)
                    {
                        Toast.makeText(applicationContext, "Welcome "+auth.currentUser?.displayName.toString().trim(), Toast.LENGTH_SHORT).show()
                        var intent = Intent(this,NotesSectionActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            catch (e:Exception)
            {
                Toast.makeText(applicationContext, "Some thing wnt Wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object
    {
        val SIGN_IN_REQUEST_CODE=9001
    }
}