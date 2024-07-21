package pradio.ep.catatsawit.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pradio.ep.catatsawit.data.model.Note
import pradio.ep.catatsawit.repository.NoteRepository
import pradio.ep.catatsawit.util.state.ConnectionState
import pradio.ep.catatsawit.util.state.SortingState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: NoteRepository
) : ViewModel() {

    private val _sortingState = MutableLiveData<SortingState>()
    val sortingState: LiveData<SortingState> = _sortingState

    fun getConnectionStatus(): LiveData<ConnectionState> {
        return repo.getConnectionStatus()
    }

    fun setSorting(sortingState: SortingState) {
        _sortingState.value = sortingState
    }

    fun getNotes(): LiveData<List<Note>> {
        return repo.getAllNotes()
    }

    fun getNotesByDriver(): LiveData<List<Note>> {
        return repo.getAllNotesByDriver()
    }

    fun getNotesByLicense(): LiveData<List<Note>> {
        return repo.getAllNotesByLicense()
    }
}