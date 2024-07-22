package pradio.ep.catatsawit

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pradio.ep.catatsawit.data.dao.NoteDao
import pradio.ep.catatsawit.data.db.AppDatabase
import pradio.ep.catatsawit.data.model.Note
import java.util.concurrent.CountDownLatch


@RunWith(AndroidJUnit4::class)
@SmallTest
class NoteDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var noteDao: NoteDao

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        noteDao = database.noteDao()
    }

    @Test
    fun successInsertNote() = runBlocking {
        val note = Note(id = "1", "21-07-2024", "Jeje", "KT 1234 BB", 5.0, 2.5)

        noteDao.insert(note)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            noteDao.getAllNotes().collect {
                assert(it.contains(note))
                latch.countDown()
            }
        }

        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun successUpdateNote() = runBlocking {
        val note = Note(id = "1", "21-07-2024", "Jeje", "KT 1234 BB", 5.0, 2.5)
        noteDao.insert(note)

        val updatedNote = Note(id = "1", "21-07-2024", "Achmad", "KT 1234 BB", 5.0, 2.5)

        noteDao.update(updatedNote)

        val result = noteDao.getNoteDetail(updatedNote.id)

        assertEquals(result.driver, updatedNote.driver)
    }

    @Test
    fun successDeleteNote() = runBlocking {
        val note1 = Note(id = "1", "21-07-2024", "Jeje", "KT 1234 BB", 5.0, 2.5)
        val note2 = Note(id = "2", "21-07-2024", "Achmad", "KT 8888 BB", 4.0, 2.0)

        noteDao.insert(note1)
        noteDao.insert(note2)

        noteDao.delete(note1)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            noteDao.getAllNotes().collect {
                assert(it.contains(note1).not())
                latch.countDown()
            }
        }

        latch.await()
        job.cancelAndJoin()
    }

    @After
    fun closeDatabase() {
        database.close()
    }
}