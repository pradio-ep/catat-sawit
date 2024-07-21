package pradio.ep.catatsawit.ui.detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pradio.ep.catatsawit.data.model.Note
import pradio.ep.catatsawit.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo: NoteRepository
): ViewModel() {

    fun deleteNote(note: Note) {
        repo.delete(note)
    }
}