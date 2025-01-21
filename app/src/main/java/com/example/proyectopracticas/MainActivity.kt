package com.example.proyectopracticas

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import android.widget.LinearLayout
import androidx.core.view.WindowInsetsCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.analytics.FirebaseAnalytics
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val analytics: FirebaseAnalytics= FirebaseAnalytics.getInstance(this)
        val bundle= Bundle()
        bundle.putString("mensaje","Integracion completada")
        analytics.logEvent("Inicio",bundle)
    }
}