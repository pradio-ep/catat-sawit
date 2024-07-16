package pradio.ep.catatsawit.ui.input

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pradio.ep.catatsawit.Const
import pradio.ep.catatsawit.databinding.ActivityInputBinding
import pradio.ep.catatsawit.domain.model.Note
import pradio.ep.catatsawit.ui.main.MainActivity


class InputActivity : AppCompatActivity() {

    companion object {
        fun navigate(context: Context) {
            context.startActivity(Intent(context, InputActivity::class.java))
        }

        fun navigate(context: Context, key: String?, data: Note?) {
            context.startActivity(Intent(context, InputActivity::class.java).apply {
                putExtra(Const.KEY, key)
                putExtra(Const.NOTE, data)
            })
        }
    }

    private val viewModel: InputViewModel by viewModels()

    private val binding: ActivityInputBinding by lazy {
        ActivityInputBinding.inflate(layoutInflater)
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
            title = "Input"
        }
    }

    private fun setupView() {
        with(binding) {
            val key: String? = intent.getStringExtra(Const.KEY)
            val note: Note? = intent.getParcelableExtra(Const.NOTE)
            val netWeightWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    text: CharSequence, start: Int,
                    count: Int, after: Int
                ) {}
                override fun onTextChanged(
                    text: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    if (text.isNotEmpty() && etInbound.text?.isNotEmpty() == true && etOutbound.text?.isNotEmpty() == true) {
                        tvNetWeight.text = Note.netWeight(
                            etInbound.text.toString().toDoubleOrNull(),
                            etOutbound.text.toString().toDoubleOrNull()
                        ) + " Ton"
                    }
                }
            }

            etDate.setText(note?.date)
            etDriver.setText(note?.driver)
            etLicense.setText(note?.license)
            etInbound.apply {
                setText(if (note?.inbound != null) note.inbound.toString() else "")
                addTextChangedListener(netWeightWatcher)
            }
            etOutbound.apply {
                setText(if (note?.outbound != null) note.outbound.toString() else "")
                addTextChangedListener(netWeightWatcher)
            }
            tvNetWeight.text = if (note?.inbound != null && note?.outbound != null) Note.netWeight(note.inbound, note.outbound) + " Ton" else "... Ton"
            btnSave.setOnClickListener {
                viewModel.saveData(
                    key,
                    Note(
                        id = System.currentTimeMillis().toString(),
                        date = etDate.text.toString(),
                        driver = etDriver.text.toString(),
                        license = etLicense.text.toString(),
                        inbound = etInbound.text.toString().toDouble(),
                        outbound = etOutbound.text.toString().toDouble()
                    )
                )
                MainActivity.navigate(this@InputActivity)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}