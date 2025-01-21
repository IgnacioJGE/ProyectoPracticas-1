package com.example.proyectopracticas
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        setup()
    }
    private  fun setup(){
        val  reg_bttn= findViewById<AppCompatButton>(R.id.reg_bttn)
        val user= findViewById<AppCompatEditText>(R.id.user)
        val pass= findViewById<AppCompatEditText>(R.id.pass)
        val error= findViewById<TextView>(R.id.error)
        reg_bttn.setOnClickListener{
            if(user.text.toString().isNotEmpty() && pass.text.toString().isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(user.text.toString(),
                    pass.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                                llevaralmain(user.text.toString())
                        }else{
                            error.setText("Error al Registrarse")
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


}
