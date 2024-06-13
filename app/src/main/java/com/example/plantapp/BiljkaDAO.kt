package com.example.plantapp

import android.graphics.Bitmap
import androidx.room.*

@Dao
interface BiljkaDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveBiljka(biljka: Biljka): Boolean {
        return insertBiljka(biljka) != -1L
    }

    @Query("SELECT id FROM Biljka WHERE naziv = :naziv LIMIT 1")
    fun getBiljkaIdByNaziv(naziv: String): Long

    @Query("SELECT * FROM Biljka WHERE onlineChecked = 0")
    fun getOfflineBiljkas(): List<Biljka>

    @Update
    fun updateBiljka(biljka: Biljka)

    @Transaction
    suspend fun fixOfflineBiljka(): Int {
        val offlineBiljkas = getOfflineBiljkas()
        var updatedCount = 0
        offlineBiljkas.forEach { biljka ->
            val fixedBiljka = TrefleDAO().fixData(biljka)
            if (biljka != fixedBiljka) {
                updateBiljka(fixedBiljka.copy(onlineChecked = true))
                updatedCount++
            }
        }
        return updatedCount
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBiljka(biljka: Biljka): Long

    @Query("SELECT * FROM Biljka")
    fun getAllBiljkas(): List<Biljka>

    @Query("DELETE FROM Biljka")
    fun clearBiljkas()

    @Query("DELETE FROM BiljkaBitmap")
    fun clearBiljkaBitmaps()

    @Query("SELECT * FROM BiljkaBitmap WHERE idBiljke = :idBiljke")
    fun getImageById(idBiljke: Long): BiljkaBitmap?

    @Transaction
    fun addImage(idBiljke: Long, bitmap: Bitmap): Boolean {
        val biljka = getImageById(idBiljke)
        if (biljka != null) {
            return false
        }
        val newBiljkaBitmap = BiljkaBitmap(idBiljke=idBiljke, bitmap = bitmap)
        return insertImage(newBiljkaBitmap) != -1L
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertImage(biljkaBitmap: BiljkaBitmap): Long

    @Transaction
    fun clearData() {
        clearBiljkas()
        clearBiljkaBitmaps()
    }
}