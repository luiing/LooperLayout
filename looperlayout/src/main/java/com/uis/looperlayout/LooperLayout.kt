package com.bailian.yike.find.view

import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
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
    /** 包括animDelay时间*/
    private val looperDelay = 3*1000L
    private val animDelay = 1000L
    private var threads: ScheduledExecutorService? = null
    private var data:Array<out Any> = arrayOf()
    private @Volatile var current = -1
    /** item点击事件监听器*/
    private var looperListener :OnLooperItemClickedListener? = null
    /** 适配自定义item，默认是textView*/
    private var looperAdapter :LooperLayoutAdapter? = null
    private var isRefresh = false

    /** true:当有一个也播放，flase:当只有一个不播放*/
    var alwaysLooper = false

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
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)
        if(childCount <= 0 && measuredWidth > 0) {
            initView()
        }
        for (i in 0 until childCount) {
            getChildAt(i).let {
                val w = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY)
                val h = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY)
                it.measure(w, h)
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if(current >= 0 && childCount > 0 && (changed || isRefresh) ) {
            isRefresh = false
            var top = scrollY
            getChildAt((current) % 2)?.let {
                bindItemView(it,data[current])
                it.layout(0, top, measuredWidth, top + it.measuredHeight)
                top += it.measuredHeight
            }
            getChildAt((current+1) % 2)?.let {
                bindItemView(it,data[(current + 1) % data.size])
                it.layout(0, top, measuredWidth, top + it.measuredHeight)
                top += it.measuredHeight
            }
        }
    }

    private fun bindItemView(it :View,value :Any){
        if(looperAdapter != null) {
            looperAdapter?.onBindView(it,value)
        }else {
            if(it is TextView) {
                it.text = value.toString()
            }
        }
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
        threads?.scheduleWithFixedDelay(LooperRunnable(this),looperDelay,looperDelay,TimeUnit.MILLISECONDS)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        threads?.shutdownNow()
    }

    fun setOnLooperItemListener(l :OnLooperItemClickedListener){
        looperListener = l
    }

    fun setOnLooperLayoutAdapter(a :LooperLayoutAdapter){
        looperAdapter = a
        removeAllViews()
    }

    fun refreshDataChange(array :Array<out Any>){
        data = array
        current = 0
        refreshContent()
    }

    private fun refreshContent(){
        if(data.isNotEmpty()) {
            isRefresh = true
            requestLayout()
        }
    }

    private fun startAnim(){
        if(alwaysLooper ||  data.size >1) {
            val anim = ValueAnimator.ofInt(scrollY, scrollY + measuredHeight).setDuration(animDelay)
            anim.addUpdateListener {
                val v = it.animatedValue as Int
                scrollTo(0, v)
                if (it.animatedFraction >= 1f) {
                    scrollTo(0, 0)
                    refreshContent()
                    current = (current + 1).rem(data.size)
                }
            }
            anim.start()
        }
    }

    private fun initView(){
        for(i in 0 until 2){
            addView(createView())
        }
        isRefresh = true
    }

    private fun createView(): View {
        var v = looperAdapter?.createView(this)
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

    class LooperRunnable(var layout :LooperLayout) :Runnable{
        override fun run() {
            layout.handler?.post {
                layout.startAnim()
            }
        }
    }

    interface OnLooperItemClickedListener{
        fun onLooperItemClicked(position :Int,value: Any)
    }

    interface LooperLayoutAdapter{
        fun createView(parent :ViewGroup): View?

        fun onBindView(view: View,value: Any)
    }
}