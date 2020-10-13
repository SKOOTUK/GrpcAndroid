package io.zelbess.grpcandroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.zelbess.grpcandroid.background.LocationService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService(Intent(this, LocationService::class.java))

        sendRequest.setOnClickListener {
            startActivity(Intent(this, SendRequestActivity::class.java))
        }

        clientStream.setOnClickListener {
            startActivity(Intent(this, StreamActivity::class.java))
        }
    }
}
