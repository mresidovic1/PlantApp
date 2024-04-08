package com.example.plantapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NovaBiljkaActivity : AppCompatActivity() {
    private lateinit var medKoristLV : ListView
    private lateinit var klimatskiTipLV : ListView
    private lateinit var zemljisniTipLV : ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nova_biljka)
        medKoristLV = findViewById(R.id.medicinskaKoristLV)
        klimatskiTipLV = findViewById(R.id.klimatskiTipLV)
        zemljisniTipLV = findViewById(R.id.zemljisniTipLV)
        val medKoristi = MedicinskaKorist.entries.toTypedArray()
        val klimatskiTipovi = KlimatskiTip.entries.toTypedArray()
        val zemljisniTipovi = Zemljiste.entries.toTypedArray()
        val medArrayAdapter : ArrayAdapter<MedicinskaKorist> = ArrayAdapter(this, android.R.layout.simple_list_item_1,medKoristi)
        val klimatskiTipoviArrayAdapter : ArrayAdapter<KlimatskiTip> = ArrayAdapter(this, android.R.layout.simple_list_item_1,klimatskiTipovi)
        val zemljisniTipoviArrayAdapter : ArrayAdapter<Zemljiste> = ArrayAdapter(this, android.R.layout.simple_list_item_1,zemljisniTipovi)
        medKoristLV.adapter=medArrayAdapter
        klimatskiTipLV.adapter=klimatskiTipoviArrayAdapter
        zemljisniTipLV.adapter=zemljisniTipoviArrayAdapter
    }
}