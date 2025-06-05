package com.example.unsc_datapad

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button

class AmbianceFragment : Fragment(R.layout.fragment_ambiance) {

    private val ambianceOptions = listOf(R.id.btn_package to "ambiance/the_package.mp3", R.id.btn_osiris to "", R.id.btn_spear to "", R.id.btn_nightfall to "", R.id.btn_exodus to "",
        R.id.btn_dawn to "", R.id.btn_midnight to "", R.id.btn_autumn to "", R.id.btn_cairo to "", R.id.btn_armory to "", R.id.btn_requium to "", R.id.btn_halo to "", R.id.btn_forerunner to "",
        R.id.btn_flood to "", R.id.btn_corvette to "")
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