package com.example.slotmachine4.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.slotmachine4.R

class BonusUserTextView (context: Context, attrs: AttributeSet)
    : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    init {
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 50F)
        setTypeface(null, Typeface.BOLD)
        typeface = ResourcesCompat.getFont(context, R.font.f19847)
        text = resources.getText(R.string.bonus_for_user)
    }
}