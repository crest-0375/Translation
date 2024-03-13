package com.app.translation

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.app.translation.databinding.ActivityMainBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.HINDI)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        val englishGermanTranslator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        englishGermanTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                binding.textView.visibility = View.VISIBLE
                binding.editTextText.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                binding.textView.visibility = View.VISIBLE
                binding.editTextText.visibility = View.GONE

                binding.textView.text = getString(R.string.error_in_downloading_model)
            }
        binding.editTextText.doOnTextChanged { text, _, _, _ ->
            englishGermanTranslator.translate(text.toString())
                .addOnSuccessListener { translatedText ->
                    binding.textView.text = translatedText
                }
                .addOnFailureListener { exception ->
                    binding.textView.text = exception.localizedMessage
                }
        }
    }
}
