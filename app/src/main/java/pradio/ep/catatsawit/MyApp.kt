package pradio.ep.catatsawit

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.database.database

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)
    }
}