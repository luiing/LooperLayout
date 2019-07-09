package com.uis.looperlayout.demo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bailian.yike.find.view.LooperLayout
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.item_looper2.view.*
import kotlinx.android.synthetic.main.ui_demo.*
import kotlin.random.Random

class DemoUi :Activity(){
    val images = arrayOf("https://www.baidu.com/img/bd_logo1.png",
                "https://gss0.bdstatic.com/70cFfyinKgQIm2_p8IuM_a/daf/pic/item/e61190ef76c6a7efca21e788f3faaf51f3de661c.jpg",
                "https://box.bdimg.com/static/fisp_static/common/img/searchbox/logo_news_276_88_1f9876a.png",
                "https://box.bdimg.com/static/fisp_static/common/img/searchbox/logo_news_276_88_1f9876a.png")
    val random = Random(255)

    override fun onCreate(savedInstanceState: Bundle?) {
        Fresco.initialize(applicationContext)
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
                view.v_color.let{it as SimpleDraweeView
                    it.setImageURI(value.toString(),view.context)
                    //Log.e("xx","value: "+value.toString())
                }
                //view.v_color.setBackgroundColor(Color.rgb(random.nextInt(),random.nextInt(),random.nextInt()))
                //view.tv_content.text = value.toString()
            }
        })
        looper2.refreshDataChange(images)
    }
}