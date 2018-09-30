package com.daftmobile.daftmobiletask

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView

class Divider(context: Context) : RecyclerView.ItemDecoration() {

    private val mDivider: Drawable? = ContextCompat.getDrawable(context, R.drawable.line_divider)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.adapter!!.itemCount

        for (i in 0 until childCount - 1) {

            val child = parent.getChildAt(i) ?: continue

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight

            mDivider.apply {
                setBounds(
                    left + child.translationX.toInt(),
                    top + child.translationY.toInt(),
                    right + child.translationX.toInt(),
                    bottom + child.translationY.toInt())}
                    .apply { draw(c) }
        }
    }
}