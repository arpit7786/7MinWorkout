package com.example.a7minworkout

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import com.example.a7minworkout.databinding.ActivityExcerciseBinding

class ExcerciseActivity : AppCompatActivity() {

    private var binding:ActivityExcerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var exerciseList = ArrayList<ExerciseModel>()
    private var currentExercise = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.ExcerciseActivityToolbar)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        binding?.ExcerciseActivityToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        setUpRestView()

    }

    private fun setUpRestView() {

        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        binding?.flProgressBarExerciseTimer?.visibility = View.GONE
        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress
        binding?.textViewForProgress?.text = "Exercise Started"

        restTimer = object: CountDownTimer(10000,1000) {
            override fun onTick(p0: Long) {
                restProgress++

                binding?.progressBar?.progress = (10 - restProgress)
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {

                currentExercise++

                restProgress = 0
                restTimer?.cancel()

                binding?.flProgressBar?.visibility = View.GONE
                binding?.flProgressBarExerciseTimer?.visibility = View.VISIBLE

                setExerciseTimer()

            }

        }.start()
    }

    private fun setExerciseTimer() {
//        binding?.flProgressBar?.visibility = View.GONE

        binding?.progressBarExerciseTimer?.progress = restProgress

        restTimer = object: CountDownTimer(30000, 1000) {
            override fun onTick(p0: Long) {
                restProgress++

                binding?.progressBarExerciseTimer?.progress = (30 - restProgress)
                binding?.tvExerciseTimer?.text = (30 - restProgress).toString()

            }

            override fun onFinish() {
                Toast.makeText(
                    this@ExcerciseActivity,
                    "Here Now we'll start new exercise",
                    Toast.LENGTH_SHORT)
                    .show()
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        binding = null
    }
}