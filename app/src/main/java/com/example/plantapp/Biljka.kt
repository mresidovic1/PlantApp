package com.example.plantapp

import java.util.ArrayList

data class Biljka(
    val naziv : String,
    var porodica : String,
    var medicinskoUpozorenje : String,
    val medicinskeKoristi : List<MedicinskaKorist>,
    val profilOkusa : ProfilOkusaBiljke,
    var jela : List<String>,
    var klimatskiTipovi : List<KlimatskiTip>,
    var zemljisniTipovi : List<Zemljiste>
)

