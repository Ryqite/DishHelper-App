package com.example.kursovaya

import android.content.Intent
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        var editTextLoginLogin=findViewById<EditText>(R.id.loginText)
//        var editTextPasswordLogin=findViewById<EditText>(R.id.loginPasswordText)
//        var editTextLoginSingup=findViewById<EditText>(R.id.singupLoginText)
//        var editTextPasswordSingup=findViewById<EditText>(R.id.singupPasswordText)
//        var editTextPasswordRepeat=findViewById<EditText>(R.id.singupPasswordRepeatText)
//        var loginButton=findViewById<Button>(R.id.loginButtonContinue)
//        var singupButton=findViewById<Button>(R.id.singupButtonContinue)
//        loginButton.setOnClickListener{
//            var loginLogin: String = editTextLoginLogin.getText().toString()
//            var passwordLogin: String=editTextPasswordLogin.getText().toString()
//        }
//        singupButton.setOnClickListener{
//            var loginSingup: String=editTextLoginSingup.getText().toString()
//            var passwordSingup: String=editTextPasswordSingup.getText().toString()
//            var passwordRepeatSingup: String=editTextPasswordRepeat.getText().toString()
//            if(passwordSingup!=passwordRepeatSingup)
//            {
//                Toast.makeText(this,"Passwords aren't same", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    lateinit var login_hint:  TextView
    lateinit var singup_hint: TextView
    fun changeToLogin(v: View){
        val login_hint=findViewById<LinearLayout>(R.id.loginLayout)
        val singup_hint=findViewById<LinearLayout>(R.id.singupLayout)
        login_hint.isVisible=true
        singup_hint.isVisible=false
    }
    fun changeToSingup(v: View){
        val login=findViewById<LinearLayout>(R.id.loginLayout)
        val singup=findViewById<LinearLayout>(R.id.singupLayout)
        login.isVisible=false
        singup.isVisible=true
    }
    fun startNewActivity(v: View){
    val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
    fun finishProcess(v: View){
        finishAffinity()
    }
}