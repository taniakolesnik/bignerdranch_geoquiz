package com.example.geoquiz2

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.geoquiz2.databinding.ActivityMainBinding
import java.util.Date

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val quizViewModel : QuizViewModel by viewModels()
    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater = result.data?.getBooleanExtra(ANSWER_SHOWN, false) ?: false
        }
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called test")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener { view: View ->
            quizViewModel.markAsAnswered()
            checkAnswer(true)
            setButtonsVisibility()
        }

        binding.falseButton.setOnClickListener { view: View ->
            quizViewModel.markAsAnswered()
            checkAnswer(false)
            setButtonsVisibility()
        }

        binding.nextButton.setOnClickListener { view: View ->
            quizViewModel.isCheater = false
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.prevButton.setOnClickListener{
            quizViewModel.isCheater = false
            quizViewModel.moveToPrevious()
            updateQuestion()

        }

        binding.questionTextView.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener {
            val correctAnswer = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, correctAnswer)
            cheatLauncher.launch(intent)
        }

        updateQuestion()
        setButtonsVisibility()
    }

    private fun setButtonsVisibility(){
        if (quizViewModel.currentQuestionIsAnswered){
            binding.trueButton.isEnabled=false
            binding.falseButton.isEnabled=false
        } else {
            binding.trueButton.isEnabled=true
            binding.falseButton.isEnabled=true
        }
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        setButtonsVisibility()
    }

    private fun checkAnswer(userAnswer : Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        if (userAnswer == correctAnswer){
            quizViewModel.updateScore()
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        if (quizViewModel.answeredNumber == quizViewModel.questionBankSize) {
            Log.d(TAG, "GAME OVER")
            Toast.makeText(this, getString(R.string.score_message, quizViewModel.currentQuizScore), Toast.LENGTH_SHORT)
                .show()
        }
    }

}