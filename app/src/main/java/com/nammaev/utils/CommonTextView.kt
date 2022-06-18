package com.nammaev.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

@SuppressLint("AppCompatCustomView")
class CommonTextView : AppCompatTextView {
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        if (!isInEditMode) {
            /*val tf = Typeface.createFromAsset(context.assets, "googlesansregular.ttf")
            typeface = tf*/
        }
    }
}