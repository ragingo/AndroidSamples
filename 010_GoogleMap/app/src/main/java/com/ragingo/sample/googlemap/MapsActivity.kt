package com.ragingo.sample.googlemap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        createPrefectures()
            .forEach {
                val latlng = LatLng(it.lat, it.lng)
                googleMap.addMarker(MarkerOptions().position(latlng).title(it.capital))
                if (it.name.toLowerCase(Locale.ROOT) == "tokyo") {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))
                }
            }
    }

    private fun createPrefectures(): List<Prefecture> {
        return listOf(
            Prefecture("Saitama", "Saitama", 35.85694,139.64889),
            Prefecture("Chiba", "Chiba", 35.60472, 140.12333),
            Prefecture("Tokyo", "Shinjuku", 35.68944, 139.69167)
        );
    }
}

// https://www.benricho.org/chimei/latlng_data.html
// https://en.wikipedia.org/wiki/List_of_capitals_in_Japan
data class Prefecture(
    val name: String,
    val capital: String,
    val lat: Double,
    val lng: Double
)
