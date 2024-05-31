package com.example.plantapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var biljkeRV : RecyclerView
    private lateinit var biljkeLista : ArrayList<Biljka>
    private lateinit var MedbiljkeRvAdapter : MedBiljkaAdapter
    private lateinit var CookbiljkeRvAdapter : CookBiljkaAdapter
    private lateinit var BotanicbiljkeRvAdapter : BotanicBiljkaAdapter
    private lateinit var originalnaLista : ArrayList<Biljka>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        TrefleDAO.initialize(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        originalnaLista = ArrayList(biljke)
        biljkeLista = ArrayList()
        biljkeLista = ArrayList(biljke)
        setContentView(R.layout.activity_main)
        biljkeRV=findViewById(R.id.biljkeRV)
        biljkeRV.layoutManager = LinearLayoutManager(this)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycler_view_item_spacing)
        val itemDecoration = SpacesItemDecoration(spacingInPixels)
        biljkeRV.addItemDecoration(itemDecoration)
        val pretragaET = findViewById<EditText>(R.id.pretragaET)
        val brzaPretragaBtn = findViewById<Button>(R.id.brzaPretraga)
        val botanicSearchLayout = findViewById<LinearLayout>(R.id.layoutPretraga)
        val bojaSpinner : Spinner = findViewById(R.id.bojaSPIN)
        ArrayAdapter.createFromResource(this,R.array.boje,
            android.R.layout.simple_spinner_item).also{
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                bojaSpinner.adapter = adapter
        }
        bojaSpinner.setSelection(0)

        val modSpinner : Spinner = findViewById(R.id.modSpinner)
        ArrayAdapter.createFromResource(this,R.array.mods,
            android.R.layout.simple_spinner_item).also{
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                modSpinner.adapter = adapter
        }
        modSpinner.setSelection(0)
        val rstBtn : Button = findViewById(R.id.resetBtn)
        val novaBiljkaBtn : Button = findViewById(R.id.novaBiljkaBtn)
        novaBiljkaBtn.setOnClickListener {
            val newAct = Intent(this,NovaBiljkaActivity::class.java)
            startActivity(newAct)
        }

        modSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var srodneBiljke = mutableListOf<Biljka>()
            var rst : Boolean = false
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        botanicSearchLayout.visibility = View.GONE
                        val adapter : MedBiljkaAdapter
                        if(srodneBiljke.size!=0 && !rst) adapter = MedBiljkaAdapter(srodneBiljke)
                        else adapter = MedBiljkaAdapter(biljkeLista)
                        MedbiljkeRvAdapter = adapter
                        biljkeRV.adapter = MedbiljkeRvAdapter
                        adapter.setOnItemClickListener(object : MedBiljkaAdapter.OnItemClickListener{
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onItemClick(position: Int) {
                                srodneBiljke.clear()
                                rst=false
                                if (position >= 0 && position < biljkeLista.size) {
                                    val izabranaBiljka = biljkeLista[position]
                                    for(biljka in biljkeLista){
                                        if(biljka.medicinskeKoristi.toSet().intersect(izabranaBiljka.medicinskeKoristi.toSet()).isNotEmpty()){
                                            srodneBiljke.add(biljka)
                                        }
                                    }
                                    MedbiljkeRvAdapter.updateList(srodneBiljke)
                                }
                            }
                        })
                        rstBtn.setOnClickListener {
                            MedbiljkeRvAdapter.updateList(originalnaLista)
                            rst=true
                        }
                    }
                    1 -> {
                        botanicSearchLayout.visibility = View.GONE
                        val adapter : CookBiljkaAdapter
                        if(srodneBiljke.size!=0 && !rst) adapter = CookBiljkaAdapter(srodneBiljke)
                        else adapter = CookBiljkaAdapter(biljkeLista)
                        CookbiljkeRvAdapter = adapter
                        biljkeRV.adapter = CookbiljkeRvAdapter
                        adapter.setOnItemClickListener(object : CookBiljkaAdapter.OnItemClickListener{
                            @SuppressLint("NotifyDataSetChanged")
                            override fun OnItemClick(position: Int) {
                                srodneBiljke.clear()
                                rst=false
                                if (position >= 0 && position < biljkeLista.size) {
                                    val izabranaBiljka = biljkeLista[position]
                                    for(biljka in biljkeLista){
                                        if(biljka.jela.toSet().intersect(izabranaBiljka.jela.toSet()).isNotEmpty() || biljka.profilOkusa == izabranaBiljka.profilOkusa){
                                            srodneBiljke.add(biljka)
                                        }
                                    }
                                    CookbiljkeRvAdapter.updateList(srodneBiljke)
                                }
                            }
                        })
                        rstBtn.setOnClickListener {
                            CookbiljkeRvAdapter.updateList(originalnaLista)
                            rst=true
                        }
                    }
                    2 -> {
                        botanicSearchLayout.visibility = View.VISIBLE
                        val adapter : BotanicBiljkaAdapter
                        if(srodneBiljke.size!=0 && !rst)
                        adapter = BotanicBiljkaAdapter(srodneBiljke)
                        else adapter = BotanicBiljkaAdapter(biljkeLista)
                        BotanicbiljkeRvAdapter = adapter
                        biljkeRV.adapter = BotanicbiljkeRvAdapter
                        adapter.setOnItemClickListener(object : BotanicBiljkaAdapter.OnItemClickListener{
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onItemClick(position: Int) {
                                srodneBiljke.clear()
                                rst=false
                                if (position >= 0 && position < biljkeLista.size) {
                                    val izabranaBiljka = biljkeLista[position]
                                    for(biljka in biljkeLista){
                                        if(biljka.zemljisniTipovi.toSet().intersect(izabranaBiljka.zemljisniTipovi.toSet()).isNotEmpty() && biljka.klimatskiTipovi.toSet().intersect(izabranaBiljka.klimatskiTipovi.toSet()).isNotEmpty()){
                                            srodneBiljke.add(biljka)
                                        }
                                    }
                                    BotanicbiljkeRvAdapter.updateList(srodneBiljke)
                                }
                            }
                        })
                        rstBtn.setOnClickListener {
                            BotanicbiljkeRvAdapter.updateList(originalnaLista)
                            rst=true
                        }
                        brzaPretragaBtn.setOnClickListener {
                            val query = pretragaET.text.toString()
                            val selectedColor = bojaSpinner.selectedItem.toString()
                            if(query.isNotEmpty()){
                                GlobalScope.launch(Dispatchers.Main) {
                                    var results : List<Biljka> = emptyList()
                                    if(query!="") {
                                        results = TrefleDAO().getPlantsWithFlowerColor(
                                            selectedColor,
                                            query
                                        )
                                    }
                                    if(results.isNotEmpty()){
                                        BotanicbiljkeRvAdapter.updateList(results)
                                    }
                                    else{
                                        Snackbar.make(brzaPretragaBtn, "Nema biljaka sa trazenim specifikacijama.", Snackbar.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                botanicSearchLayout.visibility = View.GONE
                MedbiljkeRvAdapter = MedBiljkaAdapter(biljkeLista)
                biljkeRV.adapter = MedbiljkeRvAdapter
            }
        }

        NovaBiljkaSingleton.novaBiljkaLiveData.observe(this) { novaBiljka ->
            biljkeLista.add(novaBiljka)
            originalnaLista.add(novaBiljka)
            MedbiljkeRvAdapter.notifyDataSetChanged()
        }
    }
}
