package com.example.news.base

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.example.news.R
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("isVisible")
fun bindIsVisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("imageUrl")
fun bindImageUrl(view: ImageView, url: String?){
    if(!url.isNullOrEmpty()) {
        view.load(url)
    }else{
        view.setBackgroundResource( R.drawable.sample)
    }
}


@BindingAdapter("htmlString")
fun bindHtmlString(view: TextView, txt: String?) {
    if(!txt.isNullOrEmpty()) {
        view.text = HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }else{
        view.text = txt
    }
}

@BindingAdapter("formatDate")
fun bindFormatDate(view: TextView, txt: String?) {
    if(!txt.isNullOrEmpty()){
        try{
            val firstApiFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            val date = firstApiFormat.parse(txt)
            val resultFormat = SimpleDateFormat("MMM, dd yyyy HH:mm", Locale.ENGLISH)
            val result = date?.let { resultFormat.format(it) }
            view.text = result
        }catch (ex:Exception){
            view.text = txt
        }
    }else{
        view.text = txt
    }
}



