package com.hartsa.storyapp.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.hartsa.storyapp.R

class EmailEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {


    init {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val emailReq = context.getString(R.string.email_req)
                if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    error = emailReq
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}