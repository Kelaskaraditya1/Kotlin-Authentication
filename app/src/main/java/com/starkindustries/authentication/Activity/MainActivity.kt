package com.starkindustries.authentication.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.starkindustries.authentication.R
import com.starkindustries.authentication.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        auth=FirebaseAuth.getInstance()
        LongOperation().execute()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    open inner class LongOperation:AsyncTask<String?,Void?,String?>()
    {
        override fun doInBackground(vararg params: String?): String? {
            for(i in 0..3)
            {
                try
                { Thread.sleep(1000) }
                catch(e:Exception)
                { Thread.interrupted() }
            }
         return "results"
        }
        override fun onPostExecute(result: String?) {
            if(auth.currentUser!=null)
                startActivity(Intent(this@MainActivity,DashBoardScreen::class.java))
            else
                startActivity(Intent(this@MainActivity,LoginScreen::class.java))
        }
    }
}