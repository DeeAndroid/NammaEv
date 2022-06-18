package com.nammaev.utils

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory

class CrossFadeDrawable : TransitionFactory<Drawable> {
    private val resourceTransition: DrawableCrossFadeTransition =
        DrawableCrossFadeTransition(300, true)

    override fun build(dataSource: DataSource?, isFirstResource: Boolean): Transition<Drawable> =
        resourceTransition
}