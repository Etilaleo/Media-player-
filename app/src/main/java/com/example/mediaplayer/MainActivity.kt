package com.example.mediaplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.mediaplayer.databinding.ActivityMainBinding
import kotlinx.coroutines.Runnable

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    //Here we create a runnable for the handler
    private lateinit var runnable: Runnable
    private var handler = Handler(Looper.getMainLooper())
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //connecting toggle to the screen
        toggle = ActionBarDrawerToggle(this, binding.drawerView, R.string.open, R.string.close)
        binding.drawerView.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                R.id.feedback -> Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show()
                R.id.close -> finish()
            }
            true
        }

        //now we create the media player object
        val mediaplayer = MediaPlayer.create(this, R.raw.how_are_you)
        //making the seekbar to move with the song.
        val seekbar = binding.seekBar
        seekbar.progress = 0
        //Now to make the seekbar to end when the song ends
        seekbar.max = mediaplayer.duration

        val durationMinutes = (mediaplayer.duration / (60000))
        val durationSeconds = "${(mediaplayer.duration/1000)%60}"
        binding.duration.text = "$durationMinutes:${when(durationSeconds){
                "0" -> "00"
                "1" -> "01"
                "2" -> "02"
                "3" -> "03"
                "4" -> "04"
                "5" -> "05"
                "6" -> "06"
                "7" -> "07"
                "8" -> "08"
                "9" -> "09"
                else -> {durationSeconds}
            }
        }"

        binding.playBtn.setOnClickListener {
            //Its necessary to check if the image is playing or not
            if (!mediaplayer.isPlaying) {
                mediaplayer.start()
                //We need to change the image icon to pause.
                binding.playBtn.setImageResource(R.drawable.ic_pause)
            }else {
                //And also when the pause is pressed song should pause.
                mediaplayer.pause()
                //And image should be set back to the play button
                binding.playBtn.setImageResource(R.drawable.ic_play)
            }
        }

        //Trying to play next song
        binding.nextBtn.setOnClickListener {
            if (mediaplayer.isPlaying) {
                mediaplayer.seekTo(0)
            }
        }

        //Trying to play the previous song
        binding.previousBtn.setOnClickListener {
            if (mediaplayer.isPlaying) {
                mediaplayer.seekTo(0)
            }
        }

        //Here we set the seekbar object, so anywhere we click on the sick bar the song can continue from there
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, position: Int, changed: Boolean) {
                //Here if we change the seekbar, the song should change alongside
                if(changed){
                    mediaplayer.seekTo(position)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        //Now here we make the seekbar to move inline with the song and set the time.
        runnable = Runnable {
            seekbar.progress = mediaplayer.currentPosition
            val timeMinutes = mediaplayer.currentPosition / 60000
            val timeSeconds = "${(mediaplayer.currentPosition/1000)%60}"
            binding.time.text = "$timeMinutes:${
                when (timeSeconds) {
                    "0" -> "00"
                    "1" -> "01"
                    "2" -> "02"
                    "3" -> "03"
                    "4" -> "04"
                    "5" -> "05"
                    "6" -> "06"
                    "7" -> "07"
                    "8" -> "08"
                    "9" -> "09"
                    else -> {timeSeconds}
                }
            }"
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)


        //Now we would also set it so when song finish playing the seekbar returns to original position
        mediaplayer.setOnCompletionListener {
            binding.playBtn.setImageResource(R.drawable.ic_play)
            seekbar.progress = 0
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}