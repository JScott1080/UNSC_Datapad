package com.example.unsc_datapad.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unsc_datapad.R

data class SoundItem(
    val name: String,
    val filePath: String,
    val isAsset: Boolean // ✅ Indicates if the file is from `assets/`
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
            itemView.setOnClickListener { onSoundPlay(sound) } // ✅ Play sound instantly on tap
        }
    }
}