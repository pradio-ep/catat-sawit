package pradio.ep.catatsawit.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pradio.ep.catatsawit.data.dao.NoteDao
import pradio.ep.catatsawit.data.db.AppDatabase
import pradio.ep.catatsawit.repository.NoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "note_db").build()
    }

    @Singleton
    @Provides
    fun providesNoteDao(db: AppDatabase): NoteDao {
        return db.noteDao()
    }

    @Provides
    fun providesRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepository(noteDao)
    }

    @Singleton
    @Provides
    fun providesFirebaseDatabse(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}