package com.example.proyectopracticas

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    private val editTextList = mutableListOf<EditText>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val  incio= findViewById<AppCompatEditText>(R.id.inicio)
        val  fin= findViewById<AppCompatEditText>(R.id.fin)
        val container = findViewById<LinearLayout>(R.id.paradas)
        val bttn = findViewById<AppCompatButton>(R.id.btn_add)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val busqueda = findViewById<AppCompatButton>(R.id.generar_ruta)
        mapFragment.getMapAsync(this)//ASI SE AÑADE EL MAPA AL FRAGMET

        bttn.setOnClickListener {//AÑADO LAS PARADAS
            if(editTextList.size<3){
                val newEditText = EditText(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(60, 16, 60, 16)
                    }
                    hint = "Parada"
                }
                container.addView(newEditText)
                editTextList.add(newEditText)
            }


        }
        busqueda.setOnClickListener{
            if(incio.text.toString().isEmpty()||fin.text.toString().isEmpty()){
                showAlert("Campos incompletos")
            }else{
                when(editTextList.size){
                    0-> {
                        try {
                            val geocoder = Geocoder(this, Locale.getDefault())
                            val actual = geocoder.getFromLocationName(incio.text.toString(), 1)
                            val destino = geocoder.getFromLocationName(fin.text.toString(), 1)
                            if (actual != null && destino != null) {
                                val location = actual[0]
                                val latLng = LatLng(location.latitude, location.longitude)
                                //Agregar un marcador y mover la cámara
                                val ciudad = LatLng(actual[0].latitude,actual[0].longitude)  // Ejemplo de coordenadas
                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudad, 10f))

                            }
                        }catch (e:IOException){
                            showAlert("Campos incorrectos")
                        }

                    }
                }

            }

        }

    }

    private fun showAlert(tex:String) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Error")
        builder.setMessage("Error relacionado con ${tex}")
        builder.setPositiveButton("Aceptar",null)
        val dialog= builder.create()
        dialog.show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        val geocoder = Geocoder(this, Locale.getDefault())
        val dirección = geocoder.getFromLocationName("mdagasgar", 1)
        if (dirección != null) {
            val location = dirección[0]
            val latLng = LatLng(location.latitude, location.longitude)
            //Agregar un marcador y mover la cámara
            val ciudad = LatLng(dirección[0].latitude,dirección[0].longitude)  // Ejemplo de coordenadas
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudad, 10f))
        }
    }
}




