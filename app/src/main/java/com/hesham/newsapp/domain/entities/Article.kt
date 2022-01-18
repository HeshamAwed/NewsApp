package com.hesham.newsapp.domain.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.hesham.newsapp.ui.kts.formatToDisplayDateAndTime
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "article")
data class Article(
    @PrimaryKey()
    @SerializedName("title")
    val title: String,
    @SerializedName("author")
    val author: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("source")
    val source: Source? = null,
    @SerializedName("url")
    val url: String?,
    @SerializedName("urlToImage")
    val urlToImage: String?,
    @SerializedName("publishedAt")
    val publishedAt: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("isFavorite")
    val isFavorite: Boolean = false
) : Parcelable {
    fun getPublishDate(): String? {
        return publishedAt?.formatToDisplayDateAndTime()
    }
}
