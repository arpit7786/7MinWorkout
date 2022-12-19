package com.example.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.a7minworkout.databinding.ActivityExcerciseBinding

class ExcerciseActivity : AppCompatActivity() {

    private var binding:ActivityExcerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.ExcerciseActivityToolbar)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

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

        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress

        restTimer = object: CountDownTimer(10000,1000) {
            override fun onTick(p0: Long) {
                restProgress++

                binding?.progressBar?.progress = (10 - restProgress)
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                Toast.makeText(
                    this@ExcerciseActivity,
                "Here Now we'll start the exercise",
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