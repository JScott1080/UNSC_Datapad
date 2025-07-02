package com.example.unsc_datapad

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button

class AmbianceFragment : Fragment(R.layout.fragment_ambiance) {

    private val ambianceOptions = listOf(R.id.btn_package to "ambiance/the_package.mp3", R.id.btn_osiris to "ambiance/osiris.mp3", R.id.btn_spear to "ambiance/tip_of_the_spear.mp3", R.id.btn_nightfall to "ambiance/nightfall.mp3", R.id.btn_exodus to "ambiance/exodus.mp3",
        R.id.btn_dawn to "ambiance/forward_unto_dawn.mp3", R.id.btn_midnight to "ambiance/infinity_hangar.mp3", R.id.btn_autumn to "ambiance/pillar_of_autumn.mp3", R.id.btn_cairo to "ambiance/cairo_station.mp3", R.id.btn_armory to "ambiance/the_armory.mp3",
        R.id.btn_requium to "ambiance/requium.mp3", R.id.btn_halo to "ambiance/two_betrayals.mp3", R.id.btn_forerunner to "ambiance/forerunner.mp3", R.id.btn_flood to "ambiance/the_orical.mp3", R.id.btn_corvette to "ambiance/covenant_ship.mp3")
    private var currentOption: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ambianceOptions.forEach { (buttonId, String) ->
            setupAmbianceButtons(view, buttonId, String)
        }
    }

    private fun setupAmbianceButtons(view: View, buttonId: Int, ambiRes: String){
        val button = view.findViewById<Button>(buttonId)
        button.setOnClickListener{
            if(currentOption == null || currentOption != ambiRes) {
                requestPlayAmbiance(ambiRes)
                val pulseAnimation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.pulse_glow)
                button.startAnimation(pulseAnimation)
                Handler(Looper.getMainLooper()).postDelayed({
                    val fadeOutAnimation =
                        AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
                    button.startAnimation(fadeOutAnimation)
                }, 1000)
                currentOption = ambiRes;
            }
        }
    }

    private fun requestPlayAmbiance(ambiRes: String) {
        (activity as? MainActivity)?.fadeOutAmbiance {
            (activity as? MainActivity)?.playAmbiance(ambiRes)
        }
    }

}