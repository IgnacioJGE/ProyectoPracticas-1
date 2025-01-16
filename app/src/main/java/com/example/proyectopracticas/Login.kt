package com.example.proyectopracticas

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val login_bttn = findViewById<AppCompatButton>(R.id.login_bttn)
        login_bttn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val link_register= findViewById<TextView>(R.id.link_register)
        link_register.text = Html.fromHtml("<u>¿Aún no tienes una cuenta? Regístrate</u>", Html.FROM_HTML_MODE_LEGACY)

        link_register.setOnClickListener{
            val intent = Intent(this, Registro:: class.java)
            startActivity(intent)
        }
    }
}