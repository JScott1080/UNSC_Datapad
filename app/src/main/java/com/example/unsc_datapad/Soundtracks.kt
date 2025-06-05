package com.example.unsc_datapad

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView


class SoundtracksFragment : Fragment(R.layout.fragment_soundtracks) {


    private val soundtrackGroups = mapOf(
        "HaloCE"         to listOf("soundtracks/halocea/01_random_slipspace_trajectory.mp3"),
        "HaloCEA"        to listOf("soundtracks/halocea/01_random_slipspace_trajectory.mp3"),
        "Halo2"          to listOf("soundtracks/halo2/01_halo_theme_mjolnir_mix.mp3"),
        "Halo2A"         to listOf("soundtracks/halo2a/01_halo_theme_gungnir_mix.mp3"),
        "Halo3"          to listOf("soundtracks/halo3/01_luck.mp3"),
        "Halo4"          to listOf("soundtracks/halo4/01_awakening.mp3"),
        "Halo5"          to listOf("soundtracks/halo4/01_awakening.mp3"),
        "ODST"           to listOf("soundtracks/odst/00_we're_the_desperate_measures.mp3", "soundtracks/odst/01_the_rookie.mp3"),
        "Reach"          to listOf("soundtracks/reach/01_0verture.mp3"),
        "Infinite"       to listOf("soundtracks/infinite/01_zeta_halo.mp3"),
        "infinitemp" to listOf("soundtracks/infinitemp/01_what_is_a_spartan.mp3")
    )

    private lateinit var albumCarousel: RecyclerView

    private val albums = listOf(
        SoundtrackAlbum("Halo: Combat Evolved", R.drawable.halo_ce_cover, "HaloCE"),
        SoundtrackAlbum("Halo: Combat Evolved Anniversary", R.drawable.halo_cea_cover, "HaloCEA"),
        SoundtrackAlbum("Halo 2", R.drawable.halo2_cover, "Halo2"),
        SoundtrackAlbum("Halo 2 Anniversary", R.drawable.halo2a_cover, "Halo2A"),
        SoundtrackAlbum("Halo 3", R.drawable.halo3_cover, "Halo3"),
        SoundtrackAlbum("Halo 4", R.drawable.halo4_cover, "Halo4"),
        SoundtrackAlbum("Halo 5: Guardians", R.drawable.halo5_cover, "Halo5"),
        SoundtrackAlbum("Halo 3: ODST", R.drawable.halo_odst_cover, "ODST"),
        SoundtrackAlbum("Halo: Reach", R.drawable.halo_reach_cover, "Reach"),
        SoundtrackAlbum("Halo Infinite", R.drawable.halo_infinite_cover, "Infinite"),
        SoundtrackAlbum("Halo Infinite: Multiplayer", R.drawable.halo_infinite_mp_cover, "infinitemp")
    )

    var playAllEnabled = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    fun getTracksForSelection(selectedAlbum: String?): List<String> {
        return selectedAlbum?.let { soundtrackGroups[it] ?: emptyList() } ?: soundtrackGroups.values.flatten()
    }

    fun loadAlbumSongs(album: SoundtrackAlbum) {
        val songList = soundtrackGroups[album.groupName]
        if (songList.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No songs available for ${album.name}", Toast.LENGTH_SHORT).show()
            return
        }
    }

    fun requestPlaySoundtrack(trackList: List<String>) {
        (activity as? MainActivity)?.startSoundtrackPlayback(trackList)
    }

    fun isPlayAllEnabled(): Boolean {
        return playAllEnabled
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
                                selectedAlbumTracks + allOtherTracks // ✅ Start with chosen soundtrack, then add others
                            } else {
                                listOf(song) + selectedAlbumTracks.filter { it != song } // ✅ Play album-specific tracks only
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
