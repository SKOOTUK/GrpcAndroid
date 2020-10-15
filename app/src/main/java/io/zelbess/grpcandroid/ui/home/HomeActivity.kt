package io.zelbess.grpcandroid.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import io.zelbess.grpcandroid.R
import io.zelbess.grpcandroid.service.GrpcService
import io.zelbess.grpcandroid.service.USER_ID_KEY
import io.zelbess.grpcandroid.storage.UserState
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val userState: UserState by inject()
    private val viewModel: HomeViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        startBackgroundService()

        val adapter = FeedAdapter(onMessageClicked = {
            viewModel.onMessageClicked(it)
        })
        userFeed.adapter = adapter
        viewModel.viewState.observe(this, Observer {
            when (it) {
                is HomeViewModel.ViewState.ShowFeed -> adapter.submitList(it.feedItems)
            }
        })
        viewModel.loadFeed()
    }


    private fun startBackgroundService() {
        val startingIntent = Intent(this, GrpcService::class.java).apply {
            putExtra(USER_ID_KEY, userState.userId)
        }
        startService(startingIntent)
    }
}