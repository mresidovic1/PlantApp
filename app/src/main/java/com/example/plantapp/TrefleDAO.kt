package com.example.plantapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL


class TrefleDAO {
    companion object {
        private lateinit var defaultBitmap: Bitmap
        fun initialize(context: Context) {
            defaultBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.plant1)
        }
    }

    suspend fun getImage(biljka: Biljka): Bitmap {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiAdapter.retrofit.getPlantInfo(getLatinskiNaziv(biljka.naziv) ?:"").execute()
                val responseBody = response.body()
                val plantInfo = responseBody?.data
                val imageUrl = plantInfo?.get(0)?.image_url
                if (imageUrl != null) {
                    try {
                        val imageStream = URL(imageUrl).openStream()
                        return@withContext BitmapFactory.decodeStream(imageStream)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext defaultBitmap
        }
    }

    suspend fun fixData(biljka: Biljka): Biljka {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiAdapter.retrofit.getPlantInfo(getLatinskiNaziv(biljka.naziv) ?: "").execute()
                if (response.isSuccessful) {
                    val plantInfo = response.body()?.data
                    val id: Int? = plantInfo?.getOrNull(0)?.id
                    if (id != -1) {
                        val responseDetail = ApiAdapter.retrofit.getPlantDetails(id).execute()
                        if (responseDetail.isSuccessful) {
                            val plantDetail = responseDetail.body()
                            val novaPorodica = plantDetail?.data?.family?.name
                            if (novaPorodica != null && biljka.porodica != novaPorodica) {
                                biljka.porodica = novaPorodica
                            }

                            val jestivo = plantDetail?.data?.main_species?.edible
                            if (jestivo != null && !jestivo) {
                                biljka.jela = emptyList()
                                if (!biljka.medicinskoUpozorenje.contains("NIJE JESTIVO")) {
                                    biljka.medicinskoUpozorenje += " NIJE JESTIVO"
                                }
                            }

                            val toksicnost = plantDetail?.data?.main_species?.toxicity
                            if (toksicnost != null && !toksicnost && !biljka.medicinskoUpozorenje.contains("TOKSIČNO")) {
                                biljka.medicinskoUpozorenje += " TOKSIČNO"
                            }

                            val soilTexture = plantDetail?.data?.main_species?.specifications?.growth?.soil_texture
                            val validSoilTextures = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                            if (soilTexture != null && !validSoilTextures.contains(soilTexture)) {
                                when (soilTexture) {
                                    9 -> biljka.zemljisniTipovi.toMutableList().add(Zemljiste.SLJUNKOVITO)
                                    10 -> biljka.zemljisniTipovi.toMutableList().add(Zemljiste.KRECNJACKO)
                                    in 1..2 -> biljka.zemljisniTipovi.toMutableList().add(Zemljiste.GLINENO)
                                    in 3..4 -> biljka.zemljisniTipovi.toMutableList().add(Zemljiste.PJESKOVITO)
                                    in 5..6 -> biljka.zemljisniTipovi.toMutableList().add(Zemljiste.ILOVACA)
                                    in 7..8 -> biljka.zemljisniTipovi.toMutableList().add(Zemljiste.CRNICA)
                                }
                            }

                            val light = plantDetail?.data?.main_species?.specifications?.growth?.light
                            val atmosphericHumidity = plantDetail?.data?.main_species?.specifications?.growth?.atmospheric_humidity
                            biljka.klimatskiTipovi = biljka.klimatskiTipovi.filter {
                                when (it) {
                                    KlimatskiTip.SREDOZEMNA -> light in 6..9 && atmosphericHumidity in 1..5
                                    KlimatskiTip.TROPSKA -> light in 8..10 && atmosphericHumidity in 7..10
                                    KlimatskiTip.SUBTROPSKA -> light in 6..9 && atmosphericHumidity in 5..8
                                    KlimatskiTip.UMJERENA -> light in 4..7 && atmosphericHumidity in 3..7
                                    KlimatskiTip.SUHA -> light in 7..9 && atmosphericHumidity in 1..2
                                    KlimatskiTip.PLANINSKA -> light in 0..5 && atmosphericHumidity in 3..7
                                }
                            }
                            if (light in 6..9 && atmosphericHumidity in 1..5) {
                                biljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.SREDOZEMNA)
                            }
                            if (light in 8..10 && atmosphericHumidity in 7..10) {
                                biljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.TROPSKA)
                            }
                            if (light in 6..9 && atmosphericHumidity in 5..8) {
                                biljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.SUBTROPSKA)
                            }
                            if (light in 4..7 && atmosphericHumidity in 3..7) {
                                biljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.UMJERENA)
                            }
                            if (light in 7..9 && atmosphericHumidity in 1..2) {
                                biljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.SUHA)
                            }
                            if (light in 0..5 && atmosphericHumidity in 3..7) {
                                biljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.PLANINSKA)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext biljka
        }
    }
}