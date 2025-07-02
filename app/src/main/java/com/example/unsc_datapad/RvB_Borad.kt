package com.example.unsc_datapad.fragments

import android.os.Bundle
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

class RvB_BoradFragment : Fragment(R.layout.fragment_rv_b__borad) {

    private lateinit var rvbRecyclerView: RecyclerView
    private val rvbSounds = mutableListOf<SoundItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rv_b__borad, container, false) // ✅ Corrected layout reference
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvbRecyclerView = view.findViewById(R.id.rvbRecyclerView)
        rvbRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4) // ✅ Corrected spelling
        rvbRecyclerView.adapter = SoundboardAdapter(rvbSounds) { sound -> playrvbSound(sound) }

        loadRvBSounds() // ✅ Load sound assets
    }

    private fun loadRvBSounds() {
        rvbSounds.clear() // ✅ Clears old list before loading new items


        rvbRecyclerView.adapter?.notifyDataSetChanged() // ✅ Refresh UI with new sounds
    }

    private fun playrvbSound(sound: SoundItem) {
        (activity as? MainActivity)?.playSoundbyte(sound.filePath)
    }

}