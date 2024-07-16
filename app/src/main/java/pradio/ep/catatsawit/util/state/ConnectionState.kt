package pradio.ep.catatsawit.util.state

sealed class ConnectionState {
    object Online: ConnectionState()
    object Offline: ConnectionState()
}