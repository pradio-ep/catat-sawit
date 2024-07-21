package pradio.ep.catatsawit.ui.input

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import pradio.ep.catatsawit.Const
import pradio.ep.catatsawit.databinding.ActivityInputBinding
import pradio.ep.catatsawit.data.model.Note
import pradio.ep.catatsawit.ui.main.MainActivity
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class InputActivity : AppCompatActivity() {

    companion object {
        fun navigate(context: Context) {
            context.startActivity(Intent(context, InputActivity::class.java))
        }

        fun navigate(context: Context, data: Note?) {
            context.startActivity(Intent(context, InputActivity::class.java).apply {
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
//            val key: String? = intent.getStringExtra(Const.KEY)
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
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val currentDate = sdf.format(Date())

            etDate.setText(note?.date ?: currentDate)
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
            tvNetWeight.text = if (note?.inbound != null && note.outbound != null) Note.netWeight(note.inbound, note.outbound) + " Ton" else "... Ton"
            btnSave.setOnClickListener {
                if (validation()) {
                    val inputNote = Note(
                        id = note?.id ?: System.currentTimeMillis().toString(),
                        date = etDate.text.toString(),
                        driver = etDriver.text.toString(),
                        license = etLicense.text.toString(),
                        inbound = etInbound.text.toString().toDouble(),
                        outbound = etOutbound.text.toString().toDouble()
                    )

                    if (note != null) viewModel.updateNote(inputNote)
                    else viewModel.saveNote(inputNote)

                    MainActivity.navigate(this@InputActivity)
                }
            }
        }
    }

    private fun validation(): Boolean {
        return return binding.etLicense.text?.isNotEmpty() == true &&
                binding.etInbound.text?.isNotEmpty() == true &&
                binding.etOutbound.text?.isNotEmpty() == true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}