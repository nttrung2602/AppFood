package com.trungdz.appfood.data.repository.datasource

import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.model.modelrequest.CheckoutRequest
import com.trungdz.appfood.data.model.modelrequest.CreateReviewRequest
import com.trungdz.appfood.data.model.modelrequest.LoginRequest
import com.trungdz.appfood.data.model.modelresponse.*
import retrofit2.Response

interface IAppFoodRemoteDatasource {
    // Account
    suspend fun loginUser(login: LoginRequest): Response<LoginResponse>
    suspend fun forgotPassword(username: String): Response<MessageResponse>
    suspend fun verifyOTP(username: String, verifyID: String): Response<MessageResponse>
    suspend fun accessNewPassword(
        username: String,
        password: String,
        repeatPassword: String,
    ): Response<MessageResponse>

    suspend fun updateProfile(name:String,phone:String, address:String):Response<MessageResponse>
    suspend fun createAccount( username:String,  password:String, name:String,  email:String, phone:String, address:String):Response<MessageResponse>
    suspend fun changePassword( oldPassword:String, newPassword:String,  repeatPassword:String):Response<MessageResponse>

    // Type
    suspend fun getAllTypes(): Response<ListTypesResponse>

    // ItemInOrder
    suspend fun getAllItem(page: Int): Response<ListItemsResponse>
    suspend fun getAllItemByIdType(page: Int, idType: Int): Response<ListItemsResponse>
    suspend fun getDetailItem(idItem: Int): Response<ItemDetailResponse>
    suspend fun getAllItemByName(
        page: Int,
        name: String? = null,
        typeSort: Int? = null,
    ): Response<ListItemsResponse>

    // Review
    suspend fun getAllReviewByItem(idItem: Int): Response<ListReviewsResponse>
    suspend fun createReview(
        idItem: Int,
        idOrder: Int,
        reviewRequest: CreateReviewRequest,
    ): Response<MessageResponse>

    // Wishlist
    suspend fun getAllItemInWishlist(): Response<WishlistResponse>
    suspend fun updateItemInWishlist(idItem: Int): Response<MessageResponse>

    // Cart
    suspend fun createItemInCart(idItem: Int, quantity: Int): Response<MessageResponse>
    suspend fun getAllItemInCart(): Response<ItemListInCartResponse>
    suspend fun deleteOneItemInCart(idItem: Int): Response<MessageResponse>
    suspend fun increaseNumItemInCart(idItem: Int): Response<MessageResponse>
    suspend fun decreaseNumItemInCart(idItem: Int): Response<MessageResponse>
    suspend fun checkout(checkoutRequest: CheckoutRequest): Response<MessageResponse>
    suspend fun updateItemInCart(idItem: Int, quantity: Int): Response<MessageResponse>

    // Order
    suspend fun getAllOrder(): Response<OrdersListResponse>
    suspend fun getAllItemInOrder(idOrder: Int): Response<OrderDetailResponse>
    suspend fun chart(): Response<ChartDataResponse>
    suspend fun cancelOrder(idOrder:Int):Response<MessageResponse>
}