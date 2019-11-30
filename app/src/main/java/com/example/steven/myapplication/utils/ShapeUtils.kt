package com.example.steven.myapplication.utils

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable

/**
 * 来自 伍玉南 的装逼小尾巴 on 18/8/30.
 */
class ShapeUtils {

    companion object {

        /**
         * 获取颜色的drawable
         */
        @JvmStatic
        fun getShape(solidColor: Int? = 0, strokeColor: Int? = 0, strokeWidth: Int? = 0, radius: Int? = 0): GradientDrawable {
            val dra = GradientDrawable()
            if(solidColor != null && 0 != solidColor){
                dra.setColor(solidColor)
            }

            if(strokeWidth != 0){
                dra.setStroke(strokeWidth!!, strokeColor!!)
            }

            if(radius != 0){
                dra.cornerRadius = radius?.toFloat()?: 0f
            }
            return dra
        }

        /**
         * 获取渐变色的drawable
         * @param direction 渐变方向 [android.graphics.drawable.GradientDrawable.Orientation]
         */
        @JvmStatic
        fun getGradientShape(direction: GradientDrawable.Orientation, colors: IntArray?): GradientDrawable {
            val dra = GradientDrawable(direction, colors)
            return dra
        }
    }

}