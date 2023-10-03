package com.example.geoquiz2

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.geoquiz2.databinding.ActivityCheatBinding

private const val CORRECT_ANSWER_BOOLEAN = "com.example.geoquiz2.answer_is_true"
private var correctAnswer = false
private var isAnswerShown = false

class CheatActivity() : AppCompatActivity() {

    private val cheatViewModel : CheatViewModel by viewModels()
    private lateinit var binding: ActivityCheatBinding

    companion object {
        fun newIntent(packageContext: Context, correctAnswer: Boolean) : Intent {
            return Intent(packageContext, CheatActivity::class.java).
            apply {
                putExtra(CORRECT_ANSWER_BOOLEAN, correctAnswer)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        correctAnswer = intent.getBooleanExtra(CORRECT_ANSWER_BOOLEAN, false)
        binding.showAnswerButton.setOnClickListener {
            binding.showAnswerButton.isEnabled = false
            setAnswerShownResults(cheatViewModel.isAnswerShown)
            showResults()
        }
    }

    private fun showResults() {
        val answerText = when {
            correctAnswer -> R.string.true_button
            else -> R.string.false_button
        }
        binding.answerTextView.setText(answerText)
        cheatViewModel.isAnswerShown = true
        setAnswerShownResults(true)
    }

    private fun setAnswerShownResults(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }
}