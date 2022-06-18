package com.nammaev.di.utility

import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingObjects {

    @JvmStatic
    @BindingAdapter("textValuee", "symbol")
    fun textValueWith(textView: TextView, value: String, symbol: String) {
        textView.text = "$value $symbol"
    }

    @JvmStatic
    @BindingAdapter("textValueWithPercent", "range")
    fun textValueWithPercent(textView: TextView, value: String, range: String) {
        textView.text = "$value % with $range Km range"
    }

    @JvmStatic
    @BindingAdapter("textValueWithPercent")
    fun textValueWithPercent(textView: TextView, value: String) {
        textView.text = "$value %"
    }

    @JvmStatic
    @BindingAdapter("textValueWithKm")
    fun textValueWithKm(textView: TextView, value: String) {
        textView.text = "$value Km"
    }

    @JvmStatic
    @BindingAdapter("textValue")
    fun textValue(textView: TextView, value: String) {
        textView.text = "at $value"
    }

}