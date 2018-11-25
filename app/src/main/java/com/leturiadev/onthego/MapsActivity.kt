package com.leturiadev.onthego


import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import com.google.android.gms.maps.model.PolylineOptions
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locManager:LocationManager

    private var locationManager : LocationManager? = null

    private var api = "AIzaSyAGb_sS6_DZiONRvbrkcUxBVJL-SrRfLjY "
    private var url = "https://maps.googleapis.com/maps/api/directions/"
    private var destinashon = "-8.106131, -79.021151"
    lateinit var pos:LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        backButton.setOnClickListener {
            onBackPressed()
        }

        // Create persistent LocationManager reference
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?;
            pos = LatLng (99999.99, 99999.99)

            try {
                // Request location updates
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener);
            } catch(ex: SecurityException) {
                Log.d("myTag", "Security Exception, no location available");
            }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            pos = LatLng(location.longitude,location.latitude)
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        if(pos == LatLng (99999.99, 99999.99)) {
            val sydney = pos;

            mMap.addMarker(MarkerOptions().position(sydney).title("Mi posicion :V"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }

        val polyline1 = googleMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(-8.116305, -79.030021),
                    LatLng(-8.116544, -79.029624),
                    LatLng(-8.116592, -79.028551),
                    LatLng(-8.116358, -79.026856),
                    LatLng(-8.116309, -79.026406),
                    LatLng(-8.115844, -79.025725),
                    LatLng(-8.114776, -79.024741),
                    LatLng(-8.113573, -79.023695),
                    LatLng(-8.111943, -79.022719),
                    LatLng(-8.110698, -79.021949),
                    LatLng(-8.109613, -79.021554),
                    LatLng(-8.109074, -79.021476),
                    LatLng(-8.108779, -79.021538),
                    LatLng(-8.108237, -79.021720),
                    LatLng(-8.107411, -79.022007),
                    LatLng(-8.107214, -79.022225),
                    LatLng(-8.106551, -79.022947),
                    LatLng(-8.105776, -79.023905),
                    LatLng(-8.105606, -79.024211),
                    LatLng(-8.105314, -79.025496),
                    LatLng(-8.105314, -79.025496),
                    LatLng(-8.105143, -79.026166),
                    LatLng(-8.105178, -79.026550),
                    LatLng(-8.105719, -79.027752),
                    LatLng(-8.106000, -79.028487),
                    LatLng(-8.106228, -79.028986),
                    LatLng(-8.106318, -79.029115),
                    LatLng(-8.107376, -79.030063),
                    LatLng(-8.108385, -79.030911),
                    LatLng(-8.110737, -79.032901),
                    LatLng(-8.112018, -79.032836),
                    LatLng(-8.113052, -79.032775),
                    LatLng(-8.113790, -79.032681),
                    LatLng(-8.114289, -79.032329),
                    LatLng(-8.114687, -79.032050),
                    LatLng(-8.115723, -79.031194),
                    LatLng(-8.116305, -79.030021)
                )
        )




    }
}
