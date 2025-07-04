package com.example.unsc_datapad

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.JsonReader
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import java.io.InputStreamReader


class SoundtracksFragment : Fragment(R.layout.fragment_soundtracks) {


    private lateinit var albumCarousel: RecyclerView

    private val soundtrackGroups = mutableMapOf<String, List<String>>()

    private val albums = mutableListOf<SoundtrackAlbum>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val assetManager = requireContext().assets
        val basePath = "soundtracks"

        val albumEntries = mutableListOf<Triple<Int, SoundtrackAlbum, List<String>>>()

        for (folder in assetManager.list(basePath) ?: emptyArray()) {
            val folderPath = "$basePath/$folder"
            val songs = assetManager.list(folderPath)
                ?.filter { it.endsWith(".mp3") }
                ?.map { "$folderPath/$it" }
                ?.sorted() ?: continue

            try {
                val jsonStream = assetManager.open("$folderPath/metadata.json")
                val reader = JsonReader(InputStreamReader(jsonStream))
                var name = folder
                var coverName = "default_album_cover"
                var order = Int.MAX_VALUE

                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "displayName" -> name = reader.nextString()
                        "coverName" -> coverName = reader.nextString()
                        "order" -> order = reader.nextInt()
                        else -> reader.skipValue()
                    }
                }
                reader.endObject()
                reader.close()

                val resId = resources.getIdentifier(coverName, "drawable", requireContext().packageName)
                soundtrackGroups[folder] = songs
                albumEntries.add(Triple(order, SoundtrackAlbum(name, resId, folder), songs))

            } catch (e: Exception) {
                Log.e("SoundtrackLoader", "Missing or malformed metadata.json in $folderPath: $e")
            }
        }

        albums.clear()
        albumEntries.sortBy { it.first }
        albums.addAll(albumEntries.map { it.second })

        albumCarousel = view.findViewById(R.id.carousel_viewpager)
        albumCarousel.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val formatter: (String) -> String = { filename ->
            (activity as? MainActivity)?.formatSongTitle(filename) ?: filename
        }

        loadAlbumSongs(albums.first()) // ✅ Auto-load songs from the first album

        LinearSnapHelper().attachToRecyclerView(albumCarousel) // ✅ Enables snapping behavior

        val switchPlayAll = view.findViewById<Switch>(R.id.switch_play_all)
        switchPlayAll.isChecked = true
        val currentToggleState = switchPlayAll.isChecked // ✅ Get initial state

        albumCarousel.adapter = MusicCarouselAdapter(
            albums,
            soundtrackGroups,
            formatter,
            currentToggleState, // ✅ Pass correct state at start
            { album -> loadAlbumSongs(album) },
            { selectedSongs -> requestPlaySoundtrack(selectedSongs)}
        )

        switchPlayAll.setOnCheckedChangeListener { _, isChecked ->
            Log.d("MusicPlayer", "Toggle switched: ${if (isChecked) "Play All" else "Soundtrack Only"}")

            albumCarousel.adapter = MusicCarouselAdapter(
                albums,
                soundtrackGroups,
                formatter,
                isChecked, // ✅ Pass updated toggle state dynamically
                { album -> loadAlbumSongs(album) },
                { selectedSongs -> requestPlaySoundtrack(selectedSongs)}
            )
        }


    }

    fun loadAlbumSongs(album: SoundtrackAlbum) {
        val songList = soundtrackGroups[album.groupName]
        if (songList.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No songs available for ${album.name}", Toast.LENGTH_SHORT).show()
            return
        }
    }

    fun requestPlaySoundtrack(trackList: List<String>) {
        (activity as? MainActivity)?.startSoundtrackPlayback(trackList) // ✅ Send correct tracks
    }


    data class SoundtrackAlbum(
        val name: String,
        val coverResId: Int,  // Album cover image
        val groupName: String // Matches a key in soundtrackGroups (populating soon)
    )

    class MusicCarouselAdapter(
        private val albums: List<SoundtrackAlbum>,
        private val soundtrackGroups: Map<String, List<String>>,
        private val formatTitle: (String) -> String,
        private val isPlayAllEnabled: Boolean, // ✅ Toggle state now stored in the adapter
        private val onAlbumSelected: (SoundtrackAlbum) -> Unit,
        private val onSongSelected: (List<String>) -> Unit
    ) : RecyclerView.Adapter<MusicCarouselAdapter.AlbumViewHolder>() {

        inner class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val albumCover: ImageView = view.findViewById(R.id.album_cover)
            val songContainer: LinearLayout = view.findViewById(R.id.song_button_container)

            fun bind(album: SoundtrackAlbum) {
                albumCover.setImageResource(album.coverResId)
                songContainer.removeAllViews()

                val songList = soundtrackGroups[album.groupName] ?: listOf()
                songList.forEach { song ->
                    val formattedTitle = formatTitle(song)
                    val songButton = Button(itemView.context).apply {
                        text = formattedTitle
                        layoutParams = LinearLayout.LayoutParams(350, 40)
                        setOnClickListener {
                            val selectedAlbumTracks = soundtrackGroups[album.groupName] ?: emptyList() // ✅ Tracks from chosen soundtrack
                            val allOtherTracks = soundtrackGroups.values.flatten().filterNot { selectedAlbumTracks.contains(it) } // ✅ Everything else

                            val trackList = if (isPlayAllEnabled) {
                                listOf(song) + (selectedAlbumTracks - song) + allOtherTracks // ✅ Ensures selected song plays first
                            } else {
                                listOf(song) + selectedAlbumTracks.filter { it != song } // ✅ Album-specific playback
                            }

                            val uniqueTrackList = LinkedHashSet(trackList).toList() // ✅ No extra processing needed

                            onSongSelected(uniqueTrackList) // ✅ Pass only the full track list
                            Log.d("MusicPlayer", "Final track list: ${uniqueTrackList.joinToString(", ")}") // ✅ Debugging

                            val pulseAnimation = AnimationUtils.loadAnimation(itemView.context, R.anim.pulse_glow)
                            startAnimation(pulseAnimation) // ✅ Apply animation to button, not full view

                            Handler(Looper.getMainLooper()).postDelayed({
                                clearAnimation() // ✅ Ensure button animation clears correctly
                            }, 1000)
                        }
                    }
                    songContainer.addView(songButton)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.album_view, parent, false) // ✅ Uses album_view.xml
            return AlbumViewHolder(view)
        }

        override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
            holder.bind(albums[position])
        }

        override fun getItemCount(): Int = albums.size
    }

    class SongAdapter(
        private val songs: List<String>,
        private val formatTitle: (String) -> String, // ✅ Pass formatting function
        private val onSongSelected: (String) -> Unit
    ) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

        inner class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val songTitle: TextView = view.findViewById(android.R.id.text1)

            fun bind(song: String) {
                songTitle.text = formatTitle(song) // ✅ Uses cleaned-up title
                itemView.setOnClickListener { onSongSelected(song) }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return SongViewHolder(view)
        }

        override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
            holder.bind(songs[position])
        }

        override fun getItemCount(): Int = songs.size

    }
}
