package hr.fer.grupa.erestoran.restaurants

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.models.Restaurant
import hr.fer.grupa.erestoran.databinding.FragmentRestaurantsBinding
import hr.fer.grupa.erestoran.food.FoodFragment
import hr.fer.grupa.erestoran.models.OrderFragmentEvent
import org.greenrobot.eventbus.EventBus


class RestaurantsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentRestaurantsBinding

    private val database = FirebaseDatabase.getInstance().reference

    private lateinit var locationClient: FusedLocationProviderClient

    private var googleMap: GoogleMap? = null

    private lateinit var adapter: RestaurantAdapter

    private var userLocation = Location("user")

    private val restaurants = mutableListOf<Restaurant>()
    private val markers = mutableListOf<Marker>()

    private var lastSelectedIndex = -1
    private var lastSelectedMarker: Marker? = null
    private val dialogHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_restaurants, container, false)
        binding.fragment = this
        adapter =
            RestaurantAdapter(
                restaurants,
                requireContext()
            ) { restaurant, index -> restaurantClicked(restaurant, index) }
        binding.recyclerView.adapter = adapter
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        binding.search.addTextChangedListener(object : TextWatcher {
            @SuppressLint("DefaultLocale")
            override fun afterTextChanged(p0: Editable?) {
                val searchedRestaurants = restaurants.filter {
                    it.title.toLowerCase().contains(p0.toString().toLowerCase())
                } as MutableList
                adapter.setItems(searchedRestaurants)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        getUserLocation()
        return binding.root
    }

    private fun getRestaurants() {
        val restaurantsQuery = database.child("Restaurants")
        restaurantsQuery.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "An unexpected error occured.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val restaurant = it.getValue(Restaurant::class.java)
                    restaurant!!.id = it.key!!
                    val restarauntLocation = Location("Restaurants")
                    restarauntLocation.latitude = restaurant.lat
                    restarauntLocation.longitude = restaurant.long
                    restaurant.distance =
                        (restarauntLocation.distanceTo(userLocation) / 1000).round(2)
                    restaurants.add(restaurant)
                }
                initRecyclerView()
            }

        })
    }

    fun Float.round(decimals: Int = 2) = "%.${decimals}f".format(this).replace(",", ".").toFloat()

    private fun initRecyclerView() {
        val dividerItemDecoration =
            DividerItemDecoration(binding.recyclerView.context, RecyclerView.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        restaurants.sortBy { it.distance }
        if (restaurants[0].distance < 0.05) restarauntNearMe()
        adapter.notifyDataSetChanged()
        showMapMarkers()
    }

    private fun getUserLocation() {
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
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
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
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

        locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
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
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
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
                markerOption.icon(bitmapDescriptorFromVector(requireContext(), R.drawable.round_marker))
                markerOption.title(it.title)
                markerOption.snippet(it.address)
                markers.add(googleMap!!.addMarker(markerOption))
            }
            val userMarker = MarkerOptions()
            userMarker.position(LatLng(userLocation.latitude, userLocation.longitude))
            userMarker.icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_my_location_24dp))
            userMarker.title("Your location")
            googleMap!!.addMarker(userMarker)
            googleMap!!.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    userMarker.position
                    , 14f
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
                        lastSelectedMarker?.setIcon(
                            bitmapDescriptorFromVector(
                                requireContext(),
                                R.drawable.round_marker
                            )
                        )
                        binding.recyclerView.adapter!!.notifyItemChanged(index)
                        binding.recyclerView.scrollToPosition(index)
                        lastSelectedIndex = index
                        lastSelectedMarker = it
                        it.setIcon(
                            bitmapDescriptorFromVector(
                                requireContext(),
                                R.drawable.round_marker_selected
                            )
                        )
                    }
                }
                googleMap!!.animateCamera(CameraUpdateFactory.newLatLng(it.position))
                true
            }
        }
    }

    private fun restarauntNearMe() {
        val restaurant = restaurants[0]
        markerUpdate(restaurant, 0)
        val dialogRunnable = Runnable {
            val dialog = BottomSheetDialog(requireContext())
            val dialogView = this.layoutInflater.inflate(R.layout.dialog_select_restaurant, null)
            dialog.setContentView(dialogView)
            dialog.findViewById<TextView>(R.id.bottom_sheet_prompt)!!.text =
                this.getString(R.string.are_you_here)
            dialog.findViewById<TextView>(R.id.yes_button)!!.setOnClickListener {
                handleRestaurantSelection(restaurant, dialog)
            }
            dialog.findViewById<TextView>(R.id.no_button)!!.setOnClickListener {
                restaurants[0].isSelected = false
                adapter.notifyItemChanged(0)
                dialog.hide()
            }
            dialog.show()
        }
        dialogHandler.postDelayed(dialogRunnable, 1000)
    }

    private fun markerUpdate(restaurant: Restaurant, index: Int) {
        markers.forEach { marker ->
            if (marker.title == restaurant.title) {
                restaurants[index].isSelected = true
                if (lastSelectedIndex != -1) {
                    restaurants[lastSelectedIndex].isSelected = false
                    binding.recyclerView.adapter!!.notifyItemChanged(lastSelectedIndex)
                }
                lastSelectedMarker?.setIcon(
                    bitmapDescriptorFromVector(
                        requireContext(),
                        R.drawable.round_marker
                    )
                )
                binding.recyclerView.adapter!!.notifyItemChanged(index)
                binding.recyclerView.scrollToPosition(index)
                lastSelectedIndex = index
                lastSelectedMarker = marker
                marker.setIcon(
                    bitmapDescriptorFromVector(
                        requireContext(),
                        R.drawable.round_marker_selected
                    )
                )
                googleMap!!.animateCamera(CameraUpdateFactory.newLatLng(marker.position))
            }
        }
    }

    private fun restaurantClicked(restaurant: Restaurant, index: Int) {
        markerUpdate(restaurant, index)
        val showDialogRunnable = Runnable {
            val dialog = BottomSheetDialog(requireContext())
            val dialogView = this.layoutInflater.inflate(R.layout.dialog_select_restaurant, null)
            dialog.setContentView(dialogView)
            dialog.findViewById<TextView>(R.id.yes_button)!!.setOnClickListener {
                handleRestaurantSelection(restaurant, dialog)
            }
            dialog.findViewById<TextView>(R.id.no_button)!!.setOnClickListener { dialog.hide() }
            dialog.show()
        }
        dialogHandler.postDelayed(showDialogRunnable, 1000)
    }

    private fun handleRestaurantSelection(restaurant: Restaurant, dialog: BottomSheetDialog) {
        EventBus.getDefault().post(OrderFragmentEvent(this, restaurant))
        dialog.dismiss()
    }

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