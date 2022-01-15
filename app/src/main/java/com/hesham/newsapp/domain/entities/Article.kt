package com.hesham.newsapp.domain.entities
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.Date;

@Parcelize
@Entity(tableName = "article")
data class Article(
    @PrimaryKey(autoGenerate = false)
    var id:Int,
    @SerializedName("author")
    val author:String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("urlToImage")
    val urlToImage: String,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("isFavorite")
    val isFavorite:Boolean = false
):Parcelable
