package com.example.unsc_datapad.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unsc_datapad.MainActivity
import com.example.unsc_datapad.adapters.SoundboardAdapter
import com.example.unsc_datapad.R
import com.example.unsc_datapad.adapters.SoundItem

class BoothBoardFragment : Fragment(R.layout.fragment_booth_board) {

    private lateinit var boothRecyclerView: RecyclerView
    private val handler = Handler(Looper.getMainLooper())
    private val boothSounds = mutableListOf<SoundItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booth_board, container, false) // ✅ Corrected layout reference
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        boothRecyclerView = view.findViewById(R.id.boothRecyclerView)
        boothRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4) // ✅ Corrected spelling
        boothRecyclerView.adapter = SoundboardAdapter(boothSounds) { sound -> playBoothSound(sound) }

        loadBoothSounds() // ✅ Load sound assets
        startRandomBoothPlayback() // ✅ Schedule random sound playback

        view.findViewById<Button>(R.id.addBoothSoundButton).setOnClickListener {
            (activity as? MainActivity)?.pickSoundFile?.launch(arrayOf("audio/*")) // ✅ Opens sound picker
        }

    }

    private fun loadBoothSounds() {
        boothSounds.clear() // ✅ Clears old list before loading new items

        // ✅ Dynamically load sounds (this can be extended to a database or JSON file)
        boothSounds.add(SoundItem("IWHBD", "booth/iwhbyd.wav", true))
        boothSounds.add(SoundItem("Troopers!", "booth/troopers.wav", true))
        boothSounds.add(SoundItem("Osiris Briefing", "booth/h5_open.mp3", true))

        boothRecyclerView.adapter?.notifyDataSetChanged() // ✅ Refresh UI with new sounds
    }

    private fun playBoothSound(sound: SoundItem) {
        (activity as? MainActivity)?.playSoundbyte(sound)
    }

    private val randomSoundRunnable = object : Runnable {
        override fun run() {
            if (!((activity as? MainActivity)?.isPaused ?: false) && boothSounds.isNotEmpty()) { // ✅ Correct check
                val randomSound = boothSounds.random()
                playBoothSound(randomSound)
            }
            handler.postDelayed(this, (120_000..300_000).random().toLong()) // ✅ Random delay (2-5 minutes)
        }
    }

    private fun startRandomBoothPlayback() {
        handler.postDelayed(randomSoundRunnable, (120_000..300_000).random().toLong()) // ✅ Initial delay
    }

    fun addBoothSound(fileName: String, filePath: String) {
        boothSounds.add(SoundItem(fileName, filePath, false)) // ✅ Store the new sound
        boothRecyclerView.adapter?.notifyDataSetChanged() // ✅ Refresh UI to show new sound
    }


}