package io.zelbess.grpcandroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_client_stream.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendRequest.setOnClickListener {
            startActivity(Intent(this, SendRequestActivity::class.java))
        }

        clientStream.setOnClickListener {
            startActivity(Intent(this, ClientStreamActivity::class.java))
        }
    }
}
