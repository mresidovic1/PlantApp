package com.example.plantapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    /*******DODATNI TEST ZA PROVJERU FIX OFFLINE BILJKA METODE*******/
    @Test
    @Throws(Exception::class)
    fun testFixOfflineBiljka() {
        val biljka1 = Biljka(
            naziv = "Biljka (Eriogonum thymoides)",
            porodica = "porodica",
            medicinskoUpozorenje = "Medicinsko upozorenje",
            medicinskeKoristi = listOf(),
            profilOkusa = ProfilOkusaBiljke.SLATKI,
            jela = listOf("jelo"),
            klimatskiTipovi = listOf(),
            zemljisniTipovi = listOf()
        )
        val biljka2 = Biljka(
            naziv = "Biljka (Taraxacum palustre)",
            porodica = "porodica",
            medicinskoUpozorenje = "Medicinsko upozorenje",
            medicinskeKoristi = listOf(),
            profilOkusa = ProfilOkusaBiljke.SLATKI,
            jela = listOf("jelo"),
            klimatskiTipovi = listOf(),
            zemljisniTipovi = listOf()
        )

        GlobalScope.launch(Dispatchers.IO) {
            biljkaDao.saveBiljka(biljka1)
            biljkaDao.saveBiljka(biljka2)

            val updatedCount = biljkaDao.fixOfflineBiljka()

            val updatedBiljka1 = biljkaDao.getAllBiljkas().find { it.naziv == biljka1.naziv }
            val updatedBiljka2 = biljkaDao.getAllBiljkas().find { it.naziv == biljka2.naziv }

            assertEquals(2, updatedCount)
            assertEquals(true, updatedBiljka1?.onlineChecked)
            assertEquals(true, updatedBiljka2?.onlineChecked)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}