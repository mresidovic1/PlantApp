package com.example.plantapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

class Konverteri {
    @TypeConverter
    fun fromMedicinskeKoristi(value: List<MedicinskaKorist>): String {
        val type = object : TypeToken<List<MedicinskaKorist>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toMedicinskeKoristi(value: String): List<MedicinskaKorist> {
        val type = object : TypeToken<List<MedicinskaKorist>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromProfilOkusa(value: ProfilOkusaBiljke?): String? {
        return value?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toProfilOkusa(value: String?): ProfilOkusaBiljke? {
        return value?.let { Gson().fromJson(it, ProfilOkusaBiljke::class.java) }
    }

    @TypeConverter
    fun fromJela(value: List<String>): String {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toJela(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromKlimatskiTipovi(value: List<KlimatskiTip>): String {
        val type = object : TypeToken<List<KlimatskiTip>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toKlimatskiTipovi(value: String): List<KlimatskiTip> {
        val type = object : TypeToken<List<KlimatskiTip>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromZemljisniTipovi(value: List<Zemljiste>): String {
        val type = object : TypeToken<List<Zemljiste>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toZemljisniTipovi(value: String): List<Zemljiste> {
        val type = object : TypeToken<List<Zemljiste>>() {}.type
        return Gson().fromJson(value, type)
    }
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @TypeConverter
    fun toBitmap(base64String: String?): Bitmap? {
        return if (base64String == null) null else {
            val byteArray = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }
}