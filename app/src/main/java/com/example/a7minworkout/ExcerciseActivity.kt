package com.example.a7minworkout

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.WindowId
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minworkout.databinding.ActivityExcerciseBinding
import java.util.*
import kotlin.collections.ArrayList

class ExcerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding:ActivityExcerciseBinding? = null

    private var timer: CountDownTimer? = null
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercise = -1

    private var exerciseAdapter: ExerciseStatusAdapter? = null

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

        tts = TextToSpeech(this, this)

        setUpRestView()
        setUpExerciseStatusRecyclerView()

    }

    private fun setUpExerciseStatusRecyclerView() {
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setUpRestView() {

        try {
            val soundUri = Uri.parse("android.resource://com.example.a7minworkout/" + R.raw.app_src_main_res_raw_press_start)

            player = MediaPlayer.create(applicationContext, soundUri)
            player?.isLooping = false

            player?.start()

        } catch (e: Exception){
            e.printStackTrace()
        }

        if(timer != null) {
            timer?.cancel()
            exerciseProgress = 0
        }

        binding?.flProgressBarExerciseTimer?.visibility = View.INVISIBLE
        binding?.flRestProgressTimer?.visibility = View.VISIBLE
        binding?.tvExercise?.visibility = View.INVISIBLE
        binding?.ivExercise?.visibility = View.INVISIBLE

        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = exerciseProgress
        binding?.textViewForProgress?.text = "Take a rest!! and Get Ready For"
        binding?.tvUpComingExerciseLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercise+1].getName()

        timer = object: CountDownTimer(10000,1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++

                binding?.tvTimer?.text = (10 - exerciseProgress).toString()
                binding?.progressBar?.progress = (10 - exerciseProgress)

            }

            override fun onFinish() {

                currentExercise++

                exerciseProgress = 0
                timer?.cancel()

                binding?.flRestProgressTimer?.visibility = View.GONE
                binding?.flProgressBarExerciseTimer?.visibility = View.VISIBLE

                exerciseList!![currentExercise].setIsSelected(true)
                exerciseAdapter?.notifyDataSetChanged()

                setExerciseTimer()

            }

        }.start()
    }

    private fun setExerciseTimer() {
//        binding?.flRestProgressTimer?.visibility = View.GONE
        binding?.textViewForProgress?.text = "Burn some calories!!"
        binding?.ivExercise?.setImageResource(exerciseList?.get(currentExercise)!!.getImage())

        binding?.ivExercise?.visibility = View.VISIBLE
        binding?.tvUpComingExerciseLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE
        binding?.progressBarExerciseTimer?.progress = exerciseProgress
//        currentExercise++
        binding?.tvExercise?.visibility = View.VISIBLE
        binding?.tvExercise?.text = exerciseList?.get(currentExercise)?.getName()

        speakOutExerciseName(exerciseList!![currentExercise].getName())

        timer = object: CountDownTimer(30000, 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++

                binding?.tvExerciseTimer?.text = (30 - exerciseProgress).toString()
                binding?.progressBarExerciseTimer?.progress = (30 - exerciseProgress)

            }

            override fun onFinish() {

                if(currentExercise < exerciseList!!.size-1){

                    exerciseList!![currentExercise].setIsSelected(false)
                    exerciseList!![currentExercise].setIsCompleted(true)
                    exerciseAdapter?.notifyDataSetChanged()

                    setUpRestView()

                }else{
                    Toast.makeText(
                        this@ExcerciseActivity,
                        "Congrats! You completed 7 minutes of workout",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }.start()
    }


    override fun onDestroy() {
        super.onDestroy()

        if(timer != null) {
            timer?.cancel()
            exerciseProgress = 0
        }

        if(tts != null) {
            tts?.stop()
            tts?.shutdown()
        }

        if(player != null){
            player?.stop()
        }

        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The language specified is unavailable")
            }
        } else {
            Log.e("TTS", "Initialization Failed")
        }
    }

    private fun speakOutExerciseName(name: String) {
        tts?.speak(name, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}