package com.leturiadev.onthego


import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Location
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
import android.location.LocationManager
import android.support.v4.app.ActivityCompat
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.RoundCap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CustomCap
import android.location.Geocoder
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.maps.android.PolyUtil
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

    override fun onPolylineClick(p: Polyline) {
        var type = p.tag
        when (type) {
            "C" ->
                Toast.makeText(this, "Rutas Micro C", Toast.LENGTH_LONG).show()
        }
    }

    private lateinit var mMap: GoogleMap

    private var destinashon = "-8.106131,-79.021151"

    private lateinit var  requestQueue : RequestQueue

    //Location
    private lateinit var  fusedLocationClient : FusedLocationProviderClient
    private lateinit var lastLocation : Location
    private val COLOR_ORANGE_ARGB = -0xa80e9

    private lateinit var txtCalle : TextView
    private lateinit var txtTiempo : TextView
    private lateinit var txtDistancia : TextView

    lateinit var pos:LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        backButton.setOnClickListener {
            onBackPressed()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        txtCalle = findViewById(R.id.txtCalle)
        txtTiempo = findViewById(R.id.txtTiempo)
        txtDistancia = findViewById(R.id.txtDistancia)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setUpMap(){

        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this){location ->
            if(location != null){
                lastLocation = location
                pos = LatLng(location.latitude,location.longitude)

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,13f))


                requestQueue= Volley.newRequestQueue(this)



                try {
                    val stringRequest = StringRequest(
                        Request.Method.GET, "https://maps.googleapis.com/maps/api/geocode/json?latlng="+location.latitude+","+location.longitude+"&key=AIzaSyC0miOfNBZL4Fr-IUEiQNV3YTozgTkv8Pc",
                        Response.Listener { response ->
                            val obj = JSONObject(response)

                            val resultsArray = obj.getJSONArray("results")
                            val result = resultsArray.getJSONObject(0)
                            val direccion = result.getString("formatted_address")
                            txtCalle.text = direccion

                        },
                        Response.ErrorListener { error ->
                            Toast.makeText(this, "Error JSON", Toast.LENGTH_LONG).show()
                        }
                    )

                    val requestQueue1 = Volley.newRequestQueue(this)
                    requestQueue1.add<String>(stringRequest)

                }catch (x : Exception){
                    Toast.makeText(this, x.toString(), Toast.LENGTH_LONG).show()
                }



                try {
                    val path: MutableList<List<LatLng>> = ArrayList()
                    var stringrequest2 = StringRequest(
                        Request.Method.GET, "https://maps.googleapis.com/maps/api/directions/json?origin="+location.latitude +","+location.longitude +"&destination="+destinashon+"&key=AIzaSyC0miOfNBZL4Fr-IUEiQNV3YTozgTkv8Pc",
                        Response.Listener { response ->

                            val jsonResponse = JSONObject(response)
                            // Get routes
                            val routes = jsonResponse.getJSONArray("routes")
                            val legs = routes.getJSONObject(0).getJSONArray("legs")
                            val distancia = legs.getJSONObject(0).getJSONObject("distance").getString("value")
                            val akm = (distancia.toDouble()/1000)
                            txtDistancia.text = ""+ akm + " Km."
                            val tiempo = legs.getJSONObject(0).getJSONObject("duration").getString("text")
                            txtTiempo.text = tiempo
                            val steps = legs.getJSONObject(0).getJSONArray("steps")

                            for (i in 0 until steps.length()) {
                                val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                                path.add(PolyUtil.decode(points))
                            }
                            for (i in 0 until path.size) {
                                mMap!!.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
                            }

                        },
                        Response.ErrorListener { error ->
                            Toast.makeText(this, "Error JSON", Toast.LENGTH_LONG).show()
                        }
                    )
                    val requestQueue2 = Volley.newRequestQueue(this)
                    requestQueue2.add<String>(stringrequest2)
                }catch (x : Exception){
                    Toast.makeText(this, x.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        setUpMap()


        val microC = googleMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .color(COLOR_ORANGE_ARGB)
                .add(
                    LatLng(-8.116305, -79.030021),
                    LatLng(-8.116544,   -79.029624),
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


        mMap.setOnPolylineClickListener(this)
        microC.setTag("C")

    }


}
