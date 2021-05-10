package ru.senchenko.thecloack

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var aHour: Float = 0.0f
    private var aMinute: Float = 0.0f
    private var aSecond: Float = 0.0f

    private val paintDesk: Paint = Paint()
    private val paintDeskMinutes = Paint()
    private val paintHour: Paint = Paint()
    private val paintMinutes: Paint = Paint()
    private val paintSecond: Paint = Paint()

    private val centerX: Float = (Resources.getSystem().displayMetrics.widthPixels / 2).toFloat()
    private val centerY: Float = (Resources.getSystem().displayMetrics.heightPixels / 2).toFloat()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        paintDesk.color = ContextCompat.getColor(context, R.color.purple_700)
        paintDesk.style = Paint.Style.STROKE
        paintDesk.strokeWidth = 30F
        paintDeskMinutes.color = ContextCompat.getColor(context, R.color.purple_200)
        paintDeskMinutes.strokeWidth = 8F
        paintHour.color = ContextCompat.getColor(context, R.color.purple_500)
        paintHour.strokeWidth = 20F
        paintMinutes.color = ContextCompat.getColor(context, R.color.purple_700)
        paintMinutes.strokeWidth = 16F
        paintSecond.color = ContextCompat.getColor(context, R.color.light_green)
        paintSecond.strokeWidth = 8F
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawDesk(canvas)
        canvas?.save()
        canvas?.rotate(aSecond / 60.0f * 360.0f, centerX, centerY)
        canvas?.drawLine(
            centerX,
            centerY,
            centerX,
            centerY - 300F,
            paintSecond
        )
        canvas?.restore()
        canvas?.save()
        canvas?.rotate(aMinute / 60.0f * 360.0f, centerX, centerY)
        canvas?.drawLine(
            centerX,
            centerY,
            centerX,
            centerY - 240F,
            paintMinutes
        )
        canvas?.restore()
        canvas?.save()
        canvas?.rotate(aHour / 12.0f * 360.0f, centerX, centerY)
        canvas?.drawLine(
            centerX,
            centerY,
            centerX,
            centerY - 160F,
            paintHour
        )
        canvas?.drawPoint(centerX, centerY, paintDesk)
        changeTime()
        invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val mClock = Clock.systemDefaultZone()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun changeTime() {
        val nowMillis = mClock.millis()
        val localDateTime: LocalDateTime = toLocalDateTime(nowMillis, mClock.zone)
        val hour = localDateTime.hour
        val minute = localDateTime.minute
        val second = localDateTime.second
        aSecond = second.toFloat()
        aMinute = minute + second / 60.0f
        aHour = hour + aMinute / 60.0f
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun toLocalDateTime(timeMillis: Long, zoneID: ZoneId?): LocalDateTime {
        val instant: Instant = Instant.ofEpochMilli(timeMillis)
        return LocalDateTime.ofInstant(instant, zoneID)
    }

    private fun drawDesk(canvas: Canvas?) {
        val angleHour = 30f
        val angleMinute = 6f
        for (i in 1..60) {
            canvas?.rotate(angleMinute, centerX, centerY)
            canvas?.drawLine(centerX, centerY - 360F, centerX, centerY - 300F, paintDeskMinutes)
        }
        canvas?.drawCircle(centerX, centerY, 360F, paintDesk)
        for (i in 1..12) {
            canvas?.rotate(angleHour, centerX, centerY)
            canvas?.drawLine(centerX, centerY - 360F, centerX, centerY - 300F, paintDesk)
        }
    }
}
