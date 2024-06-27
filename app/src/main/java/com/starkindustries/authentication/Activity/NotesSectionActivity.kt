package com.starkindustries.authentication.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.starkindustries.authentication.R
import com.starkindustries.authentication.databinding.ActivityNotesSectionBinding
class NotesSectionActivity : AppCompatActivity() {
    lateinit var binding:ActivityNotesSectionBinding
    lateinit var auth:FirebaseAuth
    internal lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notes_section)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_notes_section)
        auth=FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_id))
            .requestEmail()
            .build()
        googleSignInClient=GoogleSignIn.getClient(this,gso)
        binding.addNote.setOnClickListener()
        {
            var intent = Intent(this,AddNotesActivity::class.java)
            startActivity(intent)
        }
        binding.logout.setOnClickListener()
        {
            googleSignInClient.signOut().addOnCompleteListener()
            {
                if(it.isSuccessful)
                {
                    val intent = Intent(this,LoginScreen::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener(){
                Toast.makeText(applicationContext, " "+it.message.toString().trim(), Toast.LENGTH_SHORT).show()
            }

        }
        binding.updateNote.setOnClickListener()
        {
            var intent = Intent(this,ShowNotesActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}