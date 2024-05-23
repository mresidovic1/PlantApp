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

class MedBiljkaAdapter(private var biljke : List<Biljka>) : RecyclerView.Adapter<MedBiljkaAdapter.BiljkaViewHolder>(){

    private lateinit var medListener : OnItemClickListener
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener:OnItemClickListener){
        medListener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiljkaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medical_item,parent,false)
        return BiljkaViewHolder(view,medListener)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: BiljkaViewHolder, position: Int) {
        val trenutniItem = biljke[position]
        val context : Context = holder.slikaItem.context
        val id : Int = context.resources.getIdentifier("plant1","drawable",context.packageName)
        holder.slikaItem.setImageResource(id)
        holder.upozorenjeItem.text = trenutniItem.medicinskoUpozorenje
        holder.nazivItem.text = trenutniItem.naziv
        holder.korist1Item.text = trenutniItem.medicinskeKoristi.getOrNull(0)?.opis
        holder.korist2Item.text = trenutniItem.medicinskeKoristi.getOrNull(1)?.opis
        holder.korist3Item.text = trenutniItem.medicinskeKoristi.getOrNull(2)?.opis
        CoroutineScope(Dispatchers.Main).launch {
            val imageBitmap = TrefleDAO().getImage(trenutniItem)
            holder.slikaItem.setImageBitmap(imageBitmap)
        }
    }

    class BiljkaViewHolder (itemView : View, listener:OnItemClickListener) : RecyclerView.ViewHolder(itemView){
        val slikaItem : ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem : TextView = itemView.findViewById(R.id.nazivItem)
        val upozorenjeItem : TextView = itemView.findViewById(R.id.upozorenjeItem)
        val korist1Item : TextView = itemView.findViewById(R.id.korist1Item)
        val korist2Item : TextView = itemView.findViewById(R.id.korist2Item)
        val korist3Item : TextView = itemView.findViewById(R.id.korist3Item)

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
