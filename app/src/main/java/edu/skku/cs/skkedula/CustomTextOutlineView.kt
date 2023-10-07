package edu.skku.cs.skkedula

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextOutlineView : AppCompatTextView {
    private var outlineColor: Int = 0
    private var outlineWidth: Float = 0f
    private val outlinePaint: Paint = Paint()

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextOutlineView)
        outlineColor = typedArray.getColor(R.styleable.CustomTextOutlineView_outlineColor, 0)
        outlineWidth = typedArray.getDimension(R.styleable.CustomTextOutlineView_outlineWidth, 2f)
        typedArray.recycle()

        // 테두리 설정
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.strokeWidth = outlineWidth
        outlinePaint.color = outlineColor
    }

    override fun onDraw(canvas: Canvas?) {
        val textColor = textColors
        val originalTextSize = textSize

        // 테두리 그리기
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = outlinePaint.strokeWidth
        setTextColor(outlinePaint.color)
        super.onDraw(canvas)

        paint.style = Paint.Style.FILL
        //textSize = originalTextSize
        setTextColor(textColor)

        // 텍스트 내용 그리기
        super.onDraw(canvas)
    }
}
