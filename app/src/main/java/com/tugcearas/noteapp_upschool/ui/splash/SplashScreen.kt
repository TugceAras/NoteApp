package com.tugcearas.noteapp_upschool.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tugcearas.noteapp_upschool.R

@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Handler(Looper.getMainLooper()).postDelayed({
            val action = SplashScreenDirections.actionSplashScreenToNotesScreen()
            findNavController().navigate(action)
        },3000)
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }
}