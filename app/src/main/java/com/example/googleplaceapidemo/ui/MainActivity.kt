package com.example.googleplaceapidemo.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.googleplaceapidemo.adapter.LocationAdapter
import com.example.googleplaceapidemo.databinding.ActivityMainBinding
import com.example.googleplaceapidemo.utils.SavedPrefManager
import com.example.googleplaceapidemo.viewModels.GoogleLocationApiViewModel
import com.kanabix.api.LocationPrediction
import com.kanabix.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),Clicklistener {

    lateinit var binding : ActivityMainBinding

    lateinit var adapter: LocationAdapter
    var data: ArrayList<LocationPrediction> =ArrayList()


    var latitute = 0.0
    var logitute = 0.0

    private val viewModel : GoogleLocationApiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ObserveResponce()
        ObserveLatLngResponse()

        handleSearchBar()

    }

    private fun ObserveResponce() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._LocationStateFlow.collect { response ->

                    when (response) {
                        is Resource.Success -> {

                            try {
                                data = response.data!!.predictions
                                setAdapter(data)
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {

                        }

                        is Resource.Loading -> {

                        }

                        is Resource.Empty -> {
                                                   }
                    }
                }
            }
        }
    }

    private fun ObserveLatLngResponse() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel._latLngStateFLow.collect { response ->

                    when (response) {
                        is Resource.Success -> {

                            try {
                                Toast.makeText(this@MainActivity, "latitude :  ${response.data?.results?.get(0)?.geometry?.location?.lat.toString()}", Toast.LENGTH_SHORT).show()
                                Toast.makeText(this@MainActivity, "logitute :  ${response.data?.results?.get(0)?.geometry?.location?.lng.toString()}", Toast.LENGTH_SHORT).show()

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {

                        }

                        is Resource.Loading -> {

                        }

                        is Resource.Empty -> {

                        }
                    }
                }
            }
        }
    }

    fun handleSearchBar(){

        binding.search.addTextChangedListener(
            object  : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(string: Editable?) {
                    if (!string.toString().equals("")){

                        viewModel.getLocationApi(Uri.encode(string.toString()),"AIzaSyBlDosAmGJwMLJfIqFAIGkVN7w4TEdYAGg")

                    }else{

                        viewModel.getLocationApi(Uri.encode(string.toString()),"AIzaSyBlDosAmGJwMLJfIqFAIGkVN7w4TEdYAGg")
                    }
                }
            }
        )
    }


    private fun setAdapter(data : ArrayList<LocationPrediction>){
        adapter = LocationAdapter(this,data,this)
        binding.recyclerView.adapter = adapter
    }

    override fun clickLocation(name: String) {
        SavedPrefManager.saveStringPreferences(this,SavedPrefManager.location,name)
        viewModel.getLatLng(name,"AIzaSyBlDosAmGJwMLJfIqFAIGkVN7w4TEdYAGg")
    }
}