package com.example.contact.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.example.contact.R

/**
 * @author caihn
 * @create 2020-06-28-15:11
 * 自定义侧边栏
 */
class SideBarView(context: Context?, attrs: AttributeSet? = null):
    View(context, attrs){

    private val mPaint: Paint = Paint() //画笔
    private var mWidth = 0
    private var mHeight = 0
    private val letterArray = arrayOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
        "X", "Y", "Z", "#"
    )
    private val textSize: Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        14f,
        resources.displayMetrics
    ).toInt()
    private var index = 0
    private var choose = -1
//    private var mFlag = false
    private var mTouchListener: LetterTouchListener? = null

    init {
        // TODO Auto-generated constructor stub
        mPaint.textSize = textSize.toFloat()
        mPaint.color = Color.BLACK
        mPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas)
        mWidth = width
        mHeight = height
        val strHeight = (mHeight / letterArray.size).toFloat() // 设置每一个字母占的高度
        for (i in letterArray.indices) {
            val x = mWidth / 2 - mPaint.measureText(letterArray[i]) / 2
            val y = strHeight * i + strHeight
            if (choose == i) {
                mPaint.color = resources.getColor(R.color.color_3) //选中之后的颜色
            } else {
                mPaint.color = resources.getColor(R.color.color_2) //未选中的颜色
            }
            canvas.drawText(letterArray[i], x, y, mPaint)
        }
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // TODO Auto-generated method stub
        val y = event.y //获得事件的点击位置
        index = (y / mHeight * letterArray.size).toInt() //获得相对应的字母
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mTouchListener!!.setLetterVisibility(VISIBLE)
                mTouchListener!!.setLetter(letterArray[index])
                choose = index
//                mFlag = true
            }
            MotionEvent.ACTION_MOVE -> if (index > 0) {
                mTouchListener!!.setLetter(letterArray[index])
                choose = index
//                mFlag = true
            }
            MotionEvent.ACTION_UP -> {
                mTouchListener!!.setLetterVisibility(GONE)
                choose = -1
////                mFlag = false
            }
        }
        return true
    }

    fun setLetterTouchListener(listener: LetterTouchListener?) {
        mTouchListener = listener
    }


}