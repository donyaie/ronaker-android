package com.ronaker.app.model

import com.ronaker.app.data.network.response.ProductItemResponceModel

/**
 * Class which provides a model for post
 * @constructor Sets all properties of the post
 * @property userId the unique identifier of the author of the post
 * @property id the unique identifier of the post
 * @property title the message of the post
 * @property body the content of the post
 */
data class Post(val userId: Int, val id: Int, val title: String, val body: String)


