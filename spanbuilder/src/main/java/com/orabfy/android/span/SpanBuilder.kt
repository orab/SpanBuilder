package com.orabfy.android.span

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.*

class SpanBuilder private constructor(
    content: CharSequence
) {

    companion object {

        private const val SPACE = " "

        @JvmStatic
        @JvmOverloads
        fun create(content: CharSequence = ""): SpanBuilder {
            return SpanBuilder(content)
        }
    }

    private val ssBuilder: SpannableStringBuilder = SpannableStringBuilder(content)
    private var begIndex: Int = 0
    private var endIndex: Int = content.length

    fun build(): Spanned = ssBuilder

    // region Edge indexes manipulating

    fun replace(start: Int, end: Int, text: CharSequence, tbStart: Int, tbEnd: Int): SpanBuilder {
        val length = ssBuilder.length
        val tbLength = text.length

        val tStart = transformIndex(length, start)
        val tEnd = transformIndex(length, end)
        val tTbStart = transformIndex(tbLength, tbStart)
        val tTbEnd = transformIndex(tbLength, tbEnd)

        ssBuilder.replace(tStart, tEnd, text, tbStart, tbEnd)

        begIndex = tStart
        endIndex = begIndex - tTbStart + tTbEnd
        return this
    }

    fun replace(start: Int, end: Int, text: CharSequence): SpanBuilder {
        return replace(start, end, text, 0, text.length)
    }

    fun insert(where: Int, text: CharSequence, start: Int, end: Int): SpanBuilder {
        return replace(where, where, text, start, end)
    }

    fun insert(where: Int, text: CharSequence): SpanBuilder {
        return insert(where, text, 0, text.length)
    }

    fun append(text: CharSequence, start: Int, end: Int): SpanBuilder {
        val length = ssBuilder.length
        return replace(length, length, text, start, end)
    }

    fun append(text: CharSequence): SpanBuilder {
        return append(text, 0, text.length)
    }

    fun slice(start: Int, end: Int): SpanBuilder {
        val length = ssBuilder.length
        begIndex = transformIndex(length, start)
        endIndex = transformIndex(length, end)
        return this
    }

    fun all(): SpanBuilder {
        return slice(0, Int.MAX_VALUE)
    }

    private fun transformIndex(length: Int, index: Int): Int {
        return when {
            length < index -> length
            0 <= index -> index
            -length <= index -> index + length
            else -> 0
        }
    }

    // endregion

    // region Spans manipulating

    @JvmOverloads
    fun span(span: Any, flags: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE): SpanBuilder {
        if (begIndex < endIndex) {
            ssBuilder.setSpan(span, begIndex, endIndex, flags)
        }
        return this
    }

    @JvmOverloads
    fun typeface(
        family: String,
        typeface: Typeface,
        flags: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    ): SpanBuilder {
        return span(CustomTypefaceSpan(family, typeface), flags)
    }

    @JvmOverloads
    fun textColor(color: Int, flags: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE): SpanBuilder {
        return span(ForegroundColorSpan(color), flags)
    }

    @JvmOverloads
    fun backgroundColor(color: Int, flags: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE): SpanBuilder {
        return span(BackgroundColorSpan(color), flags)
    }

    @JvmOverloads
    fun textSize(
        size: Int,
        dip: Boolean = false,
        flags: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    ): SpanBuilder {
        return span(AbsoluteSizeSpan(size, dip), flags)
    }

    @JvmOverloads
    fun fakeBold(flags: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE): SpanBuilder {
        return span(object : CharacterStyle() {
            override fun updateDrawState(tp: TextPaint?) {
                tp?.isFakeBoldText = true
            }
        }, flags)
    }

    // endregion

    // region Shortcuts

    @JvmOverloads
    fun spanWithPlaceholder(span: Any, flags: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE): SpanBuilder {
        return append(SPACE).span(span, flags)
    }

    fun padding(size: Int): SpanBuilder {
        return spanWithPlaceholder(object : ReplacementSpan() {

            override fun getSize(
                paint: Paint,
                text: CharSequence?,
                start: Int,
                end: Int,
                fm: Paint.FontMetricsInt?
            ): Int {
                return size
            }

            override fun draw(
                canvas: Canvas,
                text: CharSequence?,
                start: Int,
                end: Int,
                x: Float,
                top: Int,
                y: Int,
                bottom: Int,
                paint: Paint
            ) {
                // Ignored.
            }
        })
    }

    // endregion
}