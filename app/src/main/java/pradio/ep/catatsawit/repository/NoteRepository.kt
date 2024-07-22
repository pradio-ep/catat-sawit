package pradio.ep.catatsawit.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pradio.ep.catatsawit.data.dao.NoteDao
import pradio.ep.catatsawit.data.model.Note
import pradio.ep.catatsawit.util.state.ConnectionState
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getConnectionStatus(): MutableLiveData<ConnectionState> {
        val mutableLiveData = MutableLiveData<ConnectionState>()
        val connectionReference = Firebase.database.getReference(".info/connected")
        connectionReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    mutableLiveData.value = ConnectionState.Online
                } else {
                    mutableLiveData.value = ConnectionState.Offline
                }
            }
            override fun onCancelled(error: DatabaseError) {
                mutableLiveData.value = ConnectionState.Offline
            }
        })
        return mutableLiveData
    }

    fun insert(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.insert(note)
        }
    }

    fun update(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.update(note)
        }
    }

    fun delete(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.delete(note)
        }
    }

    fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    fun getAllNotesByDriver(): Flow<List<Note>> {
        return noteDao.getAllNotesByDriver()
    }

    fun getAllNotesByLicense(): Flow<List<Note>> {
        return noteDao.getAllNotesByLicense()
    }
}