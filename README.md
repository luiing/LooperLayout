## LooperLayout Characteristic
<li>适用于上下滚动的通知展示，内部已实现定时任务，会自动创建和回收，无须手动处理</li>
<li>支持自定义展示内容，默认只现实一个TextView(xml布局预览为此效果)</li>
<li>当有一个数据时也支持滚动播放，需配置（默认关闭此功能）</li>

## Preview

![](/preview/aa_001.gif)

## USE
    implementation 'com.uis:adsorbent:0.1.3
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    
```     
        looper1.alwaysLooper = true //true:当有一个也播放，flase:当只有一个不播放(默认值)
        looper1.refreshDataChange(arrayOf("胡先煦新恋情曝光新","祖峰新片退出戛纳新","杭州多名保安被捅","张铭恩接机徐璐新"))
        looper1.setOnLooperItemClickedListener(object :LooperLayout.OnLooperItemClickedListener{
            override fun onLooperItemClicked(position: Int, value: Any) {
                Log.e("demo","position= $position, value= ${value.toString()}")
            }
        })

        /** 自定义布局*/
        looper2.setOnLooperAdapter(object :LooperLayout.LooperAdapter{
            override fun createView(parent: ViewGroup): View? {
                return LayoutInflater.from(parent.context).inflate(R.layout.item_looper2,null)
            }

            override fun onBindView(view: View, value: Any, position: Int) {
                view.v_color.let{it as SimpleDraweeView
                    it.setImageURI(value.toString(),view.context)
                }
                view.tv_content.text = "position=$position"
            }
        })
        looper2.refreshDataChange(images)
```

### LICENSE
MIT License

Copyright (c) 2019 uis

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.