package pradio.ep.catatsawit.ui.main


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import pradio.ep.catatsawit.R
import pradio.ep.catatsawit.databinding.ActivityMainBinding
import pradio.ep.catatsawit.data.model.Note
import pradio.ep.catatsawit.ui.input.InputActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pradio.ep.catatsawit.util.state.ConnectionState
import pradio.ep.catatsawit.util.state.SortingState
import javax.inject.Inject

@AndroidEntryPoint
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

    private val mainAdapter: MainAdapter by lazy {
        MainAdapter(this)
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupView()
        setupObserver()
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
            radioSortBy.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radioDate -> viewModel.setSorting(SortingState.Date)
                    R.id.radioDriver -> viewModel.setSorting(SortingState.Driver)
                    R.id.radioLicense -> viewModel.setSorting(SortingState.License)
                }
            }
            radioDate.performClick()
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
            getConnectionStatus().observe(this@MainActivity) { connection ->
                if (connection is ConnectionState.Online) {
                    binding.tvStatus.apply {
                        text = "Online"
                        setTextColor(getColor(R.color.green))
                    }
                } else {
                    binding.tvStatus.apply {
                        text = "Offline"
                        setTextColor(getColor(R.color.red))
                    }
                }
            }
            sortingState.observe(this@MainActivity) { sorting ->
                lifecycleScope.launch {
                    when (sorting) {
                        SortingState.Date -> viewModel.getNotes().collectLatest { updateList(it) }
                        SortingState.Driver -> viewModel.getNotesByDriver().collectLatest { updateList(it) }
                        SortingState.License -> viewModel.getNotesByLicense().collectLatest { updateList(it) }
                    }
                }
            }
        }
    }

    private fun updateList(list: List<Note>) {
        mainAdapter.clearItems()
        mainAdapter.setItems(list.toMutableList())
        firebaseDatabase.reference.setValue(list)
    }
}