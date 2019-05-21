package com.uis.looperlayout.demo

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bailian.yike.find.view.LooperLayout
import kotlinx.android.synthetic.main.item_looper2.view.*
import kotlinx.android.synthetic.main.ui_demo.*
import kotlin.random.Random

class DemoUi :Activity(){

    val random = Random(255)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_demo)
        looper1.refreshDataChange(arrayOf("胡先煦新恋情曝光新","祖峰新片退出戛纳新","杭州多名保安被捅","张铭恩接机徐璐新"))
        looper1.setOnLooperItemListener(object :LooperLayout.OnLooperItemClickedListener{
            override fun onLooperItemClicked(position: Int, value: Any) {
                Log.e("demo","position= $position, value= ${value.toString()}")
            }
        })


        looper2.setOnLooperLayoutAdapter(object :LooperLayout.LooperLayoutAdapter{
            override fun createView(parent: ViewGroup): View? {
                return LayoutInflater.from(parent.context).inflate(R.layout.item_looper2,null)
            }

            override fun onBindView(view: View, value: Any) {
                view.v_color.setBackgroundColor(Color.rgb(random.nextInt(),random.nextInt(),random.nextInt()))
                view.tv_content.text = value.toString()
            }
        })
        looper2.refreshDataChange(arrayOf("相亲5次遇见爱情","大疆回应美国警告","男子被辞携妻跳楼","蓝汛CEO王松被捕"))
    }
}