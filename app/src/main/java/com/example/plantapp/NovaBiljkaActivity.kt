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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        val medKoristi = MedicinskaKorist.getOpisi()
        val klimatskiTipovi = KlimatskiTip.getOpisi()
        val zemljisniTipovi = Zemljiste.getOpisi()
        val profiliOkusa = ProfilOkusaBiljke.getOpisi()
        val medArrayAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice,medKoristi)
        val klimatskiTipoviArrayAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice,klimatskiTipovi)
        val zemljisniTipoviArrayAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice,zemljisniTipovi)
        val profilOkusaAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_checked,profiliOkusa)
        medKoristLV.adapter=medArrayAdapter
        klimatskiTipLV.adapter=klimatskiTipoviArrayAdapter
        zemljisniTipLV.adapter=zemljisniTipoviArrayAdapter
        profilOkusaLV.adapter=profilOkusaAdapter
        medKoristLV.choiceMode=ListView.CHOICE_MODE_MULTIPLE
        klimatskiTipLV.choiceMode=ListView.CHOICE_MODE_MULTIPLE
        zemljisniTipLV.choiceMode=ListView.CHOICE_MODE_MULTIPLE
        profilOkusaLV.choiceMode=ListView.CHOICE_MODE_SINGLE
        val jela: ArrayList<String> = ArrayList()
        val jelaAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_checked, jela)
        jelaLV.adapter = jelaAdapter
        dodajJeloBtn.setOnClickListener {
            val novoJelo = jelo.text.toString()
            if (novoJelo.isNotEmpty()) {
                val jeloVecPostoji = jela.any { it.equals(novoJelo, ignoreCase = true) }
                if (jeloVecPostoji) jelo.setError("Jelo je već dodano u listu!")
                else {
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
                // greska
            }
        }
        dodajBiljkuBtn.setOnClickListener {
            val nazivBiljke = naziv.text.toString()
            val porodicaBiljke = porodica.text.toString()
            val medUpozorenjeBiljke = medUpozorenje.text.toString()
            var valid : Boolean = true
            if(naziv.length()<2 || naziv.length()>20){
                naziv.setError("Riječ mora sadržavati najmanje 2, ali ne više od 20 karaktera")
                valid=false
            }
            if(porodica.length()<2 || porodica.length()>20){
                porodica.setError("Riječ mora sadržavati najmanje 2, ali ne više od 20 karaktera")
                valid=false
            }
            if(medUpozorenje.length()<2 || medUpozorenje.length()>20){
                medUpozorenje.setError("Riječ mora sadržavati najmanje 2, ali ne više od 20 karaktera")
                valid=false
            }
            if((jelo.length()<2 || jelo.length()>20) && jelo.length()!=0){
                jelo.setError("Riječ mora sadržavati najmanje 2, ali ne više od 20 karaktera")
                valid=false
            }

            val selectedMedKoristiPositions = medKoristLV.checkedItemPositions
            val selectedMedKoristi = mutableListOf<MedicinskaKorist>()
            for (i in 0 until selectedMedKoristiPositions.size()) {
                if (selectedMedKoristiPositions.valueAt(i)) {
                    val selectedOpis = medKoristLV.adapter.getItem(selectedMedKoristiPositions.keyAt(i)) as String
                    val selectedEnum = MedicinskaKorist.entries.find { it.opis == selectedOpis }
                    selectedEnum?.let {
                        selectedMedKoristi.add(it)
                    }
                }
            }


            val selectedProfilOkusaPosition = profilOkusaLV.checkedItemPosition
            val profilOkusa=if (selectedProfilOkusaPosition != ListView.INVALID_POSITION) {
                val selectedOpis=profilOkusaLV.adapter.getItem(selectedProfilOkusaPosition)
                val selectedEnum=ProfilOkusaBiljke.entries.find{it.opis==selectedOpis}
                selectedEnum
            } else {
                null
            }

            val selectedJela = mutableListOf<String>()
            for (i in 0 until jelaAdapter.count) {
                val jelo = jelaAdapter.getItem(i)
                if (jelo != null) {
                    selectedJela.add(jelo)
                }
            }

            if (selectedJela.isEmpty()) {
                Snackbar.make(dodajBiljkuBtn, "Morate dodati barem jedno jelo.", Snackbar.LENGTH_SHORT).show()
                valid = false
            }

            if (selectedProfilOkusaPosition == ListView.INVALID_POSITION) {
                Snackbar.make(dodajBiljkuBtn, "Morate odabrati profil okusa.", Snackbar.LENGTH_SHORT).show()
                valid = false
            }

            if (selectedMedKoristi.isEmpty()) {
                Snackbar.make(dodajBiljkuBtn, "Mora biti odabrana barem jedna medicinska korist.", Snackbar.LENGTH_SHORT).show()
                valid = false
            }

            val selectedKlimatskiTipoviPositions = klimatskiTipLV.checkedItemPositions
            val selectedKlimatskiTipovi = mutableListOf<KlimatskiTip>()
            for (i in 0 until selectedKlimatskiTipoviPositions.size()) {
                if (selectedKlimatskiTipoviPositions.valueAt(i)) {
                    val selectedOpis = klimatskiTipLV.adapter.getItem(selectedKlimatskiTipoviPositions.keyAt(i)) as String
                    val selectedEnum = KlimatskiTip.entries.find { it.opis == selectedOpis }
                    selectedEnum?.let {
                        selectedKlimatskiTipovi.add(it)
                    }
                }
            }

            val selectedZemljisniTipoviPositions = zemljisniTipLV.checkedItemPositions
            val selectedZemljisniTipovi = mutableListOf<Zemljiste>()
            for (i in 0 until selectedZemljisniTipoviPositions.size()) {
                if (selectedZemljisniTipoviPositions.valueAt(i)) {
                    val selectedOpis = zemljisniTipLV.adapter.getItem(selectedZemljisniTipoviPositions.keyAt(i)) as String
                    val selectedEnum = Zemljiste.entries.find { it.naziv == selectedOpis }
                    selectedEnum?.let {
                        selectedZemljisniTipovi.add(it)
                    }
                }
            }

            if(selectedKlimatskiTipovi.isEmpty()){
                Snackbar.make(dodajBiljkuBtn, "Mora biti odabran barem jedan klimatski tip.", Snackbar.LENGTH_SHORT).show()
                valid=false
            }
            if(selectedZemljisniTipovi.isEmpty()){
                Snackbar.make(dodajBiljkuBtn, "Mora biti odabran barem jedan zemljišni tip.", Snackbar.LENGTH_SHORT).show()
                valid=false
            }
            if(valid) {
                val novaBiljka = Biljka(
                    nazivBiljke,
                    porodicaBiljke,
                    medUpozorenjeBiljke,
                    selectedMedKoristi,
                    profilOkusa!!,
                    selectedJela,
                    selectedKlimatskiTipovi,
                    selectedZemljisniTipovi
                )
                GlobalScope.launch(Dispatchers.Main) {
                    val fixedBiljka = TrefleDAO().fixData(novaBiljka)
                    NovaBiljkaSingleton.novaBiljkaLiveData.value = fixedBiljka
                    finish()
                }
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
