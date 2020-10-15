package io.zelbess.grpcandroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.zelbess.grpcandroid.config.DRIVER_ID
import io.zelbess.grpcandroid.config.PASSENGER_ID
import io.zelbess.grpcandroid.service.GrpcService
import io.zelbess.grpcandroid.service.USER_ID_KEY
import io.zelbess.grpcandroid.storage.UserState
import io.zelbess.grpcandroid.ui.driver.DriverActivity
import io.zelbess.grpcandroid.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val userState: UserState by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        driver.setOnClickListener {
            userState.userId = DRIVER_ID
            startActivity(Intent(this, HomeActivity::class.java))
        }

        passenger.setOnClickListener {
            userState.userId = PASSENGER_ID
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}
