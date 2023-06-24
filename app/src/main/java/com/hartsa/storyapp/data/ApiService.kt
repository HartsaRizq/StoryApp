package com.hartsa.storyapp.data

import com.hartsa.storyapp.data.response.AddStoryResponse
import com.hartsa.storyapp.data.response.DetailStoryResponse
import com.hartsa.storyapp.data.response.GetStoryResponse
import com.hartsa.storyapp.data.response.LoginResponse
import com.hartsa.storyapp.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("/v1/register")
    fun registerAcc(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("/v1/login")
    fun loginAcc(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("/v1/stories")
    fun newStory(
        @Header("Authorization") bearer: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody?,
    ): Call<AddStoryResponse>

    @GET("/v1/stories")
    fun getAllStory(
        @Header("Authorization") bearer: String,
    ): Call<GetStoryResponse>

    @GET("/v1/stories/{id}")
    fun getDetailStory(
        @Header("Authorization") bearer: String,
        @Path("id") id: String
    ): Call<DetailStoryResponse>
}