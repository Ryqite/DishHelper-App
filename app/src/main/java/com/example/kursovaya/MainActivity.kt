package com.example.kursovaya

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.kursovaya.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.loginButtonContinue.setOnClickListener{
            var loginLogin: String = binding.loginText.getText().toString()
            var passwordLogin: String=binding.loginPasswordText.getText().toString()
            startNewActivity(binding.loginButtonContinue)
        }
        binding.singupButtonContinue.setOnClickListener{
            var loginSingup: String=binding.singupLoginText.getText().toString()
            var passwordSingup: String=binding.singupPasswordText.getText().toString()
            var passwordRepeatSingup: String=binding.singupPasswordRepeatText.getText().toString()
            checkPasswords(passwordSingup,passwordRepeatSingup,binding.singupButtonContinue)
        }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
    }
    fun checkPasswords(passwordSingup: String,passwordRepeatSingup: String,v: View){
        if(passwordSingup!=passwordRepeatSingup)
            {
                Toast.makeText(this,"Passwords aren't same", Toast.LENGTH_SHORT).show()
            }
        else if(passwordSingup.length<6){
            Toast.makeText(this,"Password are less than 6 symbols", Toast.LENGTH_SHORT).show()
        }
        else{
            startNewActivity(v)
        }
    }
    fun changeToLogin(v: View){
        binding.loginLayout.isVisible=true
        binding.singupLayout.isVisible=false
    }
    fun changeToSingup(v: View){
        binding.loginLayout.isVisible=false
        binding.singupLayout.isVisible=true
    }
    fun startNewActivity(v: View){
    val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
    fun finishProcess(v: View){
        finishAffinity()
    }
}