package hr.fer.grupa.erestoran.restaurants

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.Restaurant
import hr.fer.grupa.erestoran.databinding.ActivityRestaurantsBinding


class RestaurantsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityRestaurantsBinding

    private val database = FirebaseDatabase.getInstance().reference

    private lateinit var locationClient: FusedLocationProviderClient

    private var googleMap: GoogleMap? = null

    private lateinit var adapter: RestaurantAdapter

    private var userLocation = Location("user")

    private val restaurants = mutableListOf<Restaurant>()

    private var lastSelectedIndex = -1
    private var lastSelectedMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_restaurants
        )
        binding.activity = this
        adapter = RestaurantAdapter(restaurants, this)
        binding.recyclerView.adapter = adapter
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        getUserLocation()
    }

    private fun getRestaurants() {
        val restaurantsQuery = database.child("Restaurants")
        restaurantsQuery.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "An unexpected error occured.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val restaurant = it.getValue(Restaurant::class.java)
                    val restarauntLocation = Location("Restaurants")
                    restarauntLocation.latitude = restaurant!!.lat
                    restarauntLocation.longitude = restaurant.long
                    restaurant.distance = (restarauntLocation.distanceTo(userLocation)/1000).round(2)
                    restaurants.add(restaurant)
                }
                initRecyclerView()
            }

        })
    }

    fun Float.round(decimals: Int = 2): Float = "%.${decimals}f".format(this).toFloat()

    private fun initRecyclerView() {
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerView.context, RecyclerView.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        restaurants.sortBy { it.distance }
        adapter.notifyDataSetChanged()
        showMapMarkers()
    }

    private fun getUserLocation() {
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                locationClient.lastLocation.addOnCompleteListener {
                    val location = it.result
                    if (location == null) requestNewLocation()
                    else {
                        userLocation.latitude = location.latitude
                        userLocation.longitude = location.longitude
                        getRestaurants()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestNewLocation() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        locationClient = LocationServices.getFusedLocationProviderClient(this)
        locationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            userLocation.latitude = locationResult.lastLocation.latitude
            userLocation.longitude = locationResult.lastLocation.longitude
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            123
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation()
            }
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        googleMap!!.setMinZoomPreference(10f)
    }

    private fun showMapMarkers() {
        if (googleMap != null) {
            restaurants.forEach {
                val latLng = LatLng(it.lat, it.long)
                val markerOption = MarkerOptions()
                markerOption.position(latLng)
                markerOption.icon(bitmapDescriptorFromVector(this, R.drawable.round_marker))
                markerOption.title(it.title)
                markerOption.snippet(it.address)
                googleMap!!.addMarker(markerOption)
            }
            val userMarker = MarkerOptions()
            userMarker.position(LatLng(userLocation.latitude, userLocation.longitude))
            userMarker.icon(bitmapDescriptorFromVector(this, R.drawable.ic_my_location_24dp))
            userMarker.title("Your location")
            googleMap!!.addMarker(userMarker)
            googleMap!!.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        userLocation.latitude,
                        userLocation.longitude
                    ), 14f
                )
            )
            googleMap!!.setOnMarkerClickListener {
                restaurants.forEachIndexed { index, restaurant ->
                    if (it.title == restaurant.title) {
                        restaurants[index].isSelected = true
                        if (lastSelectedIndex != -1) {
                            restaurants[lastSelectedIndex].isSelected = false
                            binding.recyclerView.adapter!!.notifyItemChanged(lastSelectedIndex)
                        }
                        lastSelectedMarker?.setIcon(bitmapDescriptorFromVector(this, R.drawable.round_marker))
                        binding.recyclerView.adapter!!.notifyItemChanged(index)
                        binding.recyclerView.scrollToPosition(index)
                        lastSelectedIndex = index
                        lastSelectedMarker = it
                        it.setIcon(bitmapDescriptorFromVector(this, R.drawable.round_marker_selected))
                    }
                }
                googleMap!!.animateCamera(CameraUpdateFactory.newLatLng(it.position))
                true
            }
        }
    }

    //TODO search, item click scroll on map, closest restaurant handling

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onStart() {
        binding.mapView.onStart()
        super.onStart()
    }

    override fun onResume() {
        binding.mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

}