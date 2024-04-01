package com.example.testhetics.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private var spaceSize: Int) : RecyclerView.ItemDecoration() {
    init {
        spaceSize *= 2
    }

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceSize
            }

            left = spaceSize
            right = spaceSize
            bottom = spaceSize
        }
    }
}