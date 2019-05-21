## LooperLayout Characteristic

## Preview

![](/preview/aa_001.gif)

## USE
```
        looper1.refreshDataChange(arrayOf("胡先煦新恋情曝光新","祖峰新片退出戛纳新","杭州多名保安被捅","张铭恩接机徐璐新"))
        looper1.setOnLooperItemListener(object :LooperLayout.OnLooperItemClickedListener{
            override fun onLooperItemClicked(position: Int, value: Any) {
                Log.e("demo","position= $position, value= ${value.toString()}")
            }
        })

        /** 自定义布局*/
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