package io.zelbess.grpcandroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.zelbess.grpcandroid.background.GrpcService
import io.zelbess.grpcandroid.driver.DriverActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService(Intent(this, GrpcService::class.java))

        driver.setOnClickListener {
            startActivity(Intent(this, DriverActivity::class.java))
        }

        passenger.setOnClickListener {
            startActivity(Intent(this, StreamActivity::class.java))
        }
    }
}
