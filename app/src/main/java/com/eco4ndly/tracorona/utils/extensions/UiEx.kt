package com.eco4ndly.tracorona.utils.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * A Sayan Porya code on 2020-02-09
 */

fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(resource, this, attachToRoot)

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(
        this.context,
        DividerItemDecoration.VERTICAL
    )
    val drawable = ContextCompat.getDrawable(
        this.context,
        drawableRes
    )
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}

fun TextView.makeTextPortionClickable(
    body: String? = null,
    underLine: Boolean = true,
    highlightColor: Int = Color.TRANSPARENT,
    clickableText: String,
    onClick: () -> Unit
) {
    val str = body ?: this.text
    val startPoint = str.indexOf(clickableText)
    val endPoint = startPoint + clickableText.length
    val ss = SpannableString(str)
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) {
            onClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = underLine
        }
    }
    ss.setSpan(clickableSpan, startPoint, endPoint, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    text = ss
    movementMethod = LinkMovementMethod.getInstance()
    this.highlightColor = highlightColor
}

@ExperimentalCoroutinesApi
fun EditText.textChanges(): Flow<String> = callbackFlow {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            safeOffer(p0.toString())
        }

    }
    addTextChangedListener(textWatcher)
    awaitClose {
        addTextChangedListener(null)
    }
}


fun Activity.openSoftKeyboard(editText: EditText) {
    val imm: InputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.hideSoftKeyboard() {
    val view: View? = currentFocus
    view?.let {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

