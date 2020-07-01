/**************************************************************************************************
 * Copyright Finn (c) 2020.                                                                       *
 **************************************************************************************************/

package com.finn.android.data

class TextDataUtils {

    //添加测试数据
    fun getTestData(): MutableList<TextBean> {
        val textBeans: MutableList<TextBean> = mutableListOf()
        textBeans.add(TextBean(0, "TitleA", "TitleA-A"))
        textBeans.add(TextBean(1, "TitleA", "TitleA-B"))
        textBeans.add(TextBean(2, "TitleA", "TitleA-C"))
        textBeans.add(TextBean(3, "TitleA", "TitleA-D"))
        textBeans.add(TextBean(4, "TitleA", "TitleA-E"))
        textBeans.add(TextBean(0, "TitleB", "TitleB-A"))
        textBeans.add(TextBean(2, "TitleC", "TitleC-C"))
        textBeans.add(TextBean(3, "TitleC", "TitleC-D"))
        textBeans.add(TextBean(4, "TitleC", "TitleC-E"))
        textBeans.add(TextBean(4, "TitleD", "TitleD-F"))
        textBeans.add(TextBean(4, "TitleD", "TitleD-F"))
        textBeans.add(TextBean(4, "TitleD", "TitleD-G"))
        textBeans.add(TextBean(1, "TitleB", "TitleB-B"))
        textBeans.add(TextBean(2, "TitleB", "TitleB-C"))
        textBeans.add(TextBean(3, "TitleB", "TitleB-D"))
        textBeans.add(TextBean(4, "TitleB", "TitleB-E"))
        textBeans.add(TextBean(0, "TitleC", "TitleC-A"))
        textBeans.add(TextBean(1, "TitleC", "TitleC-B"))

        return textBeans
    }
}