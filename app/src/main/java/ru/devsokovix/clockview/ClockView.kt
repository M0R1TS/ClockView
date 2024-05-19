package ru.devsokovix.clockview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class ClockView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : View(context, attributeSet) {

    private var  radius: Float = 0f
    private var  centerX: Float = 0f
    private var  centerY: Float = 0f
    private var  scaleSize = 60f

    private var isStaticPictureDrawn: Boolean = false
    private lateinit var bitmap: Bitmap
    private lateinit var staticCanvas : Canvas

    private var dashColor = Color.WHITE
    private var digitColor = Color.WHITE
    private var arrowColor = Color.RED

    private lateinit var paintClockCircle: Paint
    private lateinit var dashPaintThin: Paint
    private lateinit var clockPaint: Paint

    private val rect = Rect()

    private val numerals = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    init {
        initDrawingTools()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(widthMeasureSpec)

        val chosenWidth = chooseDimension(widthMode, widthSize)
        val chosenHeight = chooseDimension(heightMode, heightSize)

        val minSide = Math.min(chosenWidth, chosenHeight)
        centerX = minSide.div(2f)
        centerY = minSide.div(2f)

        setMeasuredDimension(minSide, minSide)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius =  if (width > height){
            height.div(2f)
        } else {
            width.div(2f)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (!isStaticPictureDrawn){
            drawableStaticPicture()
        }
        canvas.drawBitmap(bitmap, centerX - radius, centerY - radius, null)
//        drawHands(canvas)
//
//        postInvalidateDelayed(500)
    }

    private fun initDrawingTools() {
        dashPaintThin = Paint().apply {
            color = dashColor
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 0.01f
            isAntiAlias = true
        }
        clockPaint = Paint(dashPaintThin).apply{
            strokeWidth = 2f
            textSize = scaleSize * 1.5f
            color = digitColor
            isAntiAlias = true
        }
        paintClockCircle = Paint().apply {
            color = Color.BLACK
            strokeWidth = 10f
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    private fun drawableStaticPicture(){
        bitmap = Bitmap.createBitmap(
            (centerX * 2).toInt(),
            (centerY * 2).toInt(),
            Bitmap.Config.ARGB_8888
        )
        staticCanvas = Canvas(bitmap)
        drawClock(staticCanvas)

        isStaticPictureDrawn = true
    }

    private fun drawClock(canvas: Canvas) {
        canvas.translate(centerX, centerY)

        canvas.drawCircle(0f, 0f, radius, paintClockCircle)

        for (number in numerals) {
            val text = number.toString()
            val digitOffset = 0.9f

            clockPaint.getTextBounds(text, 0, text.length, rect)

            val angle = Math.PI / 6 * (number - 3)

            val x = (Math.cos(angle) * radius * digitOffset - rect.width() /  2).toFloat()
            val y = (Math.sin(angle) * radius * digitOffset + rect.height() /  2).toFloat()

            canvas.drawText(text, x, y, clockPaint)
        }
    }

    private fun chooseDimension(mode: Int, size: Int) =
        when (mode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
            else -> 300
        }
}