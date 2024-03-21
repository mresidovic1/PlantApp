package com.example.plantapp

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var biljkeRV : RecyclerView
    private lateinit var biljkeLista : ArrayList<Biljka>
    private lateinit var MedbiljkeRvAdapter : MedBiljkaAdapter
    private lateinit var CookbiljkeRvAdapter : CookBiljkaAdapter
    private lateinit var BotanicbiljkeRvAdapter : BotanicBiljkaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        biljkeLista = ArrayList()
        biljkeLista = ArrayList(biljke)
        setContentView(R.layout.activity_main)
        biljkeRV=findViewById(R.id.biljkeRV)
        biljkeRV.layoutManager = LinearLayoutManager(this)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycler_view_item_spacing)
        val itemDecoration = SpacesItemDecoration(spacingInPixels)
        biljkeRV.addItemDecoration(itemDecoration)

        val modSpinner : Spinner = findViewById(R.id.modSpinner)
        ArrayAdapter.createFromResource(this,R.array.mods,
            android.R.layout.simple_spinner_item).also{
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                modSpinner.adapter = adapter
        }
        modSpinner.setSelection(0)
        modSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        MedbiljkeRvAdapter = MedBiljkaAdapter(biljkeLista)
                        biljkeRV.adapter = MedbiljkeRvAdapter
                    }
                    1 -> {
                        CookbiljkeRvAdapter = CookBiljkaAdapter(biljkeLista)
                        biljkeRV.adapter = CookbiljkeRvAdapter
                    }
                    2 -> {
                        BotanicbiljkeRvAdapter = BotanicBiljkaAdapter(biljkeLista)
                        biljkeRV.adapter = BotanicbiljkeRvAdapter
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                MedbiljkeRvAdapter = MedBiljkaAdapter(biljkeLista)
                biljkeRV.adapter = MedbiljkeRvAdapter
            }
        }
    }
}