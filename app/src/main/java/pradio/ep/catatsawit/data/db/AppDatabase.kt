package pradio.ep.catatsawit.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import pradio.ep.catatsawit.data.dao.NoteDao
import pradio.ep.catatsawit.data.model.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}