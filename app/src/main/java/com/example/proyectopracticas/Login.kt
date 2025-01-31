package com.example.proyectopracticas

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        setup()
    }
    private  fun setup(){
        val login_bttn = findViewById<AppCompatButton>(R.id.login_bttn)
        val user= findViewById<AppCompatEditText>(R.id.user)
        val pass= findViewById<AppCompatEditText>(R.id.pass)
        val error= findViewById<TextView>(R.id.error)

        val link_register= findViewById<TextView>(R.id.link_register)
        link_register.text = Html.fromHtml("<u>¿Aún no tienes una cuenta? Regístrate</u>", Html.FROM_HTML_MODE_LEGACY)
        link_register.setOnClickListener{
            val intent = Intent(this, Registro:: class.java)
            startActivity(intent)
        }
        login_bttn.setOnClickListener{
            if(user.text.toString().isNotEmpty() && pass.text.toString().isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(user.text.toString(),
                    pass.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        llevaralmain(user.text.toString())
                    }else{
                        showAlert()
                    }
                }
            }
        }
    }
    private fun llevaralmain(email: String){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email",email)
        }
        startActivity(intent)
    }
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog= builder.create()
        dialog.show()
    }
}