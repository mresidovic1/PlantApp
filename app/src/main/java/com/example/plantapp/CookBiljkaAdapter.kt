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

class CookBiljkaAdapter(private var biljke : List<Biljka>) : RecyclerView.Adapter<CookBiljkaAdapter.CookViewHolder>(){
    private lateinit var cookListener : OnItemClickListener
    interface OnItemClickListener{
        fun OnItemClick(position: Int)
    }
    fun setOnItemClickListener(listener:OnItemClickListener){
        cookListener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cooking_item,parent,false)
        return CookViewHolder(view,cookListener)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: CookViewHolder, position: Int) {
        val trenutniItem = biljke[position]
        val context : Context = holder.slikaItem.context
        val id : Int = context.resources.getIdentifier("plant1","drawable",context.packageName)
        holder.slikaItem.setImageResource(id)
        holder.profilOkusaItem.text = trenutniItem.profilOkusa?.opis ?: ""
        holder.nazivItem.text = trenutniItem.naziv
        holder.jelo1Item.text = trenutniItem.jela.getOrNull(0)
        holder.jelo2Item.text = trenutniItem.jela.getOrNull(1)
        holder.jelo3Item.text = trenutniItem.jela.getOrNull(2)
        CoroutineScope(Dispatchers.Main).launch {
            val imageBitmap = TrefleDAO().getImage(trenutniItem)
            holder.slikaItem.setImageBitmap(imageBitmap)
        }
    }

    class CookViewHolder (itemView : View,listener:OnItemClickListener) : RecyclerView.ViewHolder(itemView){
        val slikaItem : ImageView = itemView.findViewById(R.id.slikaItem)
        val nazivItem : TextView = itemView.findViewById(R.id.nazivItem)
        val profilOkusaItem : TextView = itemView.findViewById(R.id.profilOkusaItem)
        val jelo1Item : TextView = itemView.findViewById(R.id.jelo1Item)
        val jelo2Item : TextView = itemView.findViewById(R.id.jelo2Item)
        val jelo3Item : TextView = itemView.findViewById(R.id.jelo3Item)
        init{
            itemView.setOnClickListener {
                listener.OnItemClick(adapterPosition)
            }
        }
    }
    fun updateList(srodnaLista : List<Biljka>){
        biljke=srodnaLista
        notifyDataSetChanged()
    }
}
