package com.example.colombogpschanger

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var locationManager: LocationManager
    private lateinit var status: TextView
    private val colomboLat = 6.9271
    private val colomboLon = 79.8612
    private val permissionRequest = 41

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        buildScreen()
        updateStatus("Ready. Select this app as your mock location app in Developer Options.")
    }

    private fun buildScreen() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(28, 36, 28, 28)
            setBackgroundColor(0xFFF4F7FB.toInt())
        }

        val title = TextView(this).apply {
            text = "Colombo GPS Changer"
            textSize = 25f
            setTextColor(0xFF12345A.toInt())
            gravity = Gravity.CENTER
        }
        val subtitle = TextView(this).apply {
            text = "Set a simulated GPS position"
            textSize = 15f
            setTextColor(0xFF526477.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 10, 0, 26)
        }
        val location = TextView(this).apply {
            text = "COLOMBO, SRI LANKA\nLatitude  6.9271\nLongitude 79.8612"
            textSize = 19f
            setTextColor(0xFF18324D.toInt())
            gravity = Gravity.CENTER
            setPadding(16, 26, 16, 26)
            setBackgroundColor(0xFFFFFFFF.toInt())
        }
        val startButton = Button(this).apply {
            text = "SET COLOMBO LOCATION"
            isAllCaps = false
            setOnClickListener { setColomboLocation() }
        }
        val stopButton = Button(this).apply {
            text = "STOP MOCK LOCATION"
            isAllCaps = false
            setOnClickListener { stopMockLocation() }
        }
        val instructions = TextView(this).apply {
            text = "SETUP\n1. Enable Developer Options on your phone.\n2. Open Developer Options → Select mock location app.\n3. Choose Colombo GPS Changer.\n4. Return here and tap Set Colombo Location.\n\nSome apps detect or reject simulated locations."
            textSize = 14f
            setTextColor(0xFF34495E.toInt())
            setPadding(4, 24, 4, 0)
        }
        status = TextView(this).apply {
            textSize = 14f
            setTextColor(0xFF0D47A1.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 18, 0, 8)
        }

        root.addView(title)
        root.addView(subtitle)
        root.addView(location, LinearLayout.LayoutParams(-1, -2))
        root.addView(startButton, LinearLayout.LayoutParams(-1, -2).apply { topMargin = 24 })
        root.addView(stopButton, LinearLayout.LayoutParams(-1, -2).apply { topMargin = 8 })
        root.addView(status, LinearLayout.LayoutParams(-1, -2))
        root.addView(instructions)
        setContentView(root)
    }

    private fun setColomboLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                permissionRequest
            )
            return
        }
        try {
            val provider = LocationManager.GPS_PROVIDER
            try {
                locationManager.removeTestProvider(provider)
            } catch (_: Exception) {
            }

            locationManager.addTestProvider(
                provider,
                false, false, false, false,
                true, true, true,
                Criteria.POWER_LOW, Criteria.ACCURACY_FINE
            )
            locationManager.setTestProviderEnabled(provider, true)

            val mock = Location(provider).apply {
                latitude = colomboLat
                longitude = colomboLon
                altitude = 10.0
                accuracy = 5f
                time = System.currentTimeMillis()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    elapsedRealtimeNanos = android.os.SystemClock.elapsedRealtimeNanos()
                }
                speed = 0f
                bearing = 0f
            }
            locationManager.setTestProviderLocation(provider, mock)
            updateStatus("Mock location sent: Colombo (6.9271, 79.8612)")
            Toast.makeText(this, "Colombo mock location set", Toast.LENGTH_LONG).show()
        } catch (e: SecurityException) {
            updateStatus("Permission or mock-location setup required. Check Developer Options.")
        } catch (e: Exception) {
            updateStatus("Could not set location: ${e.message ?: "Unknown error"}")
        }
    }

    private fun stopMockLocation() {
        try {
            locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, false)
            locationManager.removeTestProvider(LocationManager.GPS_PROVIDER)
            updateStatus("Mock provider stopped. Android will use normal location sources.")
        } catch (_: Exception) {
            updateStatus("No active mock provider, or Android could not stop it.")
        }
    }

    private fun updateStatus(message: String) {
        status.text = message
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            setColomboLocation()
        } else if (requestCode == permissionRequest) {
            updateStatus("Location permission was not granted.")
        }
    }
}
