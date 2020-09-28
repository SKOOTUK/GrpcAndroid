package io.zelbess.grpcandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import io.zelbess.grpcprototype.stream.HelloReply
import io.zelbess.grpcprototype.stream.HelloRequest
import io.zelbess.grpcprototype.stream.StreamingGreeterGrpc
import kotlinx.android.synthetic.main.activity_client_stream.*

class ClientStreamActivity : AppCompatActivity() {

    lateinit var channel: ManagedChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_stream)

        val streamRequests = setupStream()

        stream.setOnClickListener {
            val message = message.text.toString()
            val request = HelloRequest.newBuilder().setName(message).build()
            streamRequests.onNext(request)

        }
    }

    private fun setupStream(): StreamObserver<HelloRequest> {
        val host = host.text.toString()
        val port = port.text.toString().toInt()
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()

        val streamObserver = object : StreamObserver<HelloReply> {
            override fun onNext(value: HelloReply?) {
                showMessage("Reply: ${value?.message}")
            }

            override fun onError(t: Throwable?) {
                showMessage("Error: ${t?.message.orEmpty()}")
            }

            override fun onCompleted() {
            }
        }
        return StreamingGreeterGrpc.newStub(channel).sayHelloStreaming(streamObserver)
    }

    fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        channel.shutdown()
        super.onDestroy()
    }
}