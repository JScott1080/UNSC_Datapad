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


    // Utility function to safely resolve raw resources by name
    private fun getSafeRawResource(name: String): Int {
        val resId = resources.getIdentifier(name, "raw", "unsc_datapad")
        return if (resId != 0) resId else 0
    }

    // Refactored resource lists
    private val goal = mutableListOf(
        getSafeRawResource("medal1"),
        getSafeRawResource("medal2"),
        getSafeRawResource("medal3")
    )

    private val hammer = mutableListOf(
        getSafeRawResource("hammer1"),
        getSafeRawResource("hammer2"),
        getSafeRawResource("hammer3")
    )

    private val start = getSafeRawResource("countdown")
    private val end = getSafeRawResource("victory")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        assetManager = requireContext().assets

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

        setupRandomSoundButton(view, R.id.btn_goal, goal)
        setupSoundButton(view, R.id.btn_start, start)
        setupSoundButton(view, R.id.btn_end, end)
        setupRandomSoundButton(view, R.id.btn_hammer, hammer)
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

    private fun setupRandomSoundButton(view: View, buttonId: Int, soundRes: MutableList<Int>) {
        val button = view.findViewById<Button>(buttonId)
        button.setOnClickListener {
            playSound(soundRes.random())
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
        if(soundRes != 0) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(requireContext(), soundRes).apply {
                start()
            }
        }
    }

    private fun playBackground(soundRes: String) {
        if (currentTrackId == soundRes || soundRes == "") return

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