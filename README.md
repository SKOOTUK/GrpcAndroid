# GrpcAndroid
Sample android client doing various things :>

## How to run

Launch it on android emulator to avoid meddling with adb to make your device talk to the server.
Port and host are already prefilled in the app and hopefully shoud point exactly to your running server.

## Where is ze server?:

https://github.com/Aetherna/GrpcAndroidServer

## Changelog

- Simple request/ response scenario in SendRequestActivity
- Server streaming using rxjava scenario in StreamActivity - user listening on updates coming from server
- Client streaming using rxjava scenario in LocationService - using background service to constantly notify server of user whereabouts
