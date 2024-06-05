package com.example.plantapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.net.URL
import java.util.Locale


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
                            //porodica
                            val novaPorodica = plantDetail?.data?.family?.name
                            if (novaPorodica != null && biljka.porodica != novaPorodica) {
                                biljka.porodica = novaPorodica
                            }
                            //jestivost
                            val jestivo = plantDetail?.data?.main_species?.edible
                            if (jestivo != null && !jestivo) {
                                biljka.jela = emptyList()
                                if (!biljka.medicinskoUpozorenje.contains("NIJE JESTIVO")) {
                                    biljka.medicinskoUpozorenje += " NIJE JESTIVO"
                                }
                            }
                            //toksicnost
                            val toksicnost = plantDetail?.data?.main_species?.toxicity
                            if (toksicnost != null && !biljka.medicinskoUpozorenje.contains("TOKSIČNO")) {
                                biljka.medicinskoUpozorenje += " TOKSIČNO"
                            }
                            //zemljisni tipovi
                            val soilTexture = plantDetail?.data?.main_species?.growth?.soil_texture
                            val mapaDozvoljenihVrijednostiZT = mapOf(
                                "SLJUNKOVITO" to listOf(9),
                                "KRECNJACKO" to listOf(10),
                                "GLINENO" to listOf(1, 2),
                                "PJESKOVITO" to listOf(3, 4),
                                "ILOVACA" to listOf(5, 6),
                                "CRNICA" to listOf(7, 8)
                            )

                            if (soilTexture != null) {
                                biljka.zemljisniTipovi = mapaDozvoljenihVrijednostiZT.entries.filter { (_, opseg) ->
                                    soilTexture in opseg
                                }.map { (vrijednost, _) -> Zemljiste.valueOf(vrijednost) }
                            }
                            //klimatski tipovi
                            val light = plantDetail?.data?.main_species?.growth?.light
                            val atmosphericHumidity = plantDetail?.data?.main_species?.growth?.atmospheric_humidity
                            val mapaDozvoljenihVrijednosti = mapOf(
                                "SREDOZEMNA" to (6..9 to 1..5),
                                "TROPSKA" to (8..10 to 7..10),
                                "SUBTROPSKA" to (6..9 to 5..8),
                                "UMJERENA" to (4..7 to 3..7),
                                "SUHA" to (7..9 to 1..2),
                                "PLANINSKA" to (0..5 to 3..7)
                            )
                            if(light!=null && atmosphericHumidity!=null){
                                biljka.klimatskiTipovi=mapaDozvoljenihVrijednosti.entries.filter{ (_, opseg) ->
                                    val(lightOpseg,humidityOpseg)=opseg
                                    light in lightOpseg && atmosphericHumidity in humidityOpseg
                                }.map{(vrijednost, _) -> KlimatskiTip.valueOf(vrijednost)}
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

    suspend fun getPlantsWithFlowerColor(flower_color: String, substr: String): List<Biljka> {
        return withContext(Dispatchers.IO) {
            val resultList = mutableListOf<Biljka>()
            try {
                var page = 1
                var totalPages: Int
                do {
                    val response: Response<PlantSearchResponse> = ApiAdapter.retrofit.getPlantsWithFlowerColor(
                        flower_color, query = substr, page = page
                    ).execute()
                    val responseBody = response.body()
                    val plantInfo = responseBody?.data
                    totalPages = (responseBody?.meta?.total ?: 1) / 20
                    for(biljka in plantInfo!!){
                        val id: Int? = biljka.id
                        val responseDetail = ApiAdapter.retrofit.getPlantDetails(id).execute()
                        val plantDetail = responseDetail.body()
                            val nazivRegex = Regex("^[a-zA-ZčćžšđČĆŽŠĐ\\s]+\\s?\\([a-zA-ZčćžšđČĆŽŠĐ\\s]+\\)$")
                            var filtriraniNaziv = biljka.name
                            if (!biljka.name?.matches(nazivRegex)!!) {
                                filtriraniNaziv = "($filtriraniNaziv)"
                            }
                            val filtriranaBiljka = Biljka(
                                naziv = filtriraniNaziv!!,
                                porodica = plantDetail?.data?.family?.name ?: "",
                                medicinskoUpozorenje = "",
                                medicinskeKoristi = emptyList(),
                                profilOkusa = null,
                                jela = emptyList(),
                                zemljisniTipovi = emptyList(),
                                klimatskiTipovi = emptyList()
                            )
                            //klimatski tipovi
                            val light = plantDetail?.data?.main_species?.growth?.light
                            val atmosphericHumidity = plantDetail?.data?.main_species?.growth?.atmospheric_humidity
                            val mapaDozvoljenihVrijednosti = mapOf(
                                "SREDOZEMNA" to (6..9 to 1..5),
                                "TROPSKA" to (8..10 to 7..10),
                                "SUBTROPSKA" to (6..9 to 5..8),
                                "UMJERENA" to (4..7 to 3..7),
                                "SUHA" to (7..9 to 1..2),
                                "PLANINSKA" to (0..5 to 3..7)
                            )
                            if(light!=null && atmosphericHumidity!=null){
                                filtriranaBiljka.klimatskiTipovi=mapaDozvoljenihVrijednosti.entries.filter{ (_, opseg) ->
                                    val(lightOpseg,humidityOpseg)=opseg
                                    light in lightOpseg && atmosphericHumidity in humidityOpseg
                                }.map{(vrijednost, _) -> KlimatskiTip.valueOf(vrijednost)}
                            }
                            else{
                                filtriranaBiljka.klimatskiTipovi = emptyList()
                            }
                            //zemljisni tipovi
                            val soilTexture = plantDetail?.data?.main_species?.growth?.soil_texture
                            val mapaDozvoljenihVrijednostiZT = mapOf(
                                "SLJUNKOVITO" to listOf(9),
                                "KRECNJACKO" to listOf(10),
                                "GLINENO" to listOf(1, 2),
                                "PJESKOVITO" to listOf(3, 4),
                                "ILOVACA" to listOf(5, 6),
                                "CRNICA" to listOf(7, 8)
                            )

                            if (soilTexture != null) {
                                filtriranaBiljka.zemljisniTipovi = mapaDozvoljenihVrijednostiZT.entries.filter { (_, opseg) ->
                                    soilTexture in opseg
                                }.map { (vrijednost, _) -> Zemljiste.valueOf(vrijednost) }
                            }
                            else{
                                filtriranaBiljka.zemljisniTipovi = emptyList()
                            }
                            resultList.add(filtriranaBiljka)
                        }
                    page++
                } while (page <= totalPages)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext resultList
        }
    }
}