package com.zwl.gmap.offline

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        if (view == goToDemo) {
            startActivity(Intent(this, MapsActivity::class.java))
        } else if (view == goToMapFragment) {
            startActivity(Intent(this, MapFragmentAct::class.java))
        }
    }
}
