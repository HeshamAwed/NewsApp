package com.hesham.newsapp.ui.kts

import android.content.Context
import androidx.fragment.app.Fragment
import com.geniusforapp.fancydialog.builders.FancyDialogBuilder
import com.hesham.newsapp.R
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Fragment.showError(throwable: Throwable?) {
    throwable?.printStackTrace()
    requireContext().showError(throwable)
}

fun Context.showError(throwable: Throwable?) {
    throwable?.printStackTrace()
    getErrorMessage(this, throwable).takeIf { it.isNotEmpty() }?.let {
        alerter(getString(R.string.text_sorry), it)
    }
}

fun Context.alerter(title: String, message: String) {
    FancyDialogBuilder(this, R.style.AppFancyDialogStyle)
        .withImageIcon(R.drawable.ic_alert_discount)
        .withTitle(title)
        .withSubTitle(message)
        .withTitleTypeFace(R.font.bold)
        .withSubTitleTypeFace(R.font.reguler)
        .withPositive(android.R.string.ok) { w, d -> d.dismiss() }
        .show()
}

fun Fragment.alerter(title: String, message: String) {
    requireContext().alerter(title, message)
}

fun getErrorMessage(context: Context, throwable: Throwable?): String {
    return when (throwable) {
        // case no internet connection
        is UnknownHostException -> {
            context.getString(R.string.text_title_internet_connection)
        }
        is HttpException -> {
            throwable.message()
        }
        // case request time out
        is SocketTimeoutException -> {
            context.getString(R.string.text_title_internet_connection)
        }
        else -> {
            ""
        }
    }
}


fun showErrorInternet(context: Context, throwable: Throwable?): String {
    FancyDialogBuilder(context, R.style.AppFancyDialogStyle)
        .withCanCancel(false)
        .withImageIcon(R.drawable.ic_network_error)
        .withTitleTypeFace(R.font.bold)
        .withSubTitleTypeFace(R.font.reguler)
        .withTitle(R.string.text_title_internet_connection)
        .withSubTitle(R.string.text_sub_title_internet_connection)
        .withPositive(android.R.string.cancel) { w, d -> d.dismiss() }
        .show()
    return ""
}


private fun getErrorMessage(message: Any): String {
    var returnedMessage = ""
    (message as? List<*>)?.let { returnedMessage = it.toTypedArray().contentDeepToString() }
    (message as? String)?.let { returnedMessage = it }
    return returnedMessage
}