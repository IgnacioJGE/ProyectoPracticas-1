package com.example.proyectopracticas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)  // Esto le indica al fragmento que prepare el mapa
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        // Agregar un marcador y mover la cámara
        val sydney = LatLng(-34.0, 151.0)  // Ejemplo de coordenadas
        gMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))
    }
}
