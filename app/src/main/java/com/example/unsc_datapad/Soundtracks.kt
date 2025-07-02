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

    /*
    private val soundtrackGroups = mapOf(
        "HaloCE"         to listOf("soundtracks/haloce/01_Opening_Suite.mp3", "soundtracks/haloce/02_Truth_and_Reconciliation_Suite.mp3", "soundtracks/haloce/03_brothers_in_arms.mp3", "soundtracks/haloce/04_enough_dead_heroes.mp3", "soundtracks/haloce/05_Perilous_Journey.mp3", "soundtracks/haloce/06_a_walk_in_the_woods.mp3", "soundtracks/haloce/07_ambiant_wonder.mp3", "soundtracks/haloce/08_a_gun_pointed_at_the_head_of_the_universe.mp3",
            "soundtracks/haloce/09_trace_amounts.mp3", "soundtracks/haloce/10_under_cover_of_night.mp3", "soundtracks/haloce/11_what_once_was_lost.mp3", "soundtracks/haloce/12_Lament_for_PVT_Jenkins.mp3", "soundtracks/haloce/13_Devils_Monsters.mp3", "soundtracks/haloce/14_covenant_dance.mp3", "soundtracks/haloce/15_alien_corridors.mp3", "soundtracks/haloce/16_rock_anthem_for_saving_the_wold.mp3", "soundtracks/haloce/17_the_maw.mp3", "soundtracks/haloce/18_drumrum.mp3",
            "soundtracks/haloce/19_on_a_pale_horse.mp3", "soundtracks/haloce/20_perchance_to_dream.mp3", "soundtracks/haloce/21_library_suit.mp3", "soundtracks/haloce/22_the_long_run.mp3", "soundtracks/haloce/23_suite_autumn.mp3", "soundtracks/haloce/24_shadows.mp3", "soundtracks/haloce/25_dust_and_echoed.mp3", "soundtracks/haloce/26_halo.mp3"),

        "HaloCEA"        to listOf("soundtracks/halocea/01_random_slipspace_trajectory.mp3", "soundtracks/halocea/03_Installation 04 .mp3", "soundtracks/halocea/04_An End of Dying.mp3", "soundtracks/halocea/05_Pale Rider.mp3", "soundtracks/halocea/06_Yawning Chasm.mp3", "soundtracks/halocea/07_A Private Service.mp3", "soundtracks/halocea/08_Rock in a Hard Place.mp3", "soundtracks/halocea/09_Flotsam, Jetsam.mp3", "soundtracks/halocea/10_Captain, My Captain.mp3",
            "soundtracks/halocea/11_Suite Fall.mp3", "soundtracks/halocea/12_Demons and Heretics.mp3", "soundtracks/halocea/13_Exfiltration .mp3", "soundtracks/halocea/14_Honest Negotiation Suite.mp3", "soundtracks/halocea/15_Unless You Mean To Shoot.mp3", "soundtracks/halocea/16_Infiltration.mp3", "soundtracks/halocea/17_Strung.mp3", "soundtracks/halocea/18_Bravery, Brotherhood.mp3", "soundtracks/halocea/19_Still, Moving.mp3", "soundtracks/halocea/20_Lions and Tigers and.mp3",
            "soundtracks/halocea/21_Between Beams.mp3", "soundtracks/halocea/22_Paranoid Illusion .mp3", "soundtracks/halocea/23_Xenoarchaeology.mp3", "soundtracks/halocea/24_Choreographite.mp3", "soundtracks/halocea/25_In the Substance of it.mp3", "soundtracks/halocea/26_How to Get Ahead in War.mp3", "soundtracks/halocea/27_Unreliable Exploration.mp3", "soundtracks/halocea/28_Dewy Decimate.mp3", "soundtracks/halocea/29_First Step.mp3", "soundtracks/halocea/30_Arborea Above.mp3",
            "soundtracks/halocea/31_Bad Dream.mp3", "soundtracks/halocea/32_Cloaked in Blackness.mp3", "soundtracks/halocea/33_Strident .mp3", "soundtracks/halocea/34_To Sleep.mp3", "soundtracks/halocea/35_Marathon Sprint.mp3", "soundtracks/halocea/36_Fragments.mp3", "soundtracks/halocea/37_Heretic Machine.mp3", "soundtracks/halocea/38_ Unfortunate Discovery.mp3", "soundtracks/halocea/39_Heliopause.mp3", "soundtracks/halocea/40_Didactic Principal.mp3"),

        "Halo2"          to listOf("soundtracks/halo2/01_halo_theme_mjolnir_mix.mp3", "soundtracks/halo2/03_peril.mp3", "soundtracks/halo2/04_ghosts_of_reach.mp3", "soundtracks/halo2/05_odyssey_1st_movement.mp3" ,"soundtracks/halo2/06_heretic_hero.mp3" ,"soundtracks/halo2/07_flawed_legacy.mp3" ,"soundtracks/halo2/08_impend.mp3", "soundtracks/halo2/09_never_surrender.mp3", "soundtracks/halo2/10_ancient_machine.mp3",
            "soundtracks/halo2/11_odyssey_2nd_movement.mp3", "soundtracks/halo2/12_in_amber_clad.mp3", "soundtracks/halo2/13_the_last_spartan.mp3", "soundtracks/halo2/14_orbit_of_glass.mp3", "soundtracks/halo2/15 _odyssey_3rd_movement.mp3", "soundtracks/halo2/16_heavy_price_paid.mp3", "soundtracks/halo2/17_earth_city.mp3", "soundtracks/halo2/18_high_charity.mp3", "soundtracks/halo2/19_odyssey_4th_movement.mp3",
            "soundtracks/halo2/20_remembrance.mp3", "soundtracks/halo2/21_connected.mp3", "soundtracks/halo2/22_prologue.mp3", "soundtracks/halo2/23_cairo_suit.mp3", "soundtracks/halo2/24_mombasa_suit.mp3", "soundtracks/halo2/25_unyielding.mp3", "soundtracks/halo2/26_mausoleum_suit.mp3", "soundtracks/halo2/27_unforgotten.mp3", "soundtracks/halo2/28_delta_halo_suit.mp3", "soundtracks/halo2/29_scared_icon_suit.mp3",
            "soundtracks/halo2/30_reclaimer.mp3", "soundtracks/halo2/31_high_charity_suite.mp3", "soundtracks/halo2/32_final.mp3", "soundtracks/halo2/33_epilogue.mp3"),

        "Halo2A"         to listOf("soundtracks/halo2a/01_halo_theme_gungnir_mix.mp3", "soundtracks/halo2a/02_skyline.mp3", "soundtracks/halo2a/03_not_a_number.mp3", "soundtracks/halo2a/04_kilindini_harbour.mp3", "soundtracks/halo2a/05_only_a_Star_only_the_sea.mp3", "soundtracks/halo2a/06_a_spartan_rises.mp3", "soundtracks/halo2a/07_unforgotten_memories.mp3", "soundtracks/halo2a/08_second_prelude.mp3",
            "soundtracks/halo2a/09_this_glittering_band.mp3", "soundtracks/halo2a/10_jeopardy.mp3", "soundtracks/halo2a/11_halo_theme_scorpion_mix.mp3", "soundtracks/halo2a/12_punishment.mp3", "soundtracks/halo2a/13_promise_the_girl.mp3", "soundtracks/halo2a/14_unsullied_memory.mp3", "soundtracks/halo2a/15_arise_in_valor.mp3", "soundtracks/halo2a/16_unwearied_heart.mp3", "soundtracks/halo2a/17_spartan's_regret.mp3",
            "soundtracks/halo2a/18_genesong.mp3", "soundtracks/halo2a/19_breaking_the_covenant.mp3", "soundtracks/halo2a/20_follow_in_flight.mp3", "soundtracks/halo2a/21_cryptic_whisper.mp3", "soundtracks/halo2a/22_impart.mp3", "soundtracks/halo2a/23_charity's_iron.mp3", "soundtracks/halo2a/24_moon_over_mambasa.mp3", "soundtracks/halo2a/25_trapped_in_amber.mp3", "soundtracks/halo2a/26_builder's_legacy.mp3",
            "soundtracks/halo2a/27_librarian's_gift.mp3", "soundtracks/halo2a/28_zealous_champion.mp3", "soundtracks/halo2a/29_steward_shepard_lonely_soul.mp3", "soundtracks/halo2a/30_africa_suit.mp3", "soundtracks/halo2a/31_prophet_suit.mp3", "soundtracks/halo2a/32_into_the_belly_of_the_beast.mp3", "soundtracks/halo2a/33_cracked_legend.mp3", "soundtracks/halo2a/34_menace_no_more.mp3", "soundtracks/halo2a/35_moon_over_mambasa_part_2.mp3"),

        "Halo3"          to listOf("soundtracks/halo3/01_luck.mp3", "soundtracks/halo3/02_released.mp3", "soundtracks/halo3/03_Infiltrate.mp3", "soundtracks/halo3/04_honorable_intentions.mp3", "soundtracks/halo3/05_last_of_the_brave.mp3", "soundtracks/halo3/06_brutes.mp3", "soundtracks/halo3/07_out_of_shadow.mp3", "soundtracks/halo3/08_to_kill_a_demon.mp3", "soundtracks/halo3/09_this_is_our_land.mp3", "soundtracks/halo3/10_this_is_the_hour.mp3",
            "soundtracks/halo3/11_dread_intrusion.mp3", "soundtracks/halo3/12_follow_our_brothers.mp3", "soundtracks/halo3/13_farthest_outpost.mp3", "soundtracks/halo3/14_behold_a_pale_horse.mp3", "soundtracks/halo3/15_edge_closer.mp3", "soundtracks/halo3/16_three_gates.mp3", "soundtracks/halo3/17_black_tower.mp3", "soundtracks/halo3/18_one_final_effort.mp3", "soundtracks/halo3/19_gravemind.mp3", "soundtracks/halo3/20_no_more_dead_heroes.mp3",
            "soundtracks/halo3/21_keep_what_your_steal.mp3", "soundtracks/halo3/22_halo_reborn.mp3", "soundtracks/halo3/23_great_journey.mp3", "soundtracks/halo3/24_tribute.mp3", "soundtracks/halo3/25_roll_call.mp3", "soundtracks/halo3/26_wake_me_when_you_need_me.mp3", "soundtracks/halo3/27_legend.mp3", "soundtracks/halo3/28_choose_wisely.mp3", "soundtracks/halo3/29_movement.mp3", "soundtracks/halo3/30_never_forget.mp3", "soundtracks/halo3/31_finish_the_fight.mp3"),

        "Halo4"          to listOf("soundtracks/halo4/01_awakening.mp3", "soundtracks/halo4/02_belly_of_the_beast.mp3", "soundtracks/halo4/03_requiem.mp3", "soundtracks/halo4/04_legacy.mp3", "soundtracks/halo4/05_faithless.mp3", "soundtracks/halo4/06_nemesis.mp3", "soundtracks/halo4/07_haven.mp3", "soundtracks/halo4/08_ascendancy.mp3", "soundtracks/halo4/09_solace.mp3", "soundtracks/halo4/10_to_galaxy.mp3", "soundtracks/halo4/11_immaterial.mp3",
            "soundtracks/halo4/12_117.mp3", "soundtracks/halo4/13_arrival.mp3", "soundtracks/halo4/14_revival.mp3", "soundtracks/halo4/15_green_and_blue.mp3", "soundtracks/halo4/16_desecration.mp3", "soundtracks/halo4/17_never_forget.mp3", "soundtracks/halo4/18_atonement.mp3", "soundtracks/halo4/19_gravity.mp3", "soundtracks/halo4/20_wreckage.mp3", "soundtracks/halo4/21_aliens.mp3", "soundtracks/halo4/22_kantele_bow.mp3", "soundtracks/halo4/23_pylons.mp3",
            "soundtracks/halo4/24_escape.mp3", "soundtracks/halo4/25_swamp.mp3", "soundtracks/halo4/26_push_through.mp3", "soundtracks/halo4/27_convoy.mp3", "soundtracks/halo4/28_to_galaxy_(Extended_edition).mp3", "soundtracks/halo4/29_lasky's_theme.mp3", "soundtracks/halo4/30_foreshadow.mp3", "soundtracks/halo4/31_cloud_city.mp3", "soundtracks/halo4/32_this_armor.mp3", "soundtracks/halo4/33_intruders.mp3", "soundtracks/halo4/34_mantis.mp3",
            "soundtracks/halo4/35_sacrifice.mp3", "soundtracks/halo4/36_never_forget_(Midnight_version).mp3", "soundtracks/halo4/37_majestic.mp3"),

        "Halo5"          to listOf("soundtracks/halo5/01_Halo_Canticles.mp3", "soundtracks/halo5/02_Light_Is_Green.mp3", "soundtracks/halo5/03_Kamchatka.mp3", "soundtracks/halo5/04_return_to_the_fold.mp3", "soundtracks/halo5/05_rock_and_ice.mp3", "soundtracks/halo5/06_argetn_moon.mp3", "soundtracks/halo5/07_scavengers.mp3", "soundtracks/halo5/08_In_absentia.mp3", "soundtracks/halo5/09_meridian_crossing.mp3", "soundtracks/halo5/10_unearthed.mp3",
            "soundtracks/halo5/11_unconfirmed_reports.mp3", "soundtracks/halo5/12_keeper_of_secrets.mp3", "soundtracks/halo5/13_cavalier.mp3", "soundtracks/halo5/14_crossed_paths.mp3", "soundtracks/halo5/15_untethered.mp3", "soundtracks/halo5/16_skeleton_crew.mp3", "soundtracks/halo5/17_siren_song.mp3", "soundtracks/halo5/18_enemy_of_my_enemy.mp3", "soundtracks/halo5/19_honor's_song.mp3", "soundtracks/halo5/20_Warrior_World.mp3", "soundtracks/halo5/21_covenant_prayer's.mp3",
            "soundtracks/halo5/22_cloud_chariot.mp3", "soundtracks/halo5/23_sentry_battle.mp3", "soundtracks/halo5/24_worldquake.mp3", "soundtracks/halo5/25_advent.mp3", "soundtracks/halo5/25_walk_softly.mp3", "soundtracks/halo5/26_genesis.mp3", "soundtracks/halo5/27_dominion.mp3", "soundtracks/halo5/28_the_trials.mp3", "soundtracks/halo5/29_sentential_song.mp3", "soundtracks/halo5/30_crypt.mp3", "soundtracks/halo5/31_end_game.mp3",
            "soundtracks/halo5/32_reunion.mp3", "soundtracks/halo5/33_blue_team.mp3", "soundtracks/halo5/34_jamason_locke.mp3", "soundtracks/halo5/35_osiris_suit,_act_1.mp3", "soundtracks/halo5/36_osiris_suit,_act_2.mp3", "soundtracks/halo5/37_osiris_suit,_act_3.mp3", "soundtracks/halo5/38_osiris_suit,_act_4.mp3"),

        "ODST"           to listOf("soundtracks/odst/00_we're_the_desperate_measures.mp3", "soundtracks/odst/01_overture.mp3", "soundtracks/odst/02_the_rookie.mp3", "soundtracks/odst/03_more_than_his_share.mp3", "soundtracks/odst/04_defference_for_darkness.mp3", "soundtracks/odst/05_the_menagerie.mp3", "soundtracks/odst/06_asphalt_and_ablution.mp3", "soundtracks/odst/07_traffic_jam.mp3", "soundtracks/odst/08_neon_night.mp3", "soundtracks/odst/09_the_office_of_naval_intelligence.mp3",
            "soundtracks/odst/10_bits_and_pieces.mp3", "soundtracks/odst/11_skyline.mp3", "soundtracks/odst/12_no_stone_unturned.mp3", "soundtracks/odst/13_one_way_ride.mp3", "soundtracks/odst/14_the_light_at_the_end.mp3", "soundtracks/odst/15_data_hive.mp3", "soundtracks/odst/16_special_delivery.mp3", "soundtracks/odst/17_final.mp3"),

        "Reach"          to listOf("soundtracks/reach/01_0verture.mp3", "soundtracks/reach/02_winter_contigancy.mp3", "soundtracks/reach/03_ONI_sword_base.mp3", "soundtracks/reach/04_nightfall.mp3", "soundtracks/reach/05_tip_of_the_spear.mp3", "soundtracks/reach/06_long_night_of_solice.mp3", "soundtracks/reach/07_exodus.mp3", "soundtracks/reach/08_new_alexandria.mp3", "soundtracks/reach/09_the_package.mp3", "soundtracks/reach/10_the_pillar_of_autumn.mp3",
            "soundtracks/reach/11_epilogue.mp3", "soundtracks/reach/12_from_the_vault.mp3","soundtracks/reach/13_ashes.mp3", "soundtracks/reach/14_fortress.mp3", "soundtracks/reach/15_we're_not_going_anywhere.mp3", "soundtracks/reach/16_at_any_cost.mp3", "soundtracks/reach/17_both_ways_remix.mp3", "soundtracks/reach/18_walk_away.mp3", "soundtracks/reach/19_ghosts_and_glass.mp3", "soundtracks/reach/20_we_remember.mp3"),

        "Infinite"       to listOf("soundtracks/infinite/01_zeta_halo.mp3", "soundtracks/infinite/02_sacrifice.mp3", "soundtracks/infinite/03_the_banished.mp3", "soundtracks/infinite/04_Gbraakon Escape.mp3", "soundtracks/infinite/05_Escharum.mp3", "soundtracks/infinite/06_follow_the_signal.mp3", "soundtracks/infinite/07_foundations.mp3", "soundtracks/infinite/08_the_weapon.mp3", "soundtracks/infinite/09_know_my_legend.mp3", "soundtracks/infinite/10_Reverie.mp3",
            "soundtracks/infinite/11_the_road.mp3", "soundtracks/infinite/12_echo_216.mp3", "soundtracks/infinite/13_ransom_keep.mp3", "soundtracks/infinite/14_tower.mp3", "soundtracks/infinite/15_Through the Trees.mp3", "soundtracks/infinite16_horn_of_abolition.mp3", "soundtracks/infinite/17_excavation_site.mp3", "soundtracks/infinite/18_conservitory.mp3", "soundtracks/infinite/19_endless.mp3", "soundtracks/infinite/20_spire.mp3", "soundtracks/infinite/21_Adjutant Resolution.mp3",
            "soundtracks/infinite/22_pelican_down.mp3", "soundtracks/infinite/23_heavy_artillery.mp3", "soundtracks/infinite/24_Scattered, Hunted, Defeated.mp3", "soundtracks/infinite/25_seeing_phantoms.mp3", "soundtracks/infinite/26_what_makes_us_human.mp3", "soundtracks/infinite/27_under_cover.mp3", "soundtracks/infinite28_riven_gate.mp3", "soundtracks/infinite/29_sequence.mp3", "soundtracks/infinite/30_nexus.mp3", "soundtracks/infinite/31_command_spire.mp3",
            "soundtracks/infinite/32_control_rom.mp3", "soundtracks/infinite/33_Three, Two, One.mp3", "soundtracks/infinite/34_Repository.mp3", "soundtracks/infinite/35_chapel.mp3", "soundtracks/infinite/36_Imprisoned.mp3", "soundtracks/infinite/37_Bridge Too Far.mp3", "soundtracks/infinite/38_we_do_it_together.mp3", "soundtracks/infinite/39_never_tell_me_the_odds.mp3", "soundtracks/infinite/40_House of Reckoning.mp3", "soundtracks/infinite/41_test_of_mettle.mp3",
            "soundtracks/infinite/42_There Will Be Consequences.mp3", "soundtracks/infinite/43_palace_arrival.mp3", "soundtracks/infinite/44_Silent Auditorium.mp3", "soundtracks/infinite/45_judgment.mp3", "soundtracks/infinite/46_A Message.mp3", "soundtracks/infinite/47_final.mp3", "soundtracks/infinite/48_hunters_dance.mp3"),

        "infinitemp" to listOf("soundtracks/infinitemp/01_what_is_a_spartan.mp3", "soundtracks/infinitemp/02_the_precipice_of_power.mp3", "soundtracks/infinitemp/03_forged_in_fire.mp3", "soundtracks/infinitemp/04_taking_back_whats_ours.mp3", "soundtracks/infinitemp/05_of_blood_and_burden.mp3", "soundtracks/infinitemp/06_no_greater_alliance.mp3", "soundtracks/infinitemp/07_Heroes._Legends._Immortal..mp3", "soundtracks/infinitemp/08_Though_This_be_Madness.mp3",
            "soundtracks/infinitemp/09_Augmented_Realities.mp3", "soundtracks/infinitemp/10_Between_Collapse_and_Control.mp3", "soundtracks/infinitemp/11_echoes_of_orion.mp3", "soundtracks/infinitemp/12_Legacy_of_Generations.mp3", "soundtracks/infinitemp/13_Never Truly Alone.mp3", "soundtracks/infinitemp/14_Pack Mentality.mp3", "soundtracks/infinitemp/15_See you at the Bottom (Infection Theme).mp3", "soundtracks/infinitemp/16_Dinh's Descent.mp3",
            "soundtracks/infinitemp/17_Derivation.mp3", "soundtracks/infinitemp/18_Firewall.mp3", "soundtracks/infinitemp/19_Death & Triumph.mp3", "soundtracks/infinitemp/20_Artificial Mind.mp3", "soundtracks/infinitemp/21_Academia.mp3", "soundtracks/infinitemp/22_Intrusion.mp3")
    )
    */

    private lateinit var albumCarousel: RecyclerView

    private val soundtrackGroups = mutableMapOf<String, List<String>>()

    private val albums = mutableListOf<SoundtrackAlbum>()

    var playAllEnabled = true

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

    fun formatGameName(group: String): String {
        return when (group.lowercase()) {
            "haloce" -> "Halo: Combat Evolved"
            "halocea" -> "Halo: Combat Evolved Anniversary"
            "halo2" -> "Halo 2"
            "halo2a" -> "Halo 2 Anniversary"
            "halo3" -> "Halo 3"
            "halo4" -> "Halo 4"
            "halo5" -> "Halo 5: Guardians"
            "odst" -> "Halo 3: ODST"
            "reach" -> "Halo: Reach"
            "infinite" -> "Halo Infinite"
            "infinitemp" -> "Halo Infinite: Multiplayer"
            else -> group.replaceFirstChar { it.uppercase() }
        }
    }

    fun getCoverResourceFor(group: String): Int {
        return when (group.lowercase()) {
            "haloce" -> R.drawable.halo_ce_cover
            "halocea" -> R.drawable.halo_cea_cover
            "halo2" -> R.drawable.halo2_cover
            "halo2a" -> R.drawable.halo2a_cover
            "halo3" -> R.drawable.halo3_cover
            "halo4" -> R.drawable.halo4_cover
            "halo5" -> R.drawable.halo5_cover
            "odst" -> R.drawable.halo_odst_cover
            "reach" -> R.drawable.halo_reach_cover
            "infinite" -> R.drawable.halo_infinite_cover
            "infinitemp" -> R.drawable.halo_infinite_mp_cover
            else -> R.drawable.halo3_cover
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
        (activity as? MainActivity)?.startSoundtrackPlayback(trackList) // ✅ Send correct tracks
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
