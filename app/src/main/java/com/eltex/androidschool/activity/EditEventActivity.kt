package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.ActivityEditEventBinding
import com.eltex.androidschool.utils.toEditable
import com.eltex.androidschool.utils.toast

class EditEventActivity : AppCompatActivity() {
    private companion object {
        const val ID = "ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getLongExtra(ID, 0)
        val content = intent.getStringExtra(Intent.EXTRA_TEXT)
        binding.content.text = content?.toEditable()

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> {
                    val newContent = binding.content.text?.toString().orEmpty()
                    if (newContent.isNotBlank()) {
                        setResult(
                            RESULT_OK,
                            Intent().putExtra(Intent.EXTRA_TEXT, newContent).putExtra("ID", id)
                        )
                        finish()
                    } else {
                        toast(R.string.event_empty_error, true)
                    }
                    true
                }

                else -> false
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}