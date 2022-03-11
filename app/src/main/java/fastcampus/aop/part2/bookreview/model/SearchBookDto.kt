package fastcampus.aop.part2.bookreview.model

import com.google.gson.annotations.SerializedName

data class SearchBookDto(
    @SerializedName("items") val books: List<Book>,
)
