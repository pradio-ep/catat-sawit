package pradio.ep.catatsawit.ui.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import pradio.ep.catatsawit.Const
import pradio.ep.catatsawit.databinding.ActivityDetailBinding
import pradio.ep.catatsawit.domain.model.Note
import pradio.ep.catatsawit.ui.input.InputActivity

class DetailActivity : AppCompatActivity() {

    companion object {
        fun navigate(context: Context, key: String, data: Note) {
            context.startActivity(Intent(context, DetailActivity::class.java).apply {
                putExtra(Const.KEY, key)
                putExtra(Const.NOTE, data)
            })
        }
    }

    private val viewModel: DetailViewModel by viewModels()

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            elevation = 0f
            title = "Detail"
        }
    }

    private fun setupView() {
        with(binding) {
            val key: String? = intent.getStringExtra(Const.KEY)
            val note: Note? = intent.getParcelableExtra(Const.NOTE)

            tvDate.text = note?.date
            tvDriver.text = note?.driver
            tvLicense.text = note?.license
            tvInbound.text = note?.inbound.toString() + " Ton"
            tvOutbound.text = note?.outbound.toString() + " Ton"
            tvNetWeight.text = Note.netWeight(note?.inbound, note?.outbound) + " Ton"

            btnEdit.setOnClickListener {
                InputActivity.navigate(this@DetailActivity, key, note)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}