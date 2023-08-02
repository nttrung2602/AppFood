package com.trungdz.appfood.domain.repository

import com.trungdz.appfood.data.model.ItemDetail
import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.model.modelrequest.CheckoutRequest
import com.trungdz.appfood.data.model.modelrequest.CreateReviewRequest
import com.trungdz.appfood.data.model.modelrequest.LoginRequest
import com.trungdz.appfood.data.model.modelresponse.*
import com.trungdz.appfood.data.util.Resource

interface IRepository {
    // Account
    suspend fun loginUser(infoLogin: LoginRequest): Resource<LoginResponse>

    // Type
    suspend fun getAllTypes(): Resource<ListTypesResponse>

    // Item
    suspend fun getAllItem(page: Int): Resource<ListItemsResponse>
    suspend fun getAllItemByIdType(page: Int, idType: Int): Resource<ListItemsResponse>
    suspend fun getAllItemByName(
        page: Int = 1,
        name: String? = null,
        typeSort: Int? = null,
    ): Resource<ListItemsResponse>

    suspend fun getDetailItem(idItem: Int): Resource<ItemDetailResponse>

    // Review
    suspend fun getAllReviewByItem(idItem: Int): Resource<ListReviewsResponse>
    suspend fun createReview(
        idItem: Int,
        idOrder: Int,
        reviewRequest: CreateReviewRequest,
    ): Resource<MessageResponse>

    // Wishlist
    suspend fun getAllItemInWishlist(): Resource<WishlistResponse>
    suspend fun updateItemInWishlist(idItem: Int): Resource<MessageResponse>

    // Cart
    suspend fun createItemInCart(idItem: Int, quantity: Int): Resource<MessageResponse>
    suspend fun getAllItemInCart(): Resource<ItemListInCartResponse>
    suspend fun deleteOneItemInCart(idItem: Int): Resource<MessageResponse>
    suspend fun increaseNumItemInCart(idItem: Int): Resource<MessageResponse>
    suspend fun decreaseNumItemInCart(idItem: Int): Resource<MessageResponse>
    suspend fun checkout(checkoutRequest: CheckoutRequest): Resource<MessageResponse>
    suspend fun updateItemInCart(idItem: Int, quantity: Int): Resource<MessageResponse>

    // Order
    suspend fun getAllOrder(): Resource<OrdersListResponse>
    suspend fun getAllItemInOrder(idOrder: Int): Resource<OrderDetailResponse>
    suspend fun chart(): Resource<ChartDataResponse>
}