package com.example.muscletracking.view.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.muscletracking.R

class ToolBarCustomView : LinearLayout {

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        LayoutInflater.from(context).inflate(R.layout.tool_bar_custom_view, this, true)
    }

    fun configure(titleText: String, isHideLeftButton: Boolean, isHideRightButton: Boolean) {
        val titleTextView = findViewById<TextView>(R.id.tvToolBarTitle)
        val leftButton = findViewById<ImageButton>(R.id.ibToolBarLeft)
        val rightButton = findViewById<ImageButton>(R.id.ibToolBarRight)

        titleTextView.text = titleText
        leftButton.visibility = if (isHideLeftButton) View.VISIBLE else View.INVISIBLE
        rightButton.visibility = if (isHideRightButton) View.VISIBLE else View.INVISIBLE
    }
}