package fastcampus.aop.part2.bookreview.model

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("title") val title: String,
    @SerializedName("link") val link: String,
    @SerializedName("image") val image: String,
    @SerializedName("author") val author: String,
    @SerializedName("price") val price: String,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("pubdate") val pubdate: String,
    @SerializedName("isbn") val isbn: String,
    @SerializedName("description") val description: String,
)