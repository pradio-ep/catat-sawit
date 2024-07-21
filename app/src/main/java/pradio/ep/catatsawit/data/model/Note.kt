package pradio.ep.catatsawit.data.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tbl_note")
@Parcelize
data class Note(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "date")val date: String? = null,
    @ColumnInfo(name = "driver")val driver: String? = null,
    @ColumnInfo(name = "license")val license: String? = null,
    @ColumnInfo(name = "inbound")val inbound: Double? = 0.0,
    @ColumnInfo(name = "outbound")val outbound: Double? = 0.0
) : Parcelable {
    companion object {

        fun netWeight(inbound: Double?, outbound: Double?): String {
            return outbound?.let { inbound?.minus(it)?.let { it1 -> roundTheNumber(it1) } }.toString()
        }

        private fun roundTheNumber(numInDouble: Double): String {
            return "%.1f".format(numInDouble)
        }
    }
}
