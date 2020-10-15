package io.zelbess.grpcandroid.ui.driver

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import io.zelbess.grpcandroid.R
import io.zelbess.grpcandroid.service.GrpcService
import io.zelbess.grpcandroid.ui.driver.DriverViewModel.UiState
import kotlinx.android.synthetic.main.activity_driver.*
import org.koin.android.viewmodel.ext.android.viewModel


class DriverActivity : AppCompatActivity() {

    private val viewModel: DriverViewModel by viewModel()
    private var locationService: GrpcService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            locationService = null
        }

        override fun onServiceConnected(p0: ComponentName, binder: IBinder) {
            locationService = (binder as GrpcService.GrpcServiceBinder).getService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)

        createTrip.setOnClickListener {
            viewModel.createTrip()
        }

        viewModel.viewState.observe(this, Observer {
            when (it) {
                is UiState.TripCreated -> {
                    locationService?.followTrip(it.driverId, it.tripId)
                    tripView.isVisible = true
                }
                is UiState.TripUpdate -> {
                    tripUpdates.text = it.message
                }
                is UiState.ShowError -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, GrpcService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        unbindService(serviceConnection)
        super.onPause()
    }
}