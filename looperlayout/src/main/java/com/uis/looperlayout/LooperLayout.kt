package com.uis.looperlayout

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.support.v4.view.ViewCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import android.widget.TextView
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class LooperLayout :ViewGroup,View.OnClickListener{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(21)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mScroller = Scroller(context)
    private var threads: ScheduledExecutorService? = null
    private var data:Array<Any> = arrayOf()
    private @Volatile var current = -1
    /** item点击事件监听器*/
    private var looperListener :OnLooperItemClickedListener<Any>? = null
    /** 适配自定义item，默认是textView*/
    private var looperAdapter :LooperAdapter<Any>? = null
    private var isRefresh = false

    /** 包括animDelay时间*/
    var looperDelay = 3*1000L
    /** 滚动动画时间*/
    var animDelay = 500L
    /** true:当有一个也播放，flase:当只有一个不播放*/
    var alwaysLooper = false
    /** true->向上滚动,false->向下滚动*/
    var animDirect = true
    var enable = true

    init {
        /**支持xml布局预览*/
        if(isInEditMode){
            data = arrayOf("LooperLayout item one","LooperLayout item two")
            current = 0
        }
        descendantFocusability = FOCUS_BLOCK_DESCENDANTS
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if(current >= 0) layout()
    }

    fun layout(){
        for(i in childCount until 2){
            val position = (current+i)%data.size
            addView(createView(position))
        }
        var top = scrollY
        for(i in 0 until childCount){
            val position = (current+i)%data.size
            getChildAt(i)?.let {
                bindItemView(it,data[position],position)
                it.layout(0, top, measuredWidth, top + it.measuredHeight)
                top += it.measuredHeight*getAnimSign()
            }
        }
    }

    override fun computeScroll() {
        if(!mScroller.isFinished && mScroller.computeScrollOffset()){
            val dy = mScroller.currY
            scrollTo(0,dy)
            ViewCompat.postInvalidateOnAnimation(this)
        }else{
            endScroll()
        }
    }

    private fun startScroll(){
        if(enable&&(data.size>1 || alwaysLooper)) {
            isRefresh = true
            val dy = measuredHeight*getAnimSign()
            mScroller.startScroll(0,scrollY,0,dy,animDelay.toInt())
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun endScroll(){
        if(mScroller.isFinished && isRefresh){
            isRefresh = false
            current = (current + 1).rem(data.size)
            if(childCount > 1){
                removeViewAt(0)
                getChildAt(0).layout(0, 0, measuredWidth,   measuredHeight)
                scrollTo(0,0)
            }
        }
    }

    private fun getAnimSign()=if(animDirect) 1 else -1

    private fun bindItemView(it :View,value :Any, position: Int){
        if(looperAdapter != null) {
            looperAdapter?.onBindView(it, value,position)
        }else {
            if(it is TextView) {
                it.text = value.toString()
            }
        }
        it.measure(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY))
    }

    override fun onClick(v: View?) {
        if(current >= 0){
            looperListener?.onLooperItemClicked(current,data[current])
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if(threads == null || threads?.isShutdown == true){
            threads = ScheduledThreadPoolExecutor(2)
        }
        threads?.scheduleWithFixedDelay({
            startScroll()
        },looperDelay,looperDelay, TimeUnit.MILLISECONDS)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        threads?.shutdownNow()
    }

    @Suppress("UNCHECKED_CAST")
    fun setOnLooperItemClickedListener(l :OnLooperItemClickedListener<out Any>){
        looperListener = l as OnLooperItemClickedListener<Any>
    }

    @Suppress("UNCHECKED_CAST")
    fun setOnLooperAdapter(adapter :LooperAdapter<out Any>){
        looperAdapter = adapter as LooperAdapter<Any>
        removeAllViews()
    }

    @Suppress("UNCHECKED_CAST")
    fun refreshDataChange(array :Array<out Any>){
        data = array as Array<Any>
        current = 0
    }

    private fun createView(position: Int): View {
        var v = looperAdapter?.let {
            it.createView(this,it.getViewType(position))
        }
        /** 默认是TextView*/
        if(v == null) {
            v = TextView(context)
            v.gravity = Gravity.CENTER_VERTICAL
            v.textSize = 14f
            v.maxLines = 1
            v.ellipsize = TextUtils.TruncateAt.END
            v.setTextColor(Color.parseColor("#888888"))
        }
        v.setOnClickListener(this)
        return v
    }

    interface OnLooperItemClickedListener<T: Any>{
        fun onLooperItemClicked(position :Int,value: T)
    }

    interface LooperAdapter<T:Any> {
        fun createView(parent :ViewGroup,viewType:Int): View?

        fun getViewType(position: Int):Int = 0

        fun onBindView(view: View,value: T,position :Int)
    }
}