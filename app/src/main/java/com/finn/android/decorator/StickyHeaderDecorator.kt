/**************************************************************************************************
 * Copyright Finn (c) 2020.                                                                       *
 **************************************************************************************************/

package com.finn.android.decorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finn.android.R
import com.finn.android.extension.toDp
import com.finn.android.extension.toSp
import kotlin.math.min

class StickyHeaderDecorator(context: Context) : RecyclerView.ItemDecoration() {

    var hideCategoryHeader: ((isHide: Boolean) -> Unit)? = null

    var updateCategoryHeader: ((categoryName: String) -> Unit)? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val colorBg = context.resources.getColor(R.color.primary_purple)
    private val colorText = context.resources.getColor(R.color.primary_white)

    private val categoryList = mutableListOf<String>()
    private val categorySet = mutableSetOf<String>()//记录有多少组子标题
    val categoryHeaderMap = mutableMapOf<String, Int>()//记录每组子标题开始的位置
    private var categoryName = ""

    companion object {
        private const val CATEGORY_COUNT_MIN = 1
        private const val SCREEN_FULL_CHILD_COUNT = 10
    }

    fun setCategoryList(value: List<String>) {
        categoryList.clear()
        categoryList.addAll(value)
        categorySet.clear()
        categorySet.addAll(value)

        //如果子标题只有一个的情况，即隐藏粘性标题
        if (categorySet.size > CATEGORY_COUNT_MIN) {
            hideCategoryHeader?.invoke(false)
        } else {
            hideCategoryHeader?.invoke(true)
        }
    }

    //设置文字属性
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorText
        textSize = 18.toSp()
    }

    private val headerMarginStart = 36.toDp() //子标题内容与左侧的距离
    private val headerSpaceHeight = 60.toDp() //为每个子标题对应最后一个item添加空隙高度
    private val headerBackgroundHeight = 40.toDp()//子标题背景高度
    private val headerBackgroundRadius = 10.toDp()//为子标题背景设置圆角

    //简单的理解
    // 设置item布局间隙（留空间给draw方法绘制）
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (isHideInventoryHeader()) return
        val adapterPosition = parent.getChildAdapterPosition(view)
        if (adapterPosition == RecyclerView.NO_POSITION) {
            return
        }
        //Top 头部
        if (isFirstOfGroup(adapterPosition)) {
            outRect.top = headerBackgroundHeight.toInt()
            categoryHeaderMap[categoryList[adapterPosition]] = adapterPosition
        }
        //Bottom 底部
        if (isEndOfGroup(adapterPosition)) {
            outRect.bottom = headerSpaceHeight.toInt()
        }
    }

    //可在此方法中绘制背景
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (isHideInventoryHeader()) return
        val count = parent.childCount
        if (count == 0) {
            return
        }
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(child)
            if (isFirstOfGroup(adapterPosition)) {
                val left = child.left.toFloat()
                val right = child.right.toFloat()
                val top = child.top.toFloat() - headerBackgroundHeight
                val bottom = child.top.toFloat()
                val radius = headerBackgroundRadius
                paint.color = colorBg
                //绘制背景
                canvas.drawRoundRect(
                    left, top, right, bottom, radius,
                    radius, paint
                )
            }
        }
    }

    //留的空间给draw方法绘制内容/粘性标题也在此设置
    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (isHideInventoryHeader()) return
        val count = parent.childCount
        if (count == 0) {
            return
        }
        //在每个背景上绘制文字
        drawHeaderTextIndex(canvas, parent)

        //绘制粘性标题
        drawStickyTimestampIndex(canvas, parent)
    }

    private fun drawHeaderTextIndex(canvas: Canvas, parent: RecyclerView) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(child)
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return
            }
            if (isFirstOfGroup(adapterPosition)) {
                val categoryName = categoryList[adapterPosition]
                val start = child.left + headerMarginStart
                val fontMetrics = textPaint.fontMetrics
                //计算文字自身高度
                val fontHeight = fontMetrics.bottom - fontMetrics.top
                val baseline =
                    child.top.toFloat() - (headerBackgroundHeight - fontHeight) / 2 - fontMetrics.bottom
                canvas.drawText(categoryName.toUpperCase(), start, baseline, textPaint)
            }
        }
    }

    private fun drawStickyTimestampIndex(canvas: Canvas, parent: RecyclerView) {
        val layoutManager = parent.layoutManager as LinearLayoutManager
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        if (firstVisiblePosition != RecyclerView.NO_POSITION) {
            val firstVisibleChildView =
                parent.findViewHolderForAdapterPosition(firstVisiblePosition)?.itemView
            firstVisibleChildView?.let { child ->
                val firstChild = parent.getChildAt(0)
                val left = firstChild.left.toFloat()
                val right = firstChild.right.toFloat()
                val top = 0.toFloat()
                val bottom = headerBackgroundHeight
                val radius = headerBackgroundRadius
                paint.color = colorBg

                val name = categoryList[firstVisiblePosition]
                if (categoryName != name) {
                    categoryName = name
                    // 监听当前滚动到的标题
                    categoryName?.let { name ->
                        updateCategoryHeader?.invoke(name)
                    }
                }
                val start = child.left + headerMarginStart
                //计算文字高度
                val fontMetrics = textPaint.fontMetrics
                val fontHeight = fontMetrics.bottom - fontMetrics.top
                val baseline =
                    headerBackgroundHeight - (headerBackgroundHeight - fontHeight) / 2 - fontMetrics.bottom

                var upwardBottom = bottom
                var upwardBaseline = baseline
                // 下一个组马上到达顶部
                if (isFirstOfGroup(firstVisiblePosition + 1)) {
                    upwardBottom = min(child.bottom.toFloat() + headerSpaceHeight, bottom)
                    if (child.bottom.toFloat() + headerSpaceHeight < headerBackgroundHeight) {
                        upwardBaseline = baseline * (child.bottom.toFloat() + headerSpaceHeight)/headerBackgroundHeight
                    }
                }
                //绘制粘性标题背景
                canvas.drawRoundRect(left, top, right, upwardBottom, radius, radius, paint)
                //绘制粘性标题
                canvas.drawText(categoryName.toUpperCase(), start, upwardBaseline, textPaint)
            }
        }
    }

    //判断是不是每组的第一个item
    private fun isFirstOfGroup(adapterPosition: Int): Boolean {
        return adapterPosition == 0 || categoryList[adapterPosition] != categoryList[adapterPosition - 1]
    }

    //判断是不是每组的最后一个item
    private fun isEndOfGroup(adapterPosition: Int): Boolean {
        if (adapterPosition + 1 == categoryList.size) return true
        return categoryList[adapterPosition] != categoryList[adapterPosition + 1]
    }

    //判断是否需要隐藏子标题
    private fun isHideInventoryHeader(): Boolean {
        return categorySet.size <= CATEGORY_COUNT_MIN || categoryList.isNullOrEmpty()
    }
}

