package com.example.plantapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get

class NovaBiljkaActivity : AppCompatActivity() {
    private lateinit var medKoristLV : ListView
    private lateinit var klimatskiTipLV : ListView
    private lateinit var zemljisniTipLV : ListView
    private lateinit var jelaLV : ListView
    private lateinit var profilOkusaLV : ListView
    private lateinit var dodajJeloBtn : Button
    private lateinit var dodajBiljkuBtn : Button
    private lateinit var naziv : EditText
    private lateinit var uslikajBiljkuBtn : Button
    private lateinit var biljkaImage : ImageView
    val REQUEST_IMAGE_CAPTURE = 1
    private var pozicija : Int = 0
    private var izmjenaJela = false
    private lateinit var porodica : EditText
    private lateinit var medUpozorenje : EditText
    private lateinit var jelo : EditText
    private lateinit var jelaAdapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nova_biljka)
        medKoristLV = findViewById(R.id.medicinskaKoristLV)
        klimatskiTipLV = findViewById(R.id.klimatskiTipLV)
        zemljisniTipLV = findViewById(R.id.zemljisniTipLV)
        jelaLV=findViewById(R.id.jelaLV)
        biljkaImage = findViewById(R.id.slikaIV)
        dodajJeloBtn=findViewById(R.id.dodajJeloBtn)
        dodajBiljkuBtn=findViewById(R.id.dodajBiljkuBtn)
        uslikajBiljkuBtn=findViewById(R.id.uslikajBiljkuBtn)
        profilOkusaLV=findViewById(R.id.profilOkusaLV)
        naziv=findViewById(R.id.nazivET)
        porodica=findViewById(R.id.porodicaET)
        medUpozorenje=findViewById(R.id.medicinskoUpozorenjeET)
        jelo=findViewById(R.id.jeloET)
        val medKoristi = MedicinskaKorist.entries.toTypedArray()
        val klimatskiTipovi = KlimatskiTip.entries.toTypedArray()
        val zemljisniTipovi = Zemljiste.entries.toTypedArray()
        val profiliOkusa = ProfilOkusaBiljke.entries.toTypedArray()
        val medArrayAdapter : ArrayAdapter<MedicinskaKorist> = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice,medKoristi)
        val klimatskiTipoviArrayAdapter : ArrayAdapter<KlimatskiTip> = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice,klimatskiTipovi)
        val zemljisniTipoviArrayAdapter : ArrayAdapter<Zemljiste> = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice,zemljisniTipovi)
        val profilOkusaAdapter : ArrayAdapter<ProfilOkusaBiljke> = ArrayAdapter(this, android.R.layout.simple_list_item_1,profiliOkusa)
        medKoristLV.adapter=medArrayAdapter
        klimatskiTipLV.adapter=klimatskiTipoviArrayAdapter
        zemljisniTipLV.adapter=zemljisniTipoviArrayAdapter
        profilOkusaLV.adapter=profilOkusaAdapter
        medKoristLV.choiceMode=ListView.CHOICE_MODE_MULTIPLE
        klimatskiTipLV.choiceMode=ListView.CHOICE_MODE_MULTIPLE
        zemljisniTipLV.choiceMode=ListView.CHOICE_MODE_MULTIPLE
        profilOkusaLV.choiceMode=ListView.CHOICE_MODE_SINGLE
        val jela: ArrayList<String> = ArrayList()
        jelaAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, jela)
        jelaLV.adapter = jelaAdapter
        dodajJeloBtn.setOnClickListener {
            val novoJelo = jelo.text.toString()
            if (novoJelo.isNotEmpty()) {
                if (izmjenaJela) {
                    jela[pozicija] = novoJelo
                    jelaAdapter.notifyDataSetChanged()
                    jelo.setText("")
                    dodajJeloBtn.text = "Dodaj jelo"
                    izmjenaJela = false
                } else {
                    jela.add(novoJelo)
                    jelaAdapter.notifyDataSetChanged()
                    jelo.setText("")
                }
            }
        }
        jelaLV.setOnItemClickListener { parent, view, position, id ->
            pozicija=position
            val selectedItem = jelaAdapter.getItem(position)
            if (selectedItem != null) {
                jelo.setText(selectedItem.toString())
                dodajJeloBtn.text = "Izmijeni jelo"
                izmjenaJela = true
            }
        }
        uslikajBiljkuBtn.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            biljkaImage.setImageBitmap(imageBitmap)
        }
    }
}
