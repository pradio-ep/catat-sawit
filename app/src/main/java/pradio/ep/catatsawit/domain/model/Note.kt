package pradio.ep.catatsawit.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    val id: String? = null,
    val date: String? = null,
    val driver: String? = null,
    val license: String? = null,
    val inbound: Double? = 0.0,
    val outbound: Double? = 0.0
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
