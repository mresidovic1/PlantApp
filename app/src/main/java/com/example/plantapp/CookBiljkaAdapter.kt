package com.example.plantapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CookBiljkaAdapter(private var biljke : List<Biljka>) : RecyclerView.Adapter<CookBiljkaAdapter.CookViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cooking_item,parent,false)
        return CookViewHolder(view)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: CookViewHolder, position: Int) {
        val trenutniItem = biljke[position]
        val context : Context = holder.slikaItem.context
        val id : Int = context.resources.getIdentifier("plant1","drawable",context.packageName)
        holder.slikaItem.setImageResource(id)
        holder.profilOkusaItem.text = trenutniItem.profilOkusa.opis
        holder.nazivItem.text = trenutniItem.naziv
        holder.jelo1Item.text = trenutniItem.jela[0].toString()
        val visak : String
        visak = "visak"
        holder.jelo3Item.text = visak
    }

    class CookViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val slikaItem : ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem : TextView = itemView.findViewById(R.id.nazivItem)
        val profilOkusaItem : TextView = itemView.findViewById(R.id.profilOkusaItem)
        val jelo1Item : TextView = itemView.findViewById(R.id.jelo1Item)
        val jelo2Item : TextView = itemView.findViewById(R.id.jelo2Item)
        val jelo3Item : TextView = itemView.findViewById(R.id.jelo3Item)
    }
}
