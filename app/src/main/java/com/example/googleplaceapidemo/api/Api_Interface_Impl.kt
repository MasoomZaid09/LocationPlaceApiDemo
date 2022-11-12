package com.example.googleplaceapidemo.api


import com.kanabix.api.LocationLatLngResponse
import com.kanabix.api.LocationResponse
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class Api_Interface_Impl @Inject constructor(private val apiService: Api_Interface) {

    suspend fun getLocationApi(text :String,key:String): Response<LocationResponse> = apiService.getLocationApi(text, key)

    suspend fun getLatLng(text :String,key:String): Response<LocationLatLngResponse> = apiService.getLatLng(text, key)

}

