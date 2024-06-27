package com.starkindustries.authentication.Activity
import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.starkindustries.authentication.Adapter.RecyclerviewAdapter
import com.starkindustries.authentication.Model.Notes
import com.starkindustries.authentication.R
import com.starkindustries.authentication.databinding.ActivityShowNotesBinding

class ShowNotesActivity : AppCompatActivity(),RecyclerviewAdapter.OnClickListner {
    lateinit var binding:ActivityShowNotesBinding
    lateinit var auth:FirebaseAuth
    lateinit var dbRefrence:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_notes)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_show_notes)
        auth=FirebaseAuth.getInstance()
        dbRefrence=FirebaseDatabase.getInstance().reference
        var user = auth.currentUser
        if(user!=null)
        {
            user.let {
                val noterefrence=dbRefrence.child("users").child(user.uid).child("notes").addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val noteList= arrayListOf<Notes>()
                        for(notesnapshot in snapshot.children)
                        {
                            val note = notesnapshot.getValue(Notes::class.java)
                            note.let {
                                noteList.add(it!!)
                            }
                        }
                        binding.recyclerView.layoutManager=LinearLayoutManager(applicationContext)
                        binding.recyclerView.adapter=RecyclerviewAdapter(applicationContext,noteList,this@ShowNotesActivity)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }
        binding.recyclerView.layoutManager=LinearLayoutManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    override fun onUpdateClicked(noteId: String) {  
        val user = auth.currentUser
        val updateAlertDialog = AlertDialog.Builder(this)
        updateAlertDialog.setTitle("Update")
        updateAlertDialog.setMessage("Are you sure,you want to update")
        updateAlertDialog.setPositiveButton("Yes",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val updateNote = Notes("Batman","I am Batman",noteId)
                val noteref = dbRefrence.child("users").child(user?.uid!!).child("notes").child(noteId).setValue(updateNote)
            }
        })
        updateAlertDialog.setNegativeButton("No",object:DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
            }
        })
        updateAlertDialog.setNeutralButton("cancel",object:DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
            }

        })
        updateAlertDialog.setCancelable(false)
        updateAlertDialog.show()
    }

    override fun onDeleteClicked(noteId: String) {
        val user = auth.currentUser
        if(user!=null)
        {
            user.let {
                val noteref = dbRefrence.child("users").child(user.uid).child("notes")
                noteref.child(noteId).removeValue()
            }
        }
    }
}