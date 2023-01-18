package com.example.quizapp

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_quiz_questions.*
import kotlinx.android.synthetic.main.activity_result.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

//        tutaj dodajemy nasz button startowy, pobieramy info
        val etName = findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.et_name)
        val btnStart = findViewById<Button>(R.id.btn_start)
        btnStart.setOnClickListener{
            if (etName.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter your name in the form", Toast.LENGTH_SHORT).show()
            }else{
                // Intent wysyła nas do pytań
                val intent = Intent(this, QuizQuestionsActivity::class.java)
//                rozpoczynamy naszą aktywność, zamykamy naszą dotychczasową aktywość przechodząc do następnej
                //extra informations like uptaking username for result page
                intent.putExtra(Constants.USER_NAME, et_name.text.toString())
                startActivity(intent)
                finish()
            }}
    }
}