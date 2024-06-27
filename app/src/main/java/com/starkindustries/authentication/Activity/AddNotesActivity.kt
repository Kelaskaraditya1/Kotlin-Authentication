package com.starkindustries.authentication.Activity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.starkindustries.authentication.Adapter.RecyclerviewAdapter
import com.starkindustries.authentication.Model.Notes
import com.starkindustries.authentication.R
import com.starkindustries.authentication.databinding.ActivityAddNotesBinding
import com.starkindustries.authentication.databinding.ActivityNotesSectionBinding

class AddNotesActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddNotesBinding
    lateinit var databaseRefrence: DatabaseReference
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_notes)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_add_notes)
        auth=FirebaseAuth.getInstance()
        var user = auth.currentUser
        databaseRefrence=FirebaseDatabase.getInstance().reference
        binding.save.setOnClickListener()
        {
            val view = this.currentFocus
            if(view!=null)
            {
                val manager:InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(view.windowToken,0)
            }
            user.let {
                val noteKey = databaseRefrence.child("users").child(user?.uid!!).child("notes").push().key
                val note = Notes(binding.title.text.toString().trim(),binding.thought.text.toString().trim(),noteKey?:"")
                if(noteKey!=null)
                    databaseRefrence.child("users").child(user?.uid!!).child("notes").child(noteKey).setValue(note)
                        .addOnCompleteListener()
                        {
                            if(it.isSuccessful)
                                Toast.makeText(applicationContext, "Note saved successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener()
                        {
                            Toast.makeText(applicationContext, "failed to save notee", Toast.LENGTH_SHORT).show()
                        }
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}