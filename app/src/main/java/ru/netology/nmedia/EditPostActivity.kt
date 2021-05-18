package ru.netology.nmedia

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.ActivityEditPostBinding
import ru.netology.nmedia.dto.Post

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            val text = it.getStringExtra(Intent.EXTRA_TEXT)

            if (!text.isNullOrBlank()) {
                binding.editorText.setText(text)
            }
        }

        binding.saveButton.setOnClickListener {
            val intent = Intent()

            if (binding.editorText.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.editorText.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }
}