package com.example.steven.myapplication

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.example.steven.myapplication.base.TriangleView
import com.example.steven.myapplication.utils.LogUtil
import com.example.steven.myapplication.utils.ShapeUtils
import com.example.steven.myapplication.utils.WindowUtil
import razerdp.basepopup.BasePopupWindow
import java.util.*


/**
 * by wyn at 19/3/12
 * 气泡方式的 pop
 */

abstract class BubblePop<T : BubblePop<T>>(context: Context): BasePopupWindow(context) {

    private lateinit var triangleView: TriangleView
    private lateinit var container: RelativeLayout
    private lateinit var innerContainer: LinearLayout
    private lateinit var bubbleView: View

    private var anchorView: View ? = null

    private var redius = 0      //气泡半径
    private var bubbleColor = 0 //气泡背景
    private var triangleMargin = 20
    private var triangleGravity = -1
    private var alignRight = false      //是否对齐anchorView 的右边

    override fun onCreateContentView(): View{
        container = createPopupById(R.layout.base_popwindow_view) as RelativeLayout
        return container
    }


    private fun configView(){
        bubbleColor = if(bubbleColor == 0) Color.parseColor("#FFFFFF") else bubbleColor
        triangleMargin = WindowUtil.dip2px(context, triangleMargin.toFloat())

        triangleView = container.findViewById(R.id.triangle_view)
        triangleView.color = bubbleColor
        triangleView.gravity = popupGravity

        innerContainer = container.findViewById(R.id.ll_content)
        redius = WindowUtil.dip2px(context, redius.toFloat())

        if(redius != 0){
            innerContainer.background = ShapeUtils.getShape(bubbleColor, 0, 0, redius)
        } else {
            innerContainer.background = ShapeUtils.getShape(bubbleColor, 0, 0, 0)
        }

        bubbleView = onCreateBubbleView()
        innerContainer.addView(bubbleView)


        //修正pop弹窗的位置 让其适应屏幕
        container.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                container.viewTreeObserver.removeOnGlobalLayoutListener(this)

                if(anchorView == null){
                    return
                }

                val local = intArrayOf(0, 0)
                anchorView!!.getLocationOnScreen(local)

                if(popupGravity and Gravity.TOP == Gravity.TOP){
                    val anchorY = local[1]
                    val exceed = anchorY - container.height + offsetY < 0

                    if(exceed){
                        popupGravity = popupGravity and Gravity.TOP.inv()
                        popupGravity = popupGravity or Gravity.BOTTOM
                    }
                }

                if(popupGravity and Gravity.BOTTOM == Gravity.BOTTOM){
                    val anchorY = local[1]
                    val exceed = anchorY + container.height + offsetY > WindowUtil.getScreenH(context)

                    if(exceed){
                        popupGravity = popupGravity and Gravity.BOTTOM.inv()
                        popupGravity = popupGravity or Gravity.TOP
                    }
                }

                triangleView.gravity = popupGravity
                layoutView()

                if(alignRight && popupGravity and Gravity.TOP == Gravity.TOP || popupGravity and Gravity.BOTTOM == Gravity.BOTTOM){
                    offsetX = -container.width + (anchorView?.width?: 0) + offsetX
                }
            }
        })

        layoutView()
    }


    //摆放三角形的位置
    private fun layoutView(){
        clearRule(innerContainer)
        clearRule(triangleView)

        val tParam = triangleView.layoutParams as RelativeLayout.LayoutParams
        val iParam = innerContainer.layoutParams as RelativeLayout.LayoutParams

        if(popupGravity and Gravity.TOP == Gravity.TOP){

            tParam.leftMargin = triangleMargin + redius
            tParam.rightMargin = triangleMargin + redius
            tParam.addRule(RelativeLayout.BELOW, R.id.ll_content)

            if(triangleGravity == Gravity.LEFT){
                tParam.addRule(RelativeLayout.ALIGN_LEFT, R.id.ll_content)
            }

            if(triangleGravity == Gravity.RIGHT){
                tParam.addRule(RelativeLayout.ALIGN_RIGHT, R.id.ll_content)
            }
        }

        if(popupGravity and Gravity.BOTTOM == Gravity.BOTTOM){
            iParam.addRule(RelativeLayout.BELOW, R.id.triangle_view)

            tParam.leftMargin = triangleMargin + redius
            tParam.rightMargin = triangleMargin + redius

            if(triangleGravity == Gravity.LEFT){
                tParam.addRule(RelativeLayout.ALIGN_LEFT, R.id.ll_content)
            }

            if(triangleGravity == Gravity.RIGHT){
                tParam.addRule(RelativeLayout.ALIGN_RIGHT, R.id.ll_content)
            }
        }

        if(triangleGravity == Gravity.CENTER_VERTICAL){
            tParam.addRule(RelativeLayout.CENTER_VERTICAL)
        }

        if(triangleGravity == Gravity.CENTER_HORIZONTAL){
            tParam.addRule(RelativeLayout.CENTER_HORIZONTAL)
        }

        triangleView.layoutParams = tParam
        container.requestLayout()
    }


    //清楚布局约束规则
    private fun clearRule(view: View){
        val param = view.layoutParams as RelativeLayout.LayoutParams

        for(i in param.rules.indices){
            param.rules[i] = 0
        }

        view.layoutParams = param
    }


    abstract fun onCreateBubbleView(): View


    override fun showPopupWindow(anchorView: View?) {
        this.anchorView = anchorView
        configView()
        super.showPopupWindow(anchorView)
    }


    //设置圆角
    fun setRedius(redius: Int): T{
        this.redius = redius
        return this as T
    }

    //设置气泡背景
    fun setBubbleColor(bubbleColor: Int): T{
        this.bubbleColor = bubbleColor
        return this as T
    }

    //设置三角形margin
    fun setTriangleMargin(triangleMargin: Int): T{
        this.triangleMargin = triangleMargin
        return this as T
    }

    //设置三角形位置
    fun setTriangleGravity(triangleGravity: Int): T{
        this.triangleGravity = triangleGravity
        return this as T
    }

    //设置右对齐
    fun setAlignRight(alignRight: Boolean): T{
        this.alignRight = alignRight
        return this as T
    }
}