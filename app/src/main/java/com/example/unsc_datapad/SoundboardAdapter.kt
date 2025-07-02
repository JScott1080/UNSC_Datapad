package com.example.unsc_datapad.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unsc_datapad.R

data class SoundItem(
    val name: String,
    val filePath: String
)


class SoundboardAdapter(
    private val sounds: List<SoundItem>,
    private val onSoundPlay: (SoundItem) -> Unit
) : RecyclerView.Adapter<SoundboardAdapter.SoundViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sound_item, parent, false)
        return SoundViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        val sound = sounds[position]
        holder.bind(sound)
    }

    override fun getItemCount() = sounds.size

    inner class SoundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(sound: SoundItem) {
            itemView.findViewById<TextView>(R.id.soundTitle).text = sound.name // ✅ Display friendly name
            itemView.setOnClickListener {
                onSoundPlay(sound)
                val pulseAnimation = AnimationUtils.loadAnimation(itemView.context, R.anim.pulse_glow)
                itemView.startAnimation(pulseAnimation) // ✅ Apply animation to button, not full view

                Handler(Looper.getMainLooper()).postDelayed({
                    itemView.clearAnimation() // ✅ Ensure button animation clears correctly
                }, 1000)
            } // ✅ Play sound instantly on tap
        }
    }
}