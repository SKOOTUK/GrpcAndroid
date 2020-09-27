package io.zelbess.grpcandroid

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.grpc.ManagedChannelBuilder
import io.zelbess.grpcprototype.GreeterGrpc
import io.zelbess.grpcprototype.HelloReply
import io.zelbess.grpcprototype.HelloRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        send.setOnClickListener {
            val host = host.text.toString()
            val port = port.text.toString().toInt()
            val message = message.text.toString()

            GlobalScope.launch(Dispatchers.Main) {
                val reply = sendMessage(host, port, message)
                Toast.makeText(this@MainActivity, "Reply: ${reply?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun sendMessage(host: String, port: Int, message: String): HelloReply? {

        return CoroutineScope(Dispatchers.IO).async {
            val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
            val stub = GreeterGrpc.newBlockingStub(channel)
            val request = HelloRequest.newBuilder().setName(message).build()
            return@async stub.sayHello(request).also {
                channel.shutdown()
            }
        }.await()
    }
}