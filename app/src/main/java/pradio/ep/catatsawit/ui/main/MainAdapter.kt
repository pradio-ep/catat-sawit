package pradio.ep.catatsawit.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import pradio.ep.catatsawit.R
import pradio.ep.catatsawit.databinding.ItemRowBinding
import pradio.ep.catatsawit.domain.model.Note
import pradio.ep.catatsawit.ui.detail.DetailActivity

class MainAdapter(private val context: Context) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>(), Filterable {

    private var items = mutableListOf<Pair<String, Note>>()
    private var filterItems = mutableListOf<Pair<String, Note>>()

    init {
        filterItems = items
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ItemRowBinding = ItemRowBinding.bind(itemView)

        fun bind(data: Pair<String, Note>) {
            binding.apply {
                tvDriver.text = data.second.driver
                tvLicense.text = data.second.license
                tvDate.text = data.second.date
                tvNetWeight.text = Note.netWeight(data.second.inbound, data.second.outbound) + " Ton"
            }
            with(itemView) {
                setOnClickListener {
                    DetailActivity.navigate(context, data.first, data.second)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_row, viewGroup, false)
        )
    }

    fun setItems(data: MutableList<Pair<String, Note>>) {
        this.items = data
        this.filterItems = data
        notifyDataSetChanged()
    }

    fun clearItems() {
        items.clear()
        filterItems.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = filterItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filterItems[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterItems = items as ArrayList<Pair<String, Note>>
                } else {
                    val resultList = ArrayList<Pair<String, Note>>()
                    for (row in items) {
                        if (row.second.date?.lowercase()?.contains(constraint.toString().lowercase()) == true ||
                            row.second.driver?.lowercase()?.contains(constraint.toString().lowercase()) == true ||
                            row.second.license?.lowercase()?.contains(constraint.toString().lowercase()) == true) {
                            resultList.add(row)
                        }
                    }
                    filterItems = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterItems
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterItems = results?.values as ArrayList<Pair<String, Note>>
                notifyDataSetChanged()
            }
        }
    }
}