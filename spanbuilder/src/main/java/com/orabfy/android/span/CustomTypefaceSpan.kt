package com.orabfy.android.span

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

class CustomTypefaceSpan(
    family: String,
    private val customTypeface: Typeface
) : TypefaceSpan(family) {

    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeface(ds, customTypeface)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeface(paint, customTypeface)
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun applyCustomTypeface(paint: TextPaint, typeface: Typeface) {
        val oldStyle = paint.typeface?.style ?: 0

        val fake = oldStyle and typeface.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }
        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }

        paint.typeface = typeface
    }
}