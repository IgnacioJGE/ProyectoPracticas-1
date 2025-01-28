package com.example.proyectopracticas

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.analytics.FirebaseAnalytics
import org.json.JSONObject
import java.net.URL
import java.util.Locale
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    private val editTextList = mutableListOf<EditText>()
    @SuppressLint("SuspiciousIndentation")
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
                val geocoder = Geocoder(this, Locale.getDefault())
                val actual = geocoder.getFromLocationName(incio.text.toString(), 1)
                val final = geocoder.getFromLocationName(fin.text.toString(), 1)
                if (actual.isNullOrEmpty() || final.isNullOrEmpty()) {
                    showAlert("Campos incorrectos")
                }else {
                    val origen = LatLng(actual[0].latitude, actual[0].longitude)
                    val destino = LatLng(final[0].latitude, final[0].longitude)
                    try {
                        lifecycleScope.launch {
                            // Llamamos a la API para obtener la ruta
                            val puntosRuta = obtenerRuta(origen, destino)

                            // Cuando obtenemos los puntos, los mostramos en el mapa
                            if (puntosRuta.isNotEmpty()) {
                                mostrarRutaEnMapa(puntosRuta)
                            }
                        }


                    } catch (e: Exception) {
                        showAlert("Campos incorrectos")
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
        val dirección = geocoder.getFromLocationName("Madrid", 1)
        if (dirección != null) {
            val location = dirección[0]
            val latLng = LatLng(location.latitude, location.longitude)
            val ciudad = LatLng(dirección[0].latitude,dirección[0].longitude)
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudad, 10f))
        }
    }

    private suspend fun obtenerRuta(origen: LatLng, destino: LatLng): List<LatLng> {
        var url: String=""
        when (editTextList.size) {
            0 -> {
                url = "https://maps.googleapis.com/maps/api/directions/json?" +
                        "origin=${origen.latitude},${origen.longitude}" +
                        "&destination=${destino.latitude},${destino.longitude}" +
                        "&mode=driving" +
                        "&key=AIzaSyB1Jr_zKzt5aAxmdCGDT1hm7bgPwJ7sXcU"


            }
            1->{
            val geocoder = Geocoder(this, Locale.getDefault())
            val parada = geocoder.getFromLocationName(editTextList[0].text.toString(), 1)
            if(parada.isNullOrEmpty()) {
                showAlert("Campos incorrectos")
            }else{
                url = "https://maps.googleapis.com/maps/api/directions/json?" +
                        "origin=${origen.latitude},${origen.longitude}" +
                        "&destination=${destino.latitude},${destino.longitude}" +
                        "&waypoints=via:${parada[0].latitude},${parada[0].longitude}" +
                        "&mode=driving" +
                        "&key=AIzaSyB1Jr_zKzt5aAxmdCGDT1hm7bgPwJ7sXcU"
            }
        }
            2->{
                val geocoder = Geocoder(this, Locale.getDefault())
                val parada = geocoder.getFromLocationName(editTextList[0].text.toString(), 1)
                val parada2 = geocoder.getFromLocationName(editTextList[1].text.toString(), 1)

                if(parada.isNullOrEmpty()|| parada2.isNullOrEmpty()) {
                    showAlert("Campos incorrectos")
                }else{
                    url = "https://maps.googleapis.com/maps/api/directions/json?" +
                            "origin=${origen.latitude},${origen.longitude}" +
                            "&destination=${destino.latitude},${destino.longitude}" +
                            "&waypoints=via:${parada[0].latitude},${parada[0].longitude}|" +
                            "via:${parada2[0].latitude},${parada2[0].longitude}"+
                            "&mode=driving" +
                            "&key=AIzaSyB1Jr_zKzt5aAxmdCGDT1hm7bgPwJ7sXcU"
                }

            }
            3->{
                val geocoder = Geocoder(this, Locale.getDefault())
                val parada = geocoder.getFromLocationName(editTextList[0].text.toString(), 1)
                val parada2 = geocoder.getFromLocationName(editTextList[1].text.toString(), 1)
                val parada3 = geocoder.getFromLocationName(editTextList[2].text.toString(), 1)
                if(parada.isNullOrEmpty()|| parada2.isNullOrEmpty() || parada3.isNullOrEmpty()) {
                    showAlert("Campos incorrectos")
                }else{
                    url = "https://maps.googleapis.com/maps/api/directions/json?" +
                            "origin=${origen.latitude},${origen.longitude}" +
                            "&destination=${destino.latitude},${destino.longitude}" +
                            "&waypoints=via:${parada[0].latitude},${parada[0].longitude}|" +
                            "via:${parada2[0].latitude},${parada2[0].longitude}|"+
                            "via:${parada3[0].latitude},${parada3[0].longitude}"+
                            "&mode=driving" +
                            "&key=AIzaSyB1Jr_zKzt5aAxmdCGDT1hm7bgPwJ7sXcU"
                }
            }
            else -> {
                url = "https://maps.googleapis.com/maps/api/directions/json?" +
                        "origin=${origen.latitude},${origen.longitude}" +
                        "&destination=${destino.latitude},${destino.longitude}" +
                        "&mode=driving" +
                        "&key=AIzaSyB1Jr_zKzt5aAxmdCGDT1hm7bgPwJ7sXcU"
            }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = URL(url).readText()//lee la respuesta
                Log.d("CACA","${response}")
                val puntos = JSONObject(response)//combierte en jsonobject
                    .getJSONArray("routes")//combierte en jsonobject
                    .getJSONObject(0)//el primero del array es la ruta
                    .getJSONObject("overview_polyline")
                    .getString("points")//obtiene la lista de puntos
                PolyUtil.decode(puntos)//lo convierte en una lista de latlongs
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList<LatLng>()
            }
        }

    }
    private fun mostrarRutaEnMapa(puntosRuta: List<LatLng>) {
        if (puntosRuta.isEmpty()) {
            showAlert("Campos incorrectos")
        } else {
            val polylineoptions = PolylineOptions()
                .addAll(puntosRuta)
                .color(android.graphics.Color.BLUE)
                .width(10f)
            gMap.clear()
            gMap.addPolyline(polylineoptions)
            gMap.addMarker(MarkerOptions().position(puntosRuta.first()))
            gMap.addMarker(MarkerOptions().position(puntosRuta.last()))
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntosRuta.first(), 12f))
        }
    }
}




