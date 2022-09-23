package com.example.newsapp.util

import android.view.animation.AlphaAnimation
import android.view.animation.Animation

object CustomAnimation {

    fun buttonAnime(): Animation {
        val animation: Animation =
            AlphaAnimation(1f, 0.5f) //to change visibility from visible to half-visible
        animation.duration = 25 // 100 millisecond duration for each animation cycle
        animation.repeatMode =
            Animation.REVERSE //animation will start from end point once ended.
        return animation
    }

}