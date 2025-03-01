package com.example.plantapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NovaBiljkaActivity : AppCompatActivity() {
    private lateinit var medKoristLV : ListView
    private lateinit var klimatskiTipLV : ListView
    private lateinit var zemljisniTipLV : ListView
    private lateinit var jelaLV : ListView
    private lateinit var profilOkusaLV : ListView
    private lateinit var dodajJeloBtn : Button
    private var capturedBitmap: Bitmap? = null
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
            val nazivRegex = Regex("^[a-zA-ZčćžšđČĆŽŠĐ\\s]+\\s?\\([a-zA-ZčćžšđČĆŽŠĐ\\s]+\\)$")
            if (!nazivBiljke.matches(nazivRegex)) {
                naziv.error = "Naziv mora biti u formatu 'naziv (latinski_naziv)'"
                valid = false
            }
            if(naziv.length()<2 || naziv.length()>40) {
                naziv.setError("Riječ mora sadržavati najmanje 2, ali ne više od 40 karaktera")
                valid = false
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
                    naziv = nazivBiljke,
                    porodica = porodicaBiljke,
                    medicinskoUpozorenje = medUpozorenjeBiljke,
                    medicinskeKoristi = selectedMedKoristi,
                    profilOkusa = profilOkusa!!,
                    jela = selectedJela,
                    klimatskiTipovi = selectedKlimatskiTipovi,
                    zemljisniTipovi = selectedZemljisniTipovi
                )

                CoroutineScope(Dispatchers.IO).launch {
                    val fixedBiljka = TrefleDAO().fixData(novaBiljka)
                    val database = BiljkaDatabase.getDatabase(applicationContext)
                    val biljkaDAO = database.biljkaDao()
                    val biljkaSaved = biljkaDAO.saveBiljka(fixedBiljka)
                    if (biljkaSaved) {
                        val biljkaId = biljkaDAO.getBiljkaIdByNaziv(nazivBiljke)
                        capturedBitmap?.let{ bitmap ->
                            val visina = 150
                            val sirina = 150
                            val normalizovanaSlika = cropBitmap(bitmap,visina,sirina)
                            if (normalizovanaSlika != null) {
                                biljkaDAO.addImage(biljkaId,normalizovanaSlika)
                            }
                        }
                        /*val imageBitmap = capturedBitmap
                        val imageSaved = if (imageBitmap == null) {
                            val bitmap = TrefleDAO().getImage(novaBiljka)
                            biljkaDAO.addImage(biljkaId, bitmap)
                        } else {
                            val bitmap = (biljkaImage.drawable as BitmapDrawable).bitmap
                            biljkaDAO.addImage(biljkaId, bitmap)
                        }*/
                        withContext(Dispatchers.Main) {
                            NovaBiljkaSingleton.novaBiljkaLiveData.value = fixedBiljka
                            finish()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Snackbar.make(dodajBiljkuBtn, "Greška pri dodavanju biljke.", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    fun cropBitmap(bitmap: Bitmap?, width: Int, height: Int): Bitmap? {
        bitmap ?: return null

        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        val centerX = bitmapWidth / 2
        val centerY = bitmapHeight / 2

        val cropLeft = centerX - width / 2
        val cropTop = centerY - height / 2
        val cropRight = centerX + width / 2
        val cropBottom = centerY + height / 2

        return Bitmap.createBitmap(bitmap, cropLeft, cropTop, width, height)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            capturedBitmap = data?.extras?.get("data") as Bitmap
            biljkaImage.setImageBitmap(capturedBitmap)
        }
    }
}
