package com.example.plantapp

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Biljka")
data class Biljka(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name="naziv") val naziv: String,
    @ColumnInfo(name="family") var porodica: String,
    @ColumnInfo(name="medicinskoUpozorenje") var medicinskoUpozorenje: String,
    @ColumnInfo(name = "medicinskeKoristi") val medicinskeKoristi: List<MedicinskaKorist>,
    @ColumnInfo(name = "profilOkusa") val profilOkusa: ProfilOkusaBiljke?,
    @ColumnInfo(name = "jela") var jela: List<String>,
    @ColumnInfo(name = "klimatskiTipovi") var klimatskiTipovi: List<KlimatskiTip>,
    @ColumnInfo(name = "zemljisniTipovi") var zemljisniTipovi: List<Zemljiste>,
    @ColumnInfo(name = "onlineChecked") var onlineChecked: Boolean = false
)

@Entity(tableName = "BiljkaBitmap", foreignKeys = [ForeignKey(entity = Biljka::class,
    parentColumns = ["id"],
    childColumns = ["idBiljke"],
    onDelete = ForeignKey.CASCADE)])
data class BiljkaBitmap(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "idBiljke") val idBiljke: Long,
    @ColumnInfo(name = "bitmap") val bitmap: Bitmap
)
