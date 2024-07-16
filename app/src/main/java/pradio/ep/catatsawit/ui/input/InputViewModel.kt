package pradio.ep.catatsawit.ui.input

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.database
import pradio.ep.catatsawit.Const
import pradio.ep.catatsawit.domain.model.Note

class InputViewModel: ViewModel() {

    fun saveData(key: String?, inputNote: Note) {
        val database = Firebase.database
        val noteRef = database.getReference(Const.NOTE)

        if (key != null) noteRef.child(key).setValue(inputNote)
        else noteRef.push().setValue(inputNote)
    }
}