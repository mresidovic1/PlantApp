package com.example.plantapp

enum class Zemljiste(val naziv: String) {
    PJESKOVITO("Pjeskovito zemljište"),
    GLINENO("Glinеno zemljište"),
    ILOVACA("Ilovača"),
    CRNICA("Crnica"),
    SLJUNKOVITO("Šljunovito zemljište"),
    KRECNJACKO("Krečnjačko zemljište");
    companion object{
        fun getOpisi() : List<String>{
            return Zemljiste.entries.map {it.naziv}
        }
    }
}
