package com.example.plantapp

val biljke = listOf(
    Biljka(
        naziv = "Bosiljak (Ocimum basilicum)",
        porodica = "Lamiaceae (usnate)",
        medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE),
        profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
        jela = listOf("Salata od paradajza", "Punjene tikvice"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
    ),
    Biljka(
        naziv = "Nana (Mentha spicata)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine.",
        medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PROTIVBOLOVA),
        profilOkusa = ProfilOkusaBiljke.MENTA,
        jela = listOf("Jogurt sa voćem", "Gulaš"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
        zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.CRNICA)
    ),
    Biljka(
        naziv = "Kamilica (Matricaria chamomilla)",
        porodica = "Asteraceae (glavočike)",
        medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Čaj od kamilice"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
    ),
    Biljka(
        naziv = "Ružmarin (Rosmarinus officinalis)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Treba ga koristiti umjereno i konsultovati se sa ljekarom pri dugotrajnoj upotrebi ili upotrebi u većim količinama.",
        medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPRITISKA),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Pečeno pile", "Grah","Gulaš"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
        zemljisniTipovi = listOf(Zemljiste.SLJUNKOVITO, Zemljiste.KRECNJACKO)
    ),
    Biljka(
        naziv = "Lavanda (Lavandula angustifolia)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine. Također, treba izbjegavati kontakt lavanda ulja sa očima.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PODRSKAIMUNITETU),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Jogurt sa voćem"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
    ),
    Biljka(
        naziv = "Kopriva (Urtica dioica)",
        porodica = "Urticaceae (koprive)",
        medicinskoUpozorenje = "Može izazvati iritaciju kože kod dodira s dlakama na listovima.",
        medicinskeKoristi = listOf(MedicinskaKorist.PODRSKAIMUNITETU),
        profilOkusa = ProfilOkusaBiljke.GORKO,
        jela = listOf("Koprivna juha"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA),
        zemljisniTipovi = listOf(Zemljiste.CRNICA, Zemljiste.PJESKOVITO)
    ),
    Biljka(
        naziv = "Đumbir (Zingiber officinale)",
        porodica = "Zingiberaceae (đumbirke)",
        medicinskoUpozorenje = "Nije preporučljivo za trudnice u većim količinama, može izazvati žgaravicu.",
        medicinskeKoristi = listOf(MedicinskaKorist.PODRSKAIMUNITETU),
        profilOkusa = ProfilOkusaBiljke.LJUTO,
        jela = listOf("Čaj od đumbira"),
        klimatskiTipovi = listOf(KlimatskiTip.TROPSKA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.CRNICA)
    ),
    Biljka(
        naziv = "Eukaliptus (Eucalyptus globulus)",
        porodica = "Myrtaceae (mrtvačnice)",
        medicinskoUpozorenje = "Nije preporučljivo za oralnu upotrebu kod djece mlađe od 6 godina. Može izazvati alergijske reakcije kod osjetljivih osoba.",
        medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Eukaliptusni čaj"),
        klimatskiTipovi = listOf(KlimatskiTip.SUBTROPSKA),
        zemljisniTipovi = listOf(Zemljiste.CRNICA)
    ),
    Biljka(
        naziv = "Maslačak (Taraxacum)",
        porodica = "Asteraceae (glavočike)",
        medicinskoUpozorenje = "Osobe koje su alergične na cvjetni pelud mogu imati alergijske reakcije na maslačak.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE),
        profilOkusa = ProfilOkusaBiljke.GORKO,
        jela = listOf("Salata od maslačka"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO)
    ),
    Biljka(
        naziv = "Stolisnik (Achillea millefolium)",
        porodica = "Asteraceae (glavočike)",
        medicinskoUpozorenje = "Može izazvati osjetljivost na sunce kod nekih ljudi.",
        medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPROBAVE),
        profilOkusa = ProfilOkusaBiljke.GORKO,
        jela = listOf("Čaj od stolisnika"),
        klimatskiTipovi = listOf(KlimatskiTip.PLANINSKA),
        zemljisniTipovi = listOf(Zemljiste.GLINENO)
    ),
    Biljka(
        naziv = "Cimet (Cinnamomum verum)",
        porodica = "Lauraceae (lovorike)",
        medicinskoUpozorenje = "Nije preporučljivo koristiti u velikim količinama zbog mogućih nuspojava poput oštećenja jetre.",
        medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPROBAVE),
        profilOkusa = ProfilOkusaBiljke.SLATKI,
        jela = listOf("Čaj od cimeta"),
        klimatskiTipovi = listOf(KlimatskiTip.TROPSKA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO)
    ),
    Biljka(
        naziv = "Glog (Crataegus)",
        porodica = "Rosaceae (ruže)",
        medicinskoUpozorenje = "Nemojte konzumirati u velikim količinama, može izazvati srčane aritmije.",
        medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPRITISKA),
        profilOkusa = ProfilOkusaBiljke.GORKO,
        jela = listOf("Glogova tinktura"),
        klimatskiTipovi = listOf(KlimatskiTip.PLANINSKA),
        zemljisniTipovi = listOf(Zemljiste.GLINENO)
    ),
    Biljka(
        naziv = "Origano (Origanum vulgare)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Može izazvati alergijske reakcije kod osjetljivih osoba.",
        medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Pizza", "Tjestenina s umakom od rajčice"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO)
    ),
    Biljka(
        naziv = "Peršin (Petroselinum crispum)",
        porodica = "Apiaceae (štitarkovke)",
        medicinskoUpozorenje = "Osobe koje uzimaju lijekove za razrjeđivanje krvi trebaju izbjegavati konzumaciju peršina u velikim količinama.",
        medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPROBAVE),
        profilOkusa = ProfilOkusaBiljke.SLATKI,
        jela = listOf("Tabbouleh", "Goveđi gulaš"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA),
        zemljisniTipovi = listOf(Zemljiste.CRNICA)
    ),
    Biljka(
        naziv = "Majčina dušica (Thymus vulgaris)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Može izazvati alergijske reakcije kod osjetljivih osoba.",
        medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Piletina s majčinom dušicom", "Čaj od majčine dušice"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO)
    )
)

fun getLatinskiNaziv(naziv: String): String? {
    val regex = "\\(([^)]+)\\)".toRegex()
    val matchResult = regex.find(naziv)
    return matchResult?.groups?.get(1)?.value
}

