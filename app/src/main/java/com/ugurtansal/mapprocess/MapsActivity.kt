package com.ugurtansal.mapprocess

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.ugurtansal.mapprocess.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private lateinit var  locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        print("On create done---------------------")
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        print("Map is ready---------------------")
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        /*
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f)) // daha yakınlaştırarak gösteriyor



         */

        //kullanıcının konumu
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener =
            object : LocationListener { // locationListener interface ini implement ettik
                override fun onLocationChanged(location: Location) {
                    print("Location changed---------------------")
                    val userLocation = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))
                }
            }

        //İzin alma
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            //izin verilmemiş
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Snackbar.make(
                    binding.root,
                    "Permission needed for location",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Give Permission") {
                    //request permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1
                    )
                }.show()
            } else {
                //request permission
            }
        }else{
            //Konum izni zaten verilmiş
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,//Kaç milisaniyede bir güncelleme yapacak , 0 ise çok sık günceller ve pil tüketir
                0f,//kaç metrede bir güncelleme yapacak , 0 ise çok sık günceller ve pil tüketir
                locationListener
            )
        }
    }
}