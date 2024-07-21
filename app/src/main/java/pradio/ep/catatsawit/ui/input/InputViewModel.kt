package pradio.ep.catatsawit.ui.input

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import pradio.ep.catatsawit.Const
import pradio.ep.catatsawit.data.model.Note
import pradio.ep.catatsawit.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class InputViewModel @Inject constructor(
    private val repo: NoteRepository
): ViewModel() {

    fun saveNote(note: Note) {
        repo.insert(note)
    }

    fun updateNote(note: Note) {
        repo.update(note)
    }
}