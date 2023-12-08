package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.ActivityNewEventBinding
import com.eltex.androidschool.utils.toast

class NewEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewEventBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> {
                    val content = binding.content.text?.toString().orEmpty()
                    if (content.isNotBlank()) {
                        setResult(RESULT_OK, Intent().putExtra(Intent.EXTRA_TEXT, content))
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