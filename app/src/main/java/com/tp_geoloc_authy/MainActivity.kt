package com.tp_geoloc_authy

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), LocationListener {
    var firebaseAuth: FirebaseAuth? = null
    val firebaseRepo : FirebaseRepo = FirebaseRepo()
    var locationList:List<LocationsModel> = ArrayList()
    val locationListAdapter=LocationListAdapter(locationList)
    var index = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth=FirebaseAuth.getInstance()
/*

        if(firebaseAuth==null){
            firebaseAuth!!.createUserWithEmailAndPassword(email)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in: success
                        // update UI for current User
                        val user = firebaseAuth!!.currentUser
                    } else {
                        // Sign in: fail
                    }
                }
        }
        else{
            // TODO : ID biométrique
        }
        */


        if(firebaseRepo.getUser()==null){
            firebaseRepo.createUser().addOnCompleteListener{
                if(it.isSuccessful){
                    loadPostData()
                }
                else {
                    loadPostData()
                }
            }
        }
        else {
            // TODO : ID biométrique
            loadPostData()
        }
        recyclerLocations.layoutManager = LinearLayoutManager(this)
        recyclerLocations.adapter = locationListAdapter
        checkLocation()
    }

    private fun loadPostData() {
        firebaseRepo.getLocationsList().addOnCompleteListener {
            if(it.isSuccessful){
                locationList = it.result!!.toObjects(LocationsModel::class.java)
                locationListAdapter.locationListItems = locationList
                locationListAdapter.notifyDataSetChanged()
            }
            else {
                Log.d("Logging","Erreur")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkLocation()
    }

    private fun checkLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0F, this)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_ACCESS_FINE_LOCATION)
        }
    }

    override fun onLocationChanged(location: Location) {
        val textViewLocation: TextView = findViewById(R.id.textViewLocation)
        textViewLocation.text = "longitude : ${location.longitude} , latitude ${location.latitude}"
        val mapLocation = hashMapOf(
                "latitude" to location.latitude,
                "longitude" to location.longitude,
                "index" to index
        )
        Firebase.firestore.collection("locations").add(mapLocation)
                .addOnSuccessListener {
                    Toast.makeText(this, "Success $index", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.e("myLocationApp", it.message.toString())
                }
        index++

        val btnLocation: Button = findViewById(R.id.btnSaveToFireStore)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == REQUEST_PERMISSION_ACCESS_FINE_LOCATION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                checkLocation()
        }

    }

    companion object{
        const val REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1
    }

}