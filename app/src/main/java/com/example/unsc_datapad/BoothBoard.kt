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
        //startRandomBoothPlayback() // ✅ Schedule random sound playback

    }

    private fun loadBoothSounds() {
        boothSounds.clear() // ✅ Clears old list before loading new items

        // ✅ Dynamically load sounds (this can be extended to a database or JSON file)
        boothSounds.add(SoundItem("IWHBD", "booth/iwhbyd.wav"))
        boothSounds.add(SoundItem("Troopers!", "booth/troopers.wav"))
        boothSounds.add(SoundItem("Osiris Briefing", "booth/h5_open.mp3"))
        boothSounds.add(SoundItem("Bad Situation", "booth/badsituation.wav"))
        boothSounds.add(SoundItem("Flip Yap", "booth/flipyap.wav"))
        boothSounds.add(SoundItem("Big Green Style", "booth/greenstyle.wav"))
        boothSounds.add(SoundItem("I am Cupid", "booth/godislove.wav"))
        boothSounds.add(SoundItem("You had your chance", "booth/youhadyourchance.wav"))
        boothSounds.add(SoundItem("Mysterious Ways", "booth/c03_2095_jon[c03_2095_jon].wav"))
        boothSounds.add(SoundItem("Buck up boy!", "booth/c03_2094_jon[c03_2094_jon].wav"))
        boothSounds.add(SoundItem("When I joined", "booth/c03_2093_jon[c03_2093_jon].wav"))
        boothSounds.add(SoundItem("Smell of Green", "booth/010la2_061_jon[010la2_061_jon].wav"))
        boothSounds.add(SoundItem("Pucker up", "booth/c100_361_buc[c100_361_buc].wav"))
        boothSounds.add(SoundItem("Dropping feet first", "booth/c100_362_buc[c100_362_buc].wav"))
        boothSounds.add(SoundItem("Wort Wort Wort", "booth/sightedenemyrecentcombat_1.wav"))
        boothSounds.add(SoundItem("Rah OhDaBa", "booth/sightedenemyrecentcombat_2.wav"))
        boothSounds.add(SoundItem("I need a weapon", "booth/master-chief-i-need-a-weapon.mp3"))
        boothSounds.add(SoundItem("Birthday", "booth/gbp.mp3"))
        boothSounds.add(SoundItem("Boo", "booth/c07_1060_mas[c07_1060_mas].wav"))
        boothSounds.add(SoundItem("Cave", "booth/a30_460_cortana_A30_460_Cortana.wav"))



        boothRecyclerView.adapter?.notifyDataSetChanged() // ✅ Refresh UI with new sounds
    }

    private fun playBoothSound(sound: SoundItem) {
        (activity as? MainActivity)?.playSoundbyte(sound.filePath)
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


}