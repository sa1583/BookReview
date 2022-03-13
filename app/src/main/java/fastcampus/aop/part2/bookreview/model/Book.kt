package fastcampus.aop.part2.bookreview.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    @SerializedName("isbn") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("link") val link: String,
    @SerializedName("image") val image: String,
    @SerializedName("author") val author: String,
    @SerializedName("price") val price: String,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("pubdate") val pubdate: String,
    @SerializedName("description") val description: String,
) : Parcelable