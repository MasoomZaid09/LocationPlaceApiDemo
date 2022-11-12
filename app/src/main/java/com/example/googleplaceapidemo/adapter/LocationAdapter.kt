package com.example.googleplaceapidemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.googleplaceapidemo.R
import com.example.googleplaceapidemo.ui.Clicklistener
import com.kanabix.api.LocationPrediction

class LocationAdapter(var context: Context, var data : ArrayList<LocationPrediction>,var click :Clicklistener): RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.location_model_layout,null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val locationData = data[position]

        holder.locationName.text = locationData.description
        holder.clickLayout.setOnClickListener {
            click.clickLocation(holder.locationName.text.toString())
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        var locationName = view.findViewById<TextView>(R.id.locationName)
        var clickLayout = view.findViewById<ConstraintLayout>(R.id.clickLayout)
    }
}