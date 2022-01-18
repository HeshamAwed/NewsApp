package com.hesham.newsapp.domain.entities
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import androidx.room.TypeConverter
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

@Parcelize
data class Source (
    @SerializedName("name")
    val name:String): Parcelable


class SourceTypeConverter {
    @TypeConverter
    fun fromSource(source: Source): String {
        return JSONObject().apply {
            put("name", source.name)
        }.toString()
    }

    @TypeConverter
    fun toSource(source: String): Source {
        val json = JSONObject(source)
        return Source( json.getString("name"))
    }
}