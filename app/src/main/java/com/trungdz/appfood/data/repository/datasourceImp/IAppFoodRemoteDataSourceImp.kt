package com.trungdz.appfood.data.repository.datasourceImp

import com.trungdz.appfood.data.api.AppFoodApiService
import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.model.modelrequest.*
import com.trungdz.appfood.data.model.modelresponse.*
import com.trungdz.appfood.data.repository.datasource.IAppFoodRemoteDatasource
import retrofit2.Response
import javax.inject.Inject

class IAppFoodRemoteDataSourceImp @Inject constructor(private val appFoodApiService: AppFoodApiService) :
    IAppFoodRemoteDatasource {
    // Account
    override suspend fun loginUser(login: LoginRequest): Response<LoginResponse> {
        return appFoodApiService.loginUser(login)
    }

    override suspend fun forgotPassword(
        username: String,
    ): Response<MessageResponse> {
        return appFoodApiService.forgotPassword(ForgotPasswordRequest(username))
    }

    override suspend fun verifyOTP(username: String, verifyID: String): Response<MessageResponse> {
        return appFoodApiService.forgotPassword(ForgotPasswordRequest(username,verifyID))
    }

    override suspend fun accessNewPassword(
        username: String,
        password: String,
        repeatPassword: String
    ): Response<MessageResponse> {
        return appFoodApiService.accessNewPassword(ForgotPasswordRequest(username=username,password=password,repeatPassword=repeatPassword))
    }

    override suspend fun updateProfile(
        name: String,
        phone: String,
        address: String,
    ): Response<MessageResponse> {
        return appFoodApiService.updateProfile(UpdateProfileRequest(name, phone, address))
    }

    override suspend fun createAccount(
        username: String,
        password: String,
        name: String,
        email: String,
        phone: String,
        address: String,
    ): Response<MessageResponse> {
        return appFoodApiService.createAccount(CreateAccountRequest(username, password, name, email, phone, address))
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        repeatPassword: String,
    ): Response<MessageResponse> {
        return appFoodApiService.changePassword(ChangePasswordRequest(oldPassword,newPassword, repeatPassword))
    }

    // Type
    override suspend fun getAllTypes(): Response<ListTypesResponse> {
        return appFoodApiService.getAllTypes()
    }

    // ItemInOrder
    override suspend fun getAllItem(page: Int): Response<ListItemsResponse> {
        return appFoodApiService.getAllItem(page = page)
    }

    override suspend fun getAllItemByIdType(page: Int, idType: Int): Response<ListItemsResponse> {
        return appFoodApiService.getAllItem(page = page, idType = idType)
    }

    override suspend fun getAllItemByName(
        page: Int,
        name: String?,
        typeSort: Int?,
    ): Response<ListItemsResponse> {
        return appFoodApiService.getAllItem(page = page, name = name, typeSort = typeSort)
    }

    override suspend fun getDetailItem(idItem: Int): Response<ItemDetailResponse> {
        return appFoodApiService.getDetailItem(idItem)
    }

    // Review
    override suspend fun getAllReviewByItem(idItem: Int): Response<ListReviewsResponse> {
        return appFoodApiService.getAllReviewByItem(idItem)
    }

    override suspend fun createReview(
        idItem: Int, idOrder: Int, reviewRequest: CreateReviewRequest,
    ): Response<MessageResponse> {
        return appFoodApiService.createReview(idItem, idOrder, reviewRequest)
    }

    // Wishlist
    override suspend fun getAllItemInWishlist(): Response<WishlistResponse> {
        return appFoodApiService.getAllItemInWishList()
    }

    override suspend fun updateItemInWishlist(idItem: Int): Response<MessageResponse> {
        return appFoodApiService.updateItemInWishlist(idItem)
    }

    // Cart
    override suspend fun createItemInCart(idItem: Int, quantity: Int): Response<MessageResponse> {
        return appFoodApiService.createItemInCart(idItem, UpdateNumItemInCartRequest(quantity))
    }

    override suspend fun getAllItemInCart(): Response<ItemListInCartResponse> {
        return appFoodApiService.getAllItemInCart()
    }

    override suspend fun deleteOneItemInCart(idItem: Int): Response<MessageResponse> {
        return appFoodApiService.deleteOneItemInCart(idItem)
    }

    override suspend fun increaseNumItemInCart(idItem: Int): Response<MessageResponse> {
        return appFoodApiService.increaseNumItemInCart(idItem)
    }

    override suspend fun decreaseNumItemInCart(idItem: Int): Response<MessageResponse> {
        return appFoodApiService.decreaseNumItemInCart(idItem)
    }

    override suspend fun checkout(checkoutRequest: CheckoutRequest): Response<MessageResponse> {
        return appFoodApiService.checkout(checkoutRequest)
    }

    override suspend fun updateItemInCart(idItem: Int, quantity: Int): Response<MessageResponse> {

        return appFoodApiService.updateItemInCart(idItem, UpdateNumItemInCartRequest(quantity))
    }

    // Order
    override suspend fun getAllOrder(): Response<OrdersListResponse> {
        return appFoodApiService.getAllOrder()
    }

    override suspend fun getAllItemInOrder(idOrder: Int): Response<OrderDetailResponse> {
        return appFoodApiService.getAllItemInOrder(idOrder)
    }

    override suspend fun chart(): Response<ChartDataResponse> {
        return appFoodApiService.chart()
    }

    override suspend fun cancelOrder(idOrder: Int): Response<MessageResponse> {
        return appFoodApiService.cancelOrder(idOrder)
    }
}