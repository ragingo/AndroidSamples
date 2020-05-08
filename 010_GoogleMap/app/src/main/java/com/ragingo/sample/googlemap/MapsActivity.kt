package com.ragingo.sample.googlemap

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    // https://developer.android.com/training/location/retrieve-current?hl=ja
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
        private val TAG = MapsActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        gpsButton.setOnClickListener { onGpsButtonClick() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != PERMISSION_REQUEST_CODE) {
            return
        }

        val result = permissions.filterIndexed { i, _ ->
            grantResults[i] == PackageManager.PERMISSION_GRANTED
        }

        if (!result.any { it == Manifest.permission.ACCESS_FINE_LOCATION }) {
            Log.d(TAG, "ACCESS_FINE_LOCATION が許可されてない")
        }
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
        this.googleMap = googleMap
        createPrefectures()
            .forEach {
                val latlng = LatLng(it.lat, it.lng)
                googleMap.addMarker(MarkerOptions().position(latlng).title(it.capital))
                if (it.name.toLowerCase(Locale.ROOT) == "tokyo") {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13f))
                }
            }
    }

    private fun onGpsButtonClick() {
        val granted =
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            return
        }

        val req = LocationRequest()
        req.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val callback = object: LocationCallback() {
            override fun onLocationAvailability(value: LocationAvailability?) {
                super.onLocationAvailability(value)
                println("onLocationAvailability: ${value?.isLocationAvailable}")
            }
            override fun onLocationResult(value: LocationResult?) {
                fusedLocationClient.removeLocationUpdates(this)

                value ?: return
                val latlng = LatLng(value.lastLocation.latitude, value.lastLocation.longitude)
                val circleOpts = CircleOptions()
                    .center(latlng)
                    .radius(1000.0)
                    .fillColor(Color.argb(0x7f, 0, 0xff, 0))
                    .strokeColor(Color.BLUE)
                    .strokeWidth(20f)
                googleMap?.clear()
                googleMap?.addMarker(MarkerOptions().position(latlng))
                googleMap?.addCircle(circleOpts)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13f))
            }
        }

        fusedLocationClient.requestLocationUpdates(req, callback, null)
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
