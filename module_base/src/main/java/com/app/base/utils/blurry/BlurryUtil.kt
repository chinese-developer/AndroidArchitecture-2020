/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */
package com.app.base.utils.blurry

import android.R.id
import android.app.Activity
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.ViewGroup
import com.blankj.utilcode.util.ScreenUtils
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
fun Activity.drawCacheAndCreateBitmap(
  width: Int = ScreenUtils.getScreenWidth(),
  height: Int = ScreenUtils.getScreenHeight()
): Bitmap? {
  return try {
    val view = findViewById<ViewGroup>(id.content).getChildAt(0)
    view.isDrawingCacheEnabled = true
    var cacheBitmap = view.drawingCache
    cacheBitmap = Bitmap.createBitmap(cacheBitmap, 0, 0, width, height)
    view.isDrawingCacheEnabled = false
    cacheBitmap
  } catch (e: Exception) {
    e.printStackTrace()
    null
  }
}

/**
 * 高斯模糊 bitmap
 *
 * blurRadius: 设置渲染的模糊程度, 25f是最大模糊度。
 * scale: 图片缩放比例
 */
fun Activity.blurBitmap(
  bitmap: Bitmap?,
  blurRadius: Float = 0f,
  scale: Float = 0.3f
): Bitmap? {
  if (bitmap == null) return null

  val outputBitmap: Bitmap?
  val radius: Float = when (blurRadius) {
    in 0f..25f -> blurRadius
    else -> 0f
  }

  return try {
    Class.forName("android.renderscript.ScriptIntrinsicBlur")
    // 计算图片缩小后的长宽
    val width = (bitmap.width * scale).roundToInt()
    val height = (bitmap.height * scale).roundToInt()
    if (width < 2 || height < 2) {
      return null
    }
    val inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
    outputBitmap = Bitmap.createBitmap(inputBitmap)
    val rs = RenderScript.create(this)
    val blurScript =
      ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
    val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
    // 设置渲染的模糊程度, 25f是最大模糊度
    blurScript.setRadius(radius)
    // 设置blurScript对象的输入内存
    blurScript.setInput(tmpIn)
    // 将输出数据保存到输出内存中
    blurScript.forEach(tmpOut)
    // 将数据填充到Allocation中
    tmpOut.copyTo(outputBitmap)
    outputBitmap
  } catch (e: java.lang.Exception) {
    null
  }
}