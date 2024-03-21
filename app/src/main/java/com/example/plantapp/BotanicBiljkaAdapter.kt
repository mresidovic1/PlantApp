package com.example.plantapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BotanicBiljkaAdapter(private var biljke : List<Biljka>) : RecyclerView.Adapter<BotanicBiljkaAdapter.BotanicViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotanicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.botanical_item,parent,false)
        return BotanicViewHolder(view)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: BotanicViewHolder, position: Int) {
        val trenutniItem = biljke[position]
        val context : Context = holder.slikaItem.context
        val id : Int = context.resources.getIdentifier("plant1","drawable",context.packageName)
        holder.slikaItem.setImageResource(id)
        holder.porodicaItem.text = trenutniItem.porodica
        holder.nazivItem.text = trenutniItem.naziv
        holder.klimatskiTipItem.text = trenutniItem.klimatskiTipovi[0].opis
    }

    class BotanicViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val slikaItem : ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem : TextView = itemView.findViewById(R.id.nazivItem)
        val porodicaItem : TextView = itemView.findViewById(R.id.porodicaItem)
        val zemljisniTipItem : TextView = itemView.findViewById(R.id.zemljisniTipItem)
        val klimatskiTipItem : TextView = itemView.findViewById(R.id.klimatskiTipItem)
    }
}
