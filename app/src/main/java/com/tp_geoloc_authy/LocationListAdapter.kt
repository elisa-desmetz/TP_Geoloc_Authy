package com.tp_geoloc_authy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_location.view.*

class LocationListAdapter (var locationListItems : List<LocationsModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    class LocationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(locationsModel:LocationsModel){
            itemView.textView_latitude.text = locationsModel.latitude.toString()
            itemView.textView_longitude.text = locationsModel.longitude.toString()
            itemView.textView_index.text = locationsModel.index.toString()
        }
    }

    override fun getItemCount(): Int {
        return locationListItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent,false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LocationViewHolder).bind(locationListItems[position])
    }

}