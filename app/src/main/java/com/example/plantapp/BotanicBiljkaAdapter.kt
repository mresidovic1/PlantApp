package com.example.plantapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BotanicBiljkaAdapter(private var biljke : List<Biljka>) : RecyclerView.Adapter<BotanicBiljkaAdapter.BotanicViewHolder>(){
    private lateinit var botanicListener : OnItemClickListener
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        botanicListener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotanicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.botanical_item,parent,false)
        return BotanicViewHolder(view,botanicListener)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: BotanicViewHolder, position: Int) {
        val trenutniItem = biljke[position]
        val context : Context = holder.slikaItem.context
        val id : Int = context.resources.getIdentifier("plant1","drawable",context.packageName)
        holder.slikaItem.setImageResource(id)
        holder.porodicaItem.text = trenutniItem.porodica
        holder.nazivItem.text = trenutniItem.naziv
        holder.klimatskiTipItem.text = trenutniItem.klimatskiTipovi.getOrNull(0)?.opis
        holder.zemljisniTipItem.text = trenutniItem.zemljisniTipovi.getOrNull(0)?.naziv
        CoroutineScope(Dispatchers.Main).launch {
            val imageBitmap = TrefleDAO().getImage(trenutniItem)
            holder.slikaItem.setImageBitmap(imageBitmap)
        }
    }

    class BotanicViewHolder (itemView : View,listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView){
        val slikaItem : ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem : TextView = itemView.findViewById(R.id.nazivItem)
        val porodicaItem : TextView = itemView.findViewById(R.id.porodicaItem)
        val zemljisniTipItem : TextView = itemView.findViewById(R.id.zemljisniTipItem)
        val klimatskiTipItem : TextView = itemView.findViewById(R.id.klimatskiTipItem)
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    fun updateList(srodnaLista : List<Biljka>){
        biljke=srodnaLista
        notifyDataSetChanged()
    }
}
