package com.uis.looperlayout.demo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.backends.pipeline.Fresco
import com.uis.looperlayout.LooperLayout
import kotlinx.android.synthetic.main.item_looper.view.*
import kotlinx.android.synthetic.main.item_looper2.view.*
import kotlinx.android.synthetic.main.ui_demo.*

class DemoUi :Activity(){
    val images = arrayOf("https://www.baidu.com/img/bd_logo1.png",
                "https://gss0.bdstatic.com/70cFfyinKgQIm2_p8IuM_a/daf/pic/item/e61190ef76c6a7efca21e788f3faaf51f3de661c.jpg",
                "https://box.bdimg.com/static/fisp_static/common/img/searchbox/logo_news_276_88_1f9876a.png")

    override fun onCreate(savedInstanceState: Bundle?) {
        if(!Fresco.hasBeenInitialized()) {
            Fresco.initialize(applicationContext)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_demo)
        val data = arrayOf("胡先煦新恋情曝光新","祖峰新片退出戛纳新","杭州多名保安被捅","张铭恩接机徐璐新")
        looper1.setOnLooperAdapter(object :LooperLayout.LooperAdapter<String>{
            override fun getViewType(position: Int): Int {
                return position
            }

            override fun createView(parent: ViewGroup, viewType: Int): View? {
                return when(viewType){
                    0->TextView(parent.context)
                    1->LayoutInflater.from(parent.context).inflate(R.layout.item_looper,null)
                    else->LayoutInflater.from(parent.context).inflate(R.layout.item_looper1,null)
                }
            }

            override fun onBindView(view: View, value: String, position: Int) {
                when(getViewType(position)){
                    0-> (view as? TextView)?.text = value
                    1->view?.tv_name?.text = value
                    else->view?.tv_content?.text = value
                }
            }
        })
        looper1.refreshDataChange(data)
        looper1.setOnLooperItemClickedListener(object : LooperLayout.OnLooperItemClickedListener<String>{
            override fun onLooperItemClicked(position: Int, value: String) {
                Log.e("demo","position= $position, value= ${value}")
            }
        })


        looper2.animDirect = false
        looper2.animDelay = 2000
        looper2.setOnLooperAdapter(object :LooperLayout.LooperAdapter<String>{
            override fun createView(parent: ViewGroup, viewType: Int): View? {
                return LayoutInflater.from(parent.context).inflate(R.layout.item_looper2,null)
            }

            override fun onBindView(view: View, value: String, position: Int) {
                view.v_color2.setImageURI(value)
                view.tv_content.text = "position=$position $value"
            }
        })
        looper2.setOnLooperItemClickedListener(object : LooperLayout.OnLooperItemClickedListener<String>{
            override fun onLooperItemClicked(position: Int, value: String) {
                looper1.refreshDataChange(data)
            }
        })
        looper2.refreshDataChange(images)

    }
}