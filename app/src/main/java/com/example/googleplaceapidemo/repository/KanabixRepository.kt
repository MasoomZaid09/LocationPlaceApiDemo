package com.example.googleplaceapidemo.repository

import com.example.googleplaceapidemo.api.Api_Interface_Impl
import com.kanabix.api.LocationLatLngResponse
import com.kanabix.api.LocationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class KanabixRepository @Inject constructor(private val apiServiceImpl: Api_Interface_Impl) {

    fun getLocationApi(text:String,key:String): Flow<Response<LocationResponse>> = flow {
        emit(apiServiceImpl.getLocationApi(text, key))
    }.flowOn(Dispatchers.IO)


    fun getLatLng(text:String,key:String): Flow<Response<LocationLatLngResponse>> = flow {
        emit(apiServiceImpl.getLatLng(text, key))
    }.flowOn(Dispatchers.IO)


}