package com.trungdz.appfood.data.repository

import android.util.Log
import com.google.gson.GsonBuilder
import com.trungdz.appfood.data.model.ItemDetail
import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.model.modelrequest.CheckoutRequest
import com.trungdz.appfood.data.model.modelrequest.CreateReviewRequest
import com.trungdz.appfood.data.model.modelrequest.LoginRequest
import com.trungdz.appfood.data.model.modelresponse.*
import com.trungdz.appfood.data.repository.datasource.IAppFoodRemoteDatasource
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.domain.repository.IRepository
import retrofit2.Response
import javax.inject.Inject


class IRepositoryImp @Inject constructor(private val iAppFoodRemoteDatasource: IAppFoodRemoteDatasource) :
    IRepository {

    // Account
    override suspend fun loginUser(infoLogin: LoginRequest): Resource<LoginResponse> {
        val response = iAppFoodRemoteDatasource.loginUser(infoLogin)

        return responseToLoginResult(response)
    }

    private fun responseToLoginResult(response: Response<LoginResponse>): Resource<LoginResponse> {
        if (response.isSuccessful) {
            response.body()?.let { return Resource.Success(it) }
        }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    // Types
    override suspend fun getAllTypes(): Resource<ListTypesResponse> {
        val response = iAppFoodRemoteDatasource.getAllTypes()
        return responseToListType(response)
    }

    private fun responseToListType(response: Response<ListTypesResponse>): Resource<ListTypesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    // ItemInOrder
    override suspend fun getAllItem(page: Int): Resource<ListItemsResponse> {
        val response = iAppFoodRemoteDatasource.getAllItem(page)

        return responseToListItems(response)
    }

    private fun responseToListItems(response: Response<ListItemsResponse>): Resource<ListItemsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { return Resource.Success(it) }
        }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun getAllItemByIdType(page: Int, idType: Int): Resource<ListItemsResponse> {
        val response = iAppFoodRemoteDatasource.getAllItemByIdType(page, idType)

        return responseToListItemsById(response)
    }


    private fun responseToListItemsById(response: Response<ListItemsResponse>): Resource<ListItemsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { return Resource.Success(it) }
        }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun getAllItemByName(
        page: Int,
        name: String?,
        typeSort: Int?,
    ): Resource<ListItemsResponse> {
        val response =
            iAppFoodRemoteDatasource.getAllItemByName(page = page, name = name, typeSort = typeSort)
        return responseToGetAllItemByName(response)
    }

    private fun responseToGetAllItemByName(response: Response<ListItemsResponse>): Resource<ListItemsResponse> {
        if (response.isSuccessful)
            response.body()?.let {
                return Resource.Success(it)
            }
        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun getDetailItem(idItem: Int): Resource<ItemDetailResponse> {
        val response = iAppFoodRemoteDatasource.getDetailItem(idItem)

        return responseToDetailItem(response)
    }

    private fun responseToDetailItem(response: Response<ItemDetailResponse>): Resource<ItemDetailResponse> {
        if (response.isSuccessful) {

            response.body()?.let { return Resource.Success(it) }
        }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    // Review
    override suspend fun getAllReviewByItem(idItem: Int): Resource<ListReviewsResponse> {
        val response = iAppFoodRemoteDatasource.getAllReviewByItem(idItem)

        return responseToListReviews(response)
    }

    private fun responseToListReviews(response: Response<ListReviewsResponse>): Resource<ListReviewsResponse> {
        if (response.isSuccessful)
            response.body()?.let { return Resource.Success(it) }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun createReview(
        idItem: Int, idOrder: Int, reviewRequest: CreateReviewRequest,
    ): Resource<MessageResponse> {
        val response = iAppFoodRemoteDatasource.createReview(idItem, idOrder, reviewRequest)
        return responseToCreateReview(response)
    }

    private fun responseToCreateReview(response: Response<MessageResponse>): Resource<MessageResponse> {
        if (response.isSuccessful)
            response.body()?.let { return Resource.Success(it) }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    // Wishlist
    override suspend fun getAllItemInWishlist(): Resource<WishlistResponse> {
        val response = iAppFoodRemoteDatasource.getAllItemInWishlist()
        return responseToWishlist(response)
    }

    private fun responseToWishlist(response: Response<WishlistResponse>): Resource<WishlistResponse> {
        if (response.isSuccessful)
            response.body()?.let { return Resource.Success(it) }
        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun updateItemInWishlist(idItem: Int): Resource<MessageResponse> {
        val response = iAppFoodRemoteDatasource.updateItemInWishlist(idItem)
        return responseToUpdateItemInWishList(response)
    }

    private fun responseToUpdateItemInWishList(response: Response<MessageResponse>): Resource<MessageResponse> {
        if (response.isSuccessful)
            response.body()?.let { return Resource.Success(it) }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    // Cart

    override suspend fun createItemInCart(idItem: Int, quantity: Int): Resource<MessageResponse> {
        val response = iAppFoodRemoteDatasource.createItemInCart(idItem, quantity)

        return responseToCreateItemInCart(response)
    }

    private fun responseToCreateItemInCart(response: Response<MessageResponse>): Resource<MessageResponse> {
        if (response.isSuccessful) {
            response.body()?.let { return Resource.Success(it) }
        }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun getAllItemInCart(): Resource<ItemListInCartResponse> {
        val response = iAppFoodRemoteDatasource.getAllItemInCart()

        return responseToGetAllItemInCart(response)
    }

    private fun responseToGetAllItemInCart(response: Response<ItemListInCartResponse>): Resource<ItemListInCartResponse> {
        if (response.isSuccessful)
            response.body()?.let { return Resource.Success(it) }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun deleteOneItemInCart(idItem: Int): Resource<MessageResponse> {
        val response = iAppFoodRemoteDatasource.deleteOneItemInCart(idItem)

        return responseToDeleteOneItemInCart(response)
    }

    private fun responseToDeleteOneItemInCart(response: Response<MessageResponse>): Resource<MessageResponse> {
        if (response.isSuccessful)
            response.body()?.let { return Resource.Success(it) }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun increaseNumItemInCart(idItem: Int): Resource<MessageResponse> {
        val response = iAppFoodRemoteDatasource.increaseNumItemInCart(idItem)

        return responseToIncreaseNumItemInCart(response)
    }

    private fun responseToIncreaseNumItemInCart(response: Response<MessageResponse>): Resource<MessageResponse> {
        if (response.isSuccessful)
            response.body()?.let { return Resource.Success(it) }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun decreaseNumItemInCart(idItem: Int): Resource<MessageResponse> {
        val response = iAppFoodRemoteDatasource.decreaseNumItemInCart(idItem)

        return responseToDecreaseNumItemInCart(response)
    }

    private fun responseToDecreaseNumItemInCart(response: Response<MessageResponse>): Resource<MessageResponse> {
        if (response.isSuccessful)
            response.body()?.let { return Resource.Success(it) }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun checkout(checkoutRequest: CheckoutRequest): Resource<MessageResponse> {
        val response = iAppFoodRemoteDatasource.checkout(checkoutRequest)
        return responseToCheckout(response)
    }

    private fun responseToCheckout(response: Response<MessageResponse>): Resource<MessageResponse> {
        if (response.isSuccessful)
            response.body()?.let { return Resource.Success(it) }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun updateItemInCart(idItem: Int, quantity: Int): Resource<MessageResponse> {

        val response = iAppFoodRemoteDatasource.updateItemInCart(idItem, quantity)

        return responseToUpdateItemInCart(response)
    }

    private fun responseToUpdateItemInCart(response: Response<MessageResponse>): Resource<MessageResponse> {
        if (response.isSuccessful)
            response.body()?.let { return Resource.Success(it) }


        Log.d("UpdateItem", "${response.errorBody()?.string()}")
        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    // Order
    override suspend fun getAllOrder(): Resource<OrdersListResponse> {
        val response = iAppFoodRemoteDatasource.getAllOrder()
        return responseToGetAllOrder(response)
    }

    private fun responseToGetAllOrder(response: Response<OrdersListResponse>): Resource<OrdersListResponse> {
        if (response.isSuccessful)
            response.body()?.let {
                return Resource.Success(it)
            }
        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun getAllItemInOrder(idOrder: Int): Resource<OrderDetailResponse> {
        val response = iAppFoodRemoteDatasource.getAllItemInOrder(idOrder)
        return responseToGetAllItemInOrder(response)
    }

    private fun responseToGetAllItemInOrder(response: Response<OrderDetailResponse>): Resource<OrderDetailResponse> {
        if (response.isSuccessful)
            response.body()?.let {
                return Resource.Success(it)
            }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    override suspend fun chart(): Resource<ChartDataResponse> {
        val response = iAppFoodRemoteDatasource.chart()
        return responseToChart(response)
    }

    private fun responseToChart(response: Response<ChartDataResponse>): Resource<ChartDataResponse> {
        if (response.isSuccessful) {
            response.body()?.let { return Resource.Success(it) }
        }

        return Resource.Error(message = "${errorMessage(response.errorBody()?.string())}")
    }

    // util
    private fun errorMessage(message: String?): String {
        val gson = GsonBuilder().create()
        val messageResponse = gson.fromJson(message, MessageResponse::class.java)

        return messageResponse.message
    }
}