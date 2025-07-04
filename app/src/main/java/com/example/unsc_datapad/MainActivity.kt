package com.example.unsc_datapad

import GrifballFragment
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.unsc_datapad.adapters.SoundItem
import com.example.unsc_datapad.fragments.BoothBoardFragment
import com.example.unsc_datapad.fragments.RvB_BoradFragment
import java.io.File
import java.io.IOException
import java.util.LinkedList
import java.util.Queue

private const val REQUEST_CODE = 1170 // ✅ Assign a unique value

class MainActivity : AppCompatActivity() {

    private var ambiancePlayer: MediaPlayer? = null
    private var currentAmbiance: String? = null

    private var soundtrackPlayer: MediaPlayer? = null
    private var soundtrackQueue = mutableListOf<String>()
    private var isShuffleEnabled = false
    private var currentTrackIndex = 0
    private var currentPlayingSong: String? = null

    private var soundboardClip: MediaPlayer? = null


    private lateinit var assetManager: AssetManager

    var isPaused = false

    @RequiresApi(Build.VERSION_CODES.R)

    fun requestStoragePermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "Permission granted!")
        } else {
            Log.e("MainActivity", "Permission denied.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }


        // 🔹 Ensure full-screen mode persists when Spinner opens
        window.insetsController?.apply {
            hide(WindowInsets.Type.navigationBars() or WindowInsets.Type.statusBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        assetManager = assets
        // Initialize buttons
        setupButtons()
        setupGlobalFragmentSwitcher()
        setupPlaybackButtons()

    }

    private fun setupButtons() {
        val btnGrifball = findViewById<Button>(R.id.btn_grifball)
        val btnBooth = findViewById<Button>(R.id.btn_booth)
        val btnRvB = findViewById<Button>(R.id.btn_rvb)
        val btnSound = findViewById<Button>(R.id.btn_soundtrack)
        val btnAmbi = findViewById<Button>(R.id.btn_ambiance)

        btnGrifball.setOnClickListener {
            loadFragment(GrifballFragment())
        }

        btnBooth.setOnClickListener() {
            loadFragment(BoothBoardFragment())
        }

        btnRvB.setOnClickListener() {
            loadFragment(RvB_BoradFragment())
        }

        btnSound.setOnClickListener() {
            loadFragment(SoundtracksFragment())
        }

        btnAmbi.setOnClickListener() {
            loadFragment(AmbianceFragment())
        }

    }


    private fun setupGlobalFragmentSwitcher() {
        val fragmentSwitcher = findViewById<Spinner>(R.id.global_fragment_switcher)

        val options = arrayOf("Select Board", "Grifball", "Booth", "RvB", "Soundtracks", "Ambiance")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        fragmentSwitcher.adapter = adapter

        fragmentSwitcher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) return // Prevents auto-loading on startup

                when (options[position]) {
                    "Grifball" -> loadFragment(GrifballFragment())
                    "Booth" -> loadFragment(BoothBoardFragment())
                    "RvB" -> loadFragment(RvB_BoradFragment())
                    "Soundtracks" -> loadFragment(SoundtracksFragment())
                    "Ambiance" -> loadFragment(AmbianceFragment())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun setupPlaybackButtons() {
        val pauseButton = findViewById<Button>(R.id.btn_pause)

        pauseButton.setOnClickListener {
            if (isPaused) {
                resumeGlobalAudio()
                pauseButton.text = "⏸ Pause"
            } else {
                pauseGlobalAudio()
                pauseButton.text = "▶ Play"
            }
            isPaused = !isPaused
        }

        val btnNext = findViewById<Button>(R.id.btn_next_track)
        btnNext.setOnClickListener() {
            if(soundtrackPlayer != null) {
                onTrackFinished()
                val pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_glow)
                btnNext.startAnimation(pulseAnimation)
                Handler(Looper.getMainLooper()).postDelayed({
                    btnNext.clearAnimation()
                }, 1000)
            }
        }

        val btnpre = findViewById<Button>(R.id.btn_prev_track)
        btnpre.setOnClickListener() {
            if(soundtrackPlayer != null) {
                onTrackFinished()
                val pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_glow)
                btnNext.startAnimation(pulseAnimation)
                Handler(Looper.getMainLooper()).postDelayed({
                    btnNext.clearAnimation()
                }, 1000)
            }
        }

        val switchShuffle = findViewById<Switch>(R.id.switch_shuffle_mode)
        switchShuffle.isChecked = true // ✅ Default to shuffle mode

        isShuffleEnabled = switchShuffle.isChecked // ✅ Initialize correctly

        switchShuffle.setOnCheckedChangeListener { _, isChecked -> // ✅ Ensure `isChecked` is accessible
            isShuffleEnabled = isChecked
            Log.d("MusicPlayer", "Shuffle toggled: ${if (isChecked) "ON" else "OFF"}") // ✅ Debugging log
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        ambiancePlayer?.release()
        soundtrackPlayer?.release()
    }

    private val soundQueue: Queue<String> = LinkedList() // ✅ Queue for follow-up sounds

    fun playSoundbyte(fileName: String) {
        if (soundboardClip?.isPlaying == true) {
            soundQueue.add(fileName) // ✅ Add to queue instead of interrupting
            return
        }

        // ✅ Lower background music volume before playing soundbyte
        soundtrackPlayer?.setVolume(0.2f, 0.2f) // ✅ Adjust both left & right channels

        try {
            val afd = assetManager.openFd(fileName)
            soundboardClip = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
                start()

                setOnCompletionListener {
                    soundboardClip?.release()
                    soundboardClip = null

                    // ✅ Restore music volume after soundbyte finishes
                    soundtrackPlayer?.setVolume(.35f, .35f) // ✅ Reset to normal level

                    playNextSound()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace() // Log missing asset or playback error
            soundtrackPlayer?.setVolume(0.35f, 0.35f)
            playNextSound() // Continue queue even if current sound fails
        }

    }

    fun playNextSound() {
        val nextSound = soundQueue.poll() // ✅ Grab the next sound from queue
        if (nextSound != null) {
            playSoundbyte(nextSound) // ✅ Play it once the current sound finishes
        }
    }



    fun playAmbiance(soundRes: String) {
        if (currentAmbiance == soundRes) return

        ambiancePlayer?.apply {
            stop()
            release()
            ambiancePlayer = null
        }
        try {
            val afd = assetManager.openFd(soundRes)
            ambiancePlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
                isLooping = true
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            currentAmbiance = null // Reset if loading fails
        }


        currentAmbiance = soundRes
        updateCurrentlyPlayingUI() // ✅ Refresh UI when ambiance starts
    }

    fun fadeOutAmbiance(onFadeComplete: () -> Unit) {
        ambiancePlayer?.let { player ->
            val handler = Handler(Looper.getMainLooper())
            var volume = 1.0f // Start at full volume

            handler.post(object : Runnable {
                override fun run() {
                    if (volume > 0.0f) {
                        volume -= 0.1f
                        player.setVolume(volume, volume)
                        handler.postDelayed(this, 100) // Adjust volume every 100ms
                    } else {
                        player.stop()
                        player.release()
                        ambiancePlayer = null
                        onFadeComplete() // Calls the next function after fading out
                    }
                }
            })
        } ?: onFadeComplete()
    }

    fun startSoundtrackPlayback(trackList: List<String>) {
        if (trackList.isEmpty()) return

        soundtrackQueue.clear()
        soundtrackQueue.addAll(trackList) // ✅ Directly update the queue

        currentTrackIndex = 0 // ✅ Start from the first track
        playNextSoundtrack()
    }

    private fun playNextSoundtrack() {
        if (soundtrackQueue.isEmpty()) return

        val trackRes = soundtrackQueue[currentTrackIndex]

        try {
            val afd = assetManager.openFd(trackRes)

            soundtrackPlayer?.apply {
                stop()    // ✅ Ensure playback stops cleanly
                reset()   // ✅ Reset before release to prevent unexpected behavior
                release() // ✅ Properly release resources
            }

            soundtrackPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                setVolume(.35f, .35f)
                prepare()
                setOnCompletionListener { onTrackFinished() }
                start()
            }
            currentPlayingSong = trackRes
            updateCurrentlyPlayingUI()
        } catch (e: Exception) {
            e.printStackTrace()
            onTrackFinished() // Skip to next track if this one fails
        }
    }

    private var shuffleQueue = mutableListOf<String>() // ✅ Stores shuffled tracks
    private var playedTracks = mutableSetOf<String>() // ✅ Tracks played songs

    private fun initializeShuffleQueue() {
        shuffleQueue = soundtrackQueue.shuffled().toMutableList() // ✅ Shuffle full list once
        playedTracks.clear() // ✅ Reset tracking when new shuffle starts
    }

    private fun onTrackFinished() {
        if (isShuffleEnabled) {
            if (shuffleQueue.isEmpty()) {
                initializeShuffleQueue() // ✅ Only reshuffle **after all songs have been played**
            }

            val nextTrack = shuffleQueue.removeAt(0) // ✅ Get next track
            playedTracks.add(nextTrack) // ✅ Mark as played
            currentTrackIndex = soundtrackQueue.indexOf(nextTrack)
        } else {
            currentTrackIndex = (currentTrackIndex + 1) % soundtrackQueue.size // ✅ Standard looping
        }

        playNextSoundtrack() // ✅ Start next track
    }


    private fun updateCurrentlyPlayingUI() {
        runOnUiThread {
            val nowPlayingSoundtrack = findViewById<TextView>(R.id.tv_active_soundtrack)
            nowPlayingSoundtrack.text = currentPlayingSong?.let { formatSongTitle(it) } ?: "Soundtrack: None"
            nowPlayingSoundtrack.isSelected = true

            val nowPlayingAmbiance = findViewById<TextView>(R.id.tv_active_ambiance)
            nowPlayingAmbiance.text = currentAmbiance?.let { formatAmbianceTitle(it) } ?: "Now Playing: None"
            nowPlayingAmbiance.isSelected = true
        }
    }

    fun formatSongTitle(filename: String): String {
        return filename
            .substringAfter("_") // ✅ Removes forced order prefix (`##_`)
            .replace("_", " ") // ✅ Converts underscores to spaces
            .replace(".mp3", "") // ✅ Removes file extension
            .trim()
            .split(" ") // ✅ Capitalizes each word in the title
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }
    }

    fun formatAmbianceTitle(filename: String): String {
        return filename
            .substringAfter("/")
            .replace("_", " ") // ✅ Converts underscores to spaces
            .replace(".mp3", "") // ✅ Removes file extension
            .trim()
            .split(" ") // ✅ Capitalizes each word in the title
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }
    }

    fun pauseGlobalAudio() {
        if(ambiancePlayer != null)
            ambiancePlayer?.pause()
        if(soundtrackPlayer != null)
            soundtrackPlayer?.pause()
        isPaused = true
    }

    fun resumeGlobalAudio() {
        if(ambiancePlayer != null)
            ambiancePlayer?.start()
        if(soundtrackPlayer != null)
            soundtrackPlayer?.start()
        isPaused = false
    }

    private fun loadFragment(fragment: Fragment) {
        findViewById<View>(R.id.main_menu_container)?.visibility = View.GONE // Hide menu

        val playBar = findViewById<View>(R.id.playback_display)

        // Hide play bar when entering Grifball
        playBar.visibility = if (fragment is GrifballFragment) View.GONE else View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.insetsController?.apply {
                hide(WindowInsets.Type.navigationBars() or WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}