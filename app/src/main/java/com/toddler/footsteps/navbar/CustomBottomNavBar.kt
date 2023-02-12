package com.toddler.footsteps.navbar

import android.content.Context
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomBottomNavBar(context: Context) : BottomNavigationView(context) {

    init {

    }


}
/*

class CurvedBottomNavigationView : BottomNavigationView {
    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        mPath = Path()
        mPaint = Paint()
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE)
        mPaint.setColor(Color.WHITE)
        setBackgroundColor(Color.TRANSPARENT)
    }
}*/
