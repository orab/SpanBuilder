package com.orabfy.android.spanbuilderdemo

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.TextPaint
import android.text.style.LineHeightSpan
import androidx.appcompat.app.AppCompatActivity
import com.orabfy.android.span.SpanBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factor = resources.displayMetrics.density

        spanDemoView.text =
            SpanBuilder.create("pre-define text\n")
                // Change previous part text size:
                .append("text 18dp\n").textSize(18, true)
                // Change previous part text color:
                .append("text red\n").textColor(Color.RED)
                // Apply fakeBold
                .append("text fake bold\n").fakeBold()
                // re-select range
                .all().backgroundColor(Color.YELLOW)
                // Leading letter effect
                // Add blank padding
                .padding(40).append("irst line text.\n").textSize((12 * factor).toInt())
                .append(
                    SpanBuilder.create("f").textSize((28 * factor).toInt())
                        .append(" second line text.").textSize((12 * factor).toInt())
                        // Apply custom span
                        .all().span(object : LineHeightSpan {

                            override fun chooseHeight(
                                text: CharSequence?,
                                start: Int,
                                end: Int,
                                spanstartv: Int,
                                lineHeight: Int,
                                fm: Paint.FontMetricsInt?
                            ) {
                                val paint = TextPaint()
                                paint.textSize = 12 * factor
                                paint.fontMetricsInt.let {
                                    fm?.leading = it.leading
                                    fm?.top = it.top
                                    fm?.ascent = it.ascent
                                    fm?.descent = it.descent
                                    fm?.bottom = it.bottom
                                }
                            }
                        })
                        .build()
                )
                .build()
    }
}
