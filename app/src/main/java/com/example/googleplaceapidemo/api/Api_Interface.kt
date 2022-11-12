package com.example.googleplaceapidemo.api

import com.kanabix.api.LocationLatLngResponse
import com.kanabix.api.LocationResponse
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api_Interface {

//    @POST("user/signUp")
//    suspend fun CustomerSignIn(@Body request: SignUpRequest): Response<SignUpResponse>

    @GET("place/queryautocomplete/json")
    suspend fun getLocationApi(@Query("input") text:String,@Query("key") key:String) : Response<LocationResponse>

    @GET("geocode/json")
    suspend fun getLatLng(@Query("address") text:String,@Query("key") key:String) : Response<LocationLatLngResponse>

}