package pradio.ep.catatsawit.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pradio.ep.catatsawit.data.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note): Long

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM `tbl_note` where id = :id")
    suspend fun getNoteDetail(id: String): Note

    @Query("SELECT * FROM tbl_note")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM tbl_note ORDER BY driver")
    fun getAllNotesByDriver(): Flow<List<Note>>

    @Query("SELECT * FROM tbl_note ORDER BY license")
    fun getAllNotesByLicense(): Flow<List<Note>>
}