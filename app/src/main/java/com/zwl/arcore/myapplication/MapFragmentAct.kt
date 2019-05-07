package com.zwl.arcore.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragmentAct : AppCompatActivity(), OnMapReadyCallback {
    lateinit var fragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_fragment)

        val options = GoogleMapOptions()
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
            .compassEnabled(false)
            .rotateGesturesEnabled(false)
            .tiltGesturesEnabled(false);
        fragment = MapFragment.newInstance(options)
        fragment.onEnterAmbient(savedInstanceState)
        fragment.getMapAsync(this)
        fragmentManager.beginTransaction().add(R.id.fragment, fragment, "f").commit()
    }
    override fun onMapReady(map: GoogleMap?) {
        val latlng = LatLng(29.708640, -95.769102)
        map?.addMarker(MarkerOptions().position(latlng).title("huston"))
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,16f))
    }


    override fun onDestroy() {

        fragment.onExitAmbient()
        super.onDestroy()
    }
}
