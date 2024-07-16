package pradio.ep.catatsawit.ui.main


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import pradio.ep.catatsawit.R
import pradio.ep.catatsawit.databinding.ActivityMainBinding
import pradio.ep.catatsawit.domain.model.Note
import pradio.ep.catatsawit.ui.input.InputActivity
import androidx.activity.viewModels
import pradio.ep.catatsawit.util.state.ConnectionState


class MainActivity : AppCompatActivity() {

    companion object {
        fun navigate(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    private val viewModel: MainViewModel by viewModels()

    private val items = mutableListOf<Pair<String, Note>>()

    private val mainAdapter: MainAdapter by lazy {
        MainAdapter(this)
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val noteListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            mainAdapter.clearItems()
            val notes = dataSnapshot.children
            notes.forEach {
                it.getValue<Note>()?.let { note ->
                    it.key?.let { key ->
                        items.add(Pair(key, note))
                    }
                }
            }
            mainAdapter.setItems(items)
        }
        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("Listener", "onCancelled", databaseError.toException())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupView()
        setupObserver()

        viewModel.getData(noteListener)
        viewModel.getConnectionStatus()
    }

    private fun setupToolbar() {
        supportActionBar?.hide()
    }

    private fun setupView() {
        with(binding) {
            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    query: CharSequence, start: Int,
                    count: Int, after: Int
                ) {}
                override fun onTextChanged(
                    query: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    mainAdapter.filter.filter(query)
                }
                override fun afterTextChanged(editable: Editable?) {}
            })
            radioSortBy.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.radioDate -> { viewModel.getData(noteListener) }
                    R.id.radioDriver -> { viewModel.getDataSortByDriver(noteListener) }
                    R.id.radioLicense -> { viewModel.getDataSortByLicense(noteListener) }
                }
            }
            rvInput.apply {
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                adapter = mainAdapter
            }
            fabAdd.setOnClickListener {
                InputActivity.navigate(this@MainActivity)
            }
        }
    }

    private fun setupObserver() {
        with(viewModel) {
            connectionState.observe(this@MainActivity) { connection ->
                if (connection is ConnectionState.Online) {
                    binding.tvStatus.apply {
                        text = "Online"
                        setTextColor(getColor(R.color.green))
                    }
                    Log.d("Status", "Online")
                } else {
                    binding.tvStatus.apply {
                        text = "Offline"
                        setTextColor(getColor(R.color.red))
                    }
                    Log.d("Status", "Offline")
                }
            }
        }
    }
}