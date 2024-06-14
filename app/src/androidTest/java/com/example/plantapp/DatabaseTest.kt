package com.example.plantapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var biljkaDao: BiljkaDAO
    private lateinit var db: BiljkaDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BiljkaDatabase::class.java).build()
        biljkaDao = db.biljkaDao()
    }

    @Test
    @Throws(Exception::class)
    fun dodajUListuProvjera() {
        val biljka = Biljka(
            naziv = "Biljka (Eriogonum thymoides)",
            porodica = "Polygonaceae",
            medicinskoUpozorenje = "Medicinsko upozorenje",
            medicinskeKoristi = listOf(),
            profilOkusa = ProfilOkusaBiljke.SLATKI,
            jela = listOf("jelo"),
            klimatskiTipovi = listOf(),
            zemljisniTipovi = listOf()
        )
        biljkaDao.saveBiljka(biljka)
        val biljke = biljkaDao.getAllBiljkas()
        val pronadjenaBiljka = biljke.find { it.naziv == biljka.naziv }
        assertTrue(pronadjenaBiljka != null)
    }

    @Test
    @Throws(Exception::class)
    fun provjeriIspraznjenuBazu() {
        val biljka = Biljka(
            naziv = "Biljka (Eriogonum vimineum)",
            porodica = "Polygonaceae",
            medicinskoUpozorenje = "Medicinsko upozorenje",
            medicinskeKoristi = listOf(),
            profilOkusa = ProfilOkusaBiljke.SLATKI,
            jela = listOf("jelo"),
            klimatskiTipovi = listOf(),
            zemljisniTipovi = listOf()
        )
        biljkaDao.clearData()
        val biljke = biljkaDao.getAllBiljkas()
        assertTrue(biljke.isEmpty())
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


}