import android.content.res.AssetManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.unsc_datapad.MainActivity
import com.example.unsc_datapad.R

class GrifballFragment : Fragment(R.layout.fragment_grifball) {

    private var mediaPlayer: MediaPlayer? = null
    private var backgroundMusic: MediaPlayer? = null
    private var currentTrackId: String? = null

    private lateinit var assetManager: AssetManager


    private val goal = mutableListOf(R.raw.medal1, R.raw.medal2, R.raw.medal3)
    private val hammer = mutableListOf(R.raw.hammer1, R.raw.hammer2, R.raw.hammer3)
    private val start = R.raw.countdown
    private val end = R.raw.victory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        assetManager = requireContext().assets

        /*val newMusicRes = R.raw.background_music1
        backgroundMusic = MediaPlayer.create(requireContext(), newMusicRes).apply {
            isLooping = true
            start()
        }
        currentTrackId = newMusicRes*/
        val afd = assetManager.openFd("ambiance/the_package.mp3")
        backgroundMusic?.apply {
            stop()
            release()
        }

        backgroundMusic = MediaPlayer().apply {
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            prepare()
            isLooping = true
            start()
        }
        currentTrackId = "ambiance/the_package.mp3"

        setupSoundButton(view, R.id.btn_goal, goal.random())
        setupSoundButton(view, R.id.btn_start, start)
        setupSoundButton(view, R.id.btn_end, end)
        setupSoundButton(view, R.id.btn_hammer, hammer.random())
        setupBackgroundbutton(view, R.id.btn_back1, "ambiance/the_package.mp3")
        setupBackgroundbutton(view, R.id.btn_back2, "ambiance/the_package_music.mp3")
    }

    private fun setupSoundButton(view: View, buttonId: Int, soundRes: Int) {
        val button = view.findViewById<Button>(buttonId)
        button.setOnClickListener {
            playSound(soundRes)
            val pulseAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pulse_glow)
            button.startAnimation(pulseAnimation)
            Handler(Looper.getMainLooper()).postDelayed({
                val fadeOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
                button.startAnimation(fadeOutAnimation)
            }, 1000)
        }
    }

    private fun setupBackgroundbutton(view: View, buttonId: Int, soundRes: String){
        val button = view.findViewById<Button>(buttonId)
        button.setOnClickListener {
            playBackground(soundRes)
            val pulseAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pulse_glow)
            button.startAnimation(pulseAnimation)
            Handler(Looper.getMainLooper()).postDelayed({
                val fadeOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
                button.startAnimation(fadeOutAnimation)
            }, 1000)
        }
    }


    private fun playSound(soundRes: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(requireContext(), soundRes).apply {
            start()
        }
    }

    private fun playBackground(soundRes: String) {
        if (currentTrackId == soundRes) return

        backgroundMusic?.apply {
            stop()
            release()
            backgroundMusic = null
        }

        val afd = assetManager.openFd(soundRes)
        backgroundMusic = MediaPlayer().apply {
            if(soundRes.contains("music"))
                setVolume(.65f, .65f)
            else
                setVolume(1.0f, 1.0f)
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            prepare()
            isLooping = true
            start()
        }

        currentTrackId = soundRes
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        backgroundMusic?.release()
        backgroundMusic = null
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).pauseGlobalAudio()
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as MainActivity).resumeGlobalAudio()
    }
}