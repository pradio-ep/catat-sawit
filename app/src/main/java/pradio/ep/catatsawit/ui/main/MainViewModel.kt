package pradio.ep.catatsawit.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import pradio.ep.catatsawit.Const
import pradio.ep.catatsawit.util.state.ConnectionState

class MainViewModel : ViewModel() {

    private val _connectionState = MutableLiveData<ConnectionState>()
    val connectionState: LiveData<ConnectionState> = _connectionState

    private val database: FirebaseDatabase by lazy {
        Firebase.database
    }

    fun getData(listener: ValueEventListener) {
        val noteRefByKey = database.getReference(Const.NOTE).orderByKey()
        noteRefByKey.addValueEventListener(listener)
    }

    fun getDataSortByDriver(listener: ValueEventListener) {
        val noteRefByDriver = database.getReference(Const.NOTE).orderByChild(Const.DRIVER)
        noteRefByDriver.addValueEventListener(listener)
    }

    fun getDataSortByLicense(listener: ValueEventListener) {
        val noteRefByLicense = database.getReference(Const.NOTE).orderByChild(Const.LICENSE)
        noteRefByLicense.addValueEventListener(listener)
    }

    fun getConnectionStatus() {
        val connectedRef = Firebase.database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    _connectionState.value = ConnectionState.Online
                } else {
                    _connectionState.value = ConnectionState.Offline
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("Connection", "Listener was cancelled")
            }
        })
    }
}