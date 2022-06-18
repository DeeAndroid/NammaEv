package com.nammaev.data.network.api.response

data class ResAddRating(
    var code: String? = null,
    var message: String? = null,
    var data: String? = null
)

class AddRatingRequestBody {
    var comment: String? = null
    var station: String? = null
    var rating: String? = null
}