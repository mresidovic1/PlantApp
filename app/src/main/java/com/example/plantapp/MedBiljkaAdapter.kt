package com.example.plantapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedBiljkaAdapter(private var biljke : List<Biljka>) : RecyclerView.Adapter<MedBiljkaAdapter.BiljkaViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiljkaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medical_item,parent,false)
        return BiljkaViewHolder(view)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: BiljkaViewHolder, position: Int) {
        val trenutniItem = biljke[position]
        val context : Context = holder.slikaItem.context
        val id : Int = context.resources.getIdentifier("plant1","drawable",context.packageName)
        holder.slikaItem.setImageResource(id)
        holder.upozorenjeItem.text = trenutniItem.medicinskoUpozorenje
        holder.nazivItem.text = trenutniItem.naziv
        holder.korist1Item.text = trenutniItem.medicinskeKoristi[0].opis
        holder.korist2Item.text = trenutniItem.medicinskeKoristi[1].opis
        val visak : String
        visak = "visak"
        holder.korist3Item.text = visak
    }

    class BiljkaViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val slikaItem : ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem : TextView = itemView.findViewById(R.id.nazivItem)
        val upozorenjeItem : TextView = itemView.findViewById(R.id.upozorenjeItem)
        val korist1Item : TextView = itemView.findViewById(R.id.korist1Item)
        val korist2Item : TextView = itemView.findViewById(R.id.korist2Item)
        val korist3Item : TextView = itemView.findViewById(R.id.korist3Item)
    }
}
