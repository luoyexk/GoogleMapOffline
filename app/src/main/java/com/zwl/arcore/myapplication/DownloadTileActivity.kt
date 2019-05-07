package com.zwl.arcore.myapplication

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.gms.maps.model.TileProvider
import java.lang.ref.WeakReference

class DownloadTileActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_tile)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        val coordTileProvider = GetAxisTileProvider(this)
        map?.addTileOverlay(TileOverlayOptions().tileProvider(coordTileProvider))
    }


    private class GetAxisTileProvider(val context:Context) : TileProvider {
        val weakReference = WeakReference<Context>(context)
        override fun getTile(x: Int, y: Int, zoom: Int): Tile? {
            val path  = "http://mt0.google.cn/vt/lyrs=s&hl=zh-CN&gl=cn&x=$x&y=$y&z=$zoom"
//            Glide.with(weakReference.get()).load(path).asBitmap().
            return null
        }

    }
}
