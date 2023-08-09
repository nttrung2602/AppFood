package com.trungdz.appfood.data.api

import com.trungdz.appfood.data.model.ItemDetail
import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.model.modelrequest.*
import com.trungdz.appfood.data.model.modelresponse.*
import retrofit2.Response
import retrofit2.http.*

interface AppFoodApiService {
    // Define endpoint here

    // account
    @POST("account/login")
    suspend fun loginUser(@Body infoLogin: LoginRequest): Response<LoginResponse>

    @POST("account/forgotpassword")
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Response<MessageResponse>

    @POST("account/forgotpassword/verify")
    suspend fun verifyOTP(@Body forgotPasswordRequest: ForgotPasswordRequest):Response<MessageResponse>

    @POST("account/forgotpassword/verify/success")
    suspend fun accessNewPassword(@Body forgotPasswordRequest: ForgotPasswordRequest):Response<MessageResponse>

    @PUT("account/updateprofile")
    suspend fun updateProfile(@Body updateProfileRequest: UpdateProfileRequest):Response<MessageResponse>

    @POST("account/create")
    suspend fun createAccount(@Body createAccountRequest: CreateAccountRequest):Response<MessageResponse>

    @PUT("account/changepassword")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest):Response<MessageResponse>

    // types
    @GET("types")
    suspend fun getAllTypes(): Response<ListTypesResponse>

    // item
    @GET("items/page/{page}")
    suspend fun getAllItem(
        @Path("page") page: Int,
        @Query("name") name: String? = null,
        @Query("id_type") idType: Int? = null,
        @Query("typesort") typeSort: Int? = null,
    ): Response<ListItemsResponse>

    @GET("items/detail/{id_item}")
    suspend fun getDetailItem(@Path("id_item") idItem: Int): Response<ItemDetailResponse>

    // review
    @GET("reviews/{id_item}")
    suspend fun getAllReviewByItem(@Path("id_item") idItem: Int): Response<ListReviewsResponse>

    @POST("reviews/{id_item}")
    suspend fun createReview(
        @Path("id_item") idItem: Int,
        @Query("id_order") idOrder: Int,
        @Body reviewRequest: CreateReviewRequest,
    ): Response<MessageResponse>

    // wishlist
    @GET("wishlist")
    suspend fun getAllItemInWishList(): Response<WishlistResponse>

    @POST("wishlist/{id_item}")
    suspend fun updateItemInWishlist(@Path("id_item") idItem: Int): Response<MessageResponse>

    // cart
    @POST("cart/add/{id_item}")
    suspend fun createItemInCart(
        @Path("id_item") idItem: Int,
        @Body updateNumItemInCartRequest: UpdateNumItemInCartRequest,
    ): Response<MessageResponse>

    @GET("cart")
    suspend fun getAllItemInCart(): Response<ItemListInCartResponse>

    @DELETE("cart/remove/{id_item}")
    suspend fun deleteOneItemInCart(@Path("id_item") idItem: Int): Response<MessageResponse>

    @POST("cart/increase/{id_item}")
    suspend fun increaseNumItemInCart(@Path("id_item") idItem: Int): Response<MessageResponse>

    @POST("cart/decrease/{id_item}")
    suspend fun decreaseNumItemInCart(@Path("id_item") idItem: Int): Response<MessageResponse>

    @POST("cart/checkout")
    suspend fun checkout(@Body checkoutRequest: CheckoutRequest): Response<MessageResponse>

    @POST("cart/update/{id_item}")
    suspend fun updateItemInCart(
        @Path("id_item") idItem: Int,
        @Body
        UpdateNumItemInCartRequest: UpdateNumItemInCartRequest,
    ): Response<MessageResponse>

    // Order
    @GET("orders")
    suspend fun getAllOrder(): Response<OrdersListResponse>

    @GET("orders/detail/{id_order}")
    suspend fun getAllItemInOrder(@Path("id_order") idOrder: Int): Response<OrderDetailResponse>

    @GET("orders/chart")
    suspend fun chart(): Response<ChartDataResponse>

    @GET("orders/cancel/{id_order}")
    suspend fun cancelOrder (@Path("id_order") idOrder:Int):Response<MessageResponse>
}