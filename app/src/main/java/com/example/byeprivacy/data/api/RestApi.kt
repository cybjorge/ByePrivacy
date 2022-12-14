package com.example.byeprivacy.data.api

import android.content.Context
import com.example.byeprivacy.data.api.helpers.AuthInterceptor
import com.example.byeprivacy.data.api.helpers.TokenAuth
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*



interface RestApi {
    @POST("user/refresh.php")
    fun userRefresh(@Body user: UserRefreshRequest) : Call<UserResponse>

    @POST("user/create.php")
    suspend fun userCreate(@Body userCreateRequest: UserCreateRequest): Response<UserResponse>

    @POST("user/login.php")
    suspend fun userLogin(@Body userLoginRequest: UserLoginRequest): Response<UserResponse>

    @GET("bar/list.php")
    @Headers("mobv-auth: accept")
    suspend fun barList(): Response<List<barResponseList>>

    @GET("https://overpass-api.de/api/interpreter?")
    suspend fun barDetail(@Query("data") data : String): Response<BarDetailResponse>

    @GET("https://overpass-api.de/api/interpreter?")
    suspend fun barsInRadius(@Query("data") data: String): Response<BarDetailResponse>

    @POST("bar/message.php")
    @Headers("mobv-auth: accept")
    suspend fun barMessage(@Body bar: barRequest) : Response<Any>

    @GET("contact/list.php")
    @Headers("mobv-auth: accept")
    suspend fun friendList(): Response<List<FriendResponse>>

    @GET("contact/friends.php")
    @Headers("mobv-auth: accept")
    suspend fun friendAddedByMeList(): Response<List<FriendResponse>>

    @POST("contact/message.php")
    @Headers("mobv-auth: accept")
    suspend fun addFriend(@Body contact: friendRequest): Response<Any>

    @POST("contact/delete.php")
    suspend fun removeFriend(@Body contact: friendRequest): Response<Any>


    companion object{
        const val BASE_URL = "https://zadanie.mpage.sk/"

        fun create(context: Context): RestApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .authenticator(TokenAuth(context))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(RestApi::class.java)
        }
    }
}