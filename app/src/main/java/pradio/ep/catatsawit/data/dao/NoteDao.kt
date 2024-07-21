package pradio.ep.catatsawit.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pradio.ep.catatsawit.data.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note): Long

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM tbl_note")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM tbl_note ORDER BY driver")
    fun getAllNotesByDriver(): LiveData<List<Note>>

    @Query("SELECT * FROM tbl_note ORDER BY license")
    fun getAllNotesByLicense(): LiveData<List<Note>>
}