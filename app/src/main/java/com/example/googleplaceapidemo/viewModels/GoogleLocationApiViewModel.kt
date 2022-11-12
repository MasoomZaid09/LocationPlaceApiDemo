package com.example.googleplaceapidemo.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.googleplaceapidemo.ApplicationClass
import com.example.googleplaceapidemo.api.Constants
import com.example.googleplaceapidemo.repository.KanabixRepository
import com.google.gson.GsonBuilder
import com.kanabix.api.LocationLatLngResponse
import com.kanabix.api.LocationResponse
import com.kanabix.utils.PojoClass
import com.kanabix.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GoogleLocationApiViewModel @Inject constructor(
    app: Application,
    private val respository: KanabixRepository
) : AndroidViewModel(app) {


    private val LocationStateFlow: MutableStateFlow<Resource<LocationResponse>> =
        MutableStateFlow(Resource.Empty())
    val _LocationStateFlow = LocationStateFlow.asStateFlow()

    private val latLngStateFLow: MutableStateFlow<Resource<LocationLatLngResponse>> =
        MutableStateFlow(Resource.Empty())
    val _latLngStateFLow = latLngStateFLow.asStateFlow()

    fun getLocationApi(text:String,key:String) = viewModelScope.launch{

        LocationStateFlow.value=Resource.Loading()
        if (hasInternetConnection()){
            respository.getLocationApi(text, key)
                .catch { e->
                    LocationStateFlow.value=Resource.Error(e.message.toString())
                }.collect { data ->
                    LocationStateFlow.value = LocationHandle(data)
                }
        }else{
            LocationStateFlow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun LocationHandle(response: Response<LocationResponse>): Resource<LocationResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                Log.e("Mtlb","hsih")
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }

    fun getLatLng(text:String,key:String) = viewModelScope.launch{

        latLngStateFLow.value=Resource.Loading()
        if (hasInternetConnection()){
            respository.getLatLng(text, key)
                .catch { e->
                    latLngStateFLow.value=Resource.Error(e.message.toString())
                }.collect { data ->
                    latLngStateFLow.value = latLngHandle(data)
                }
        }else{
            latLngStateFLow.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    fun latLngHandle(response: Response<LocationLatLngResponse>): Resource<LocationLatLngResponse> {
        if (response.isSuccessful) {
            response.body()?.let { data ->
                Log.e("Mtlb","hsih")
                return Resource.Success(data)
            }
        }
        val gson = GsonBuilder().create()
        var pojo = PojoClass()

        try {
            pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error(pojo.responseMessage)
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<ApplicationClass>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}