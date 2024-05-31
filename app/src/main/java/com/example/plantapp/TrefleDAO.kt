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
                            if (toksicnost != null && !toksicnost && !biljka.medicinskoUpozorenje.contains("TOKSIČNO")) {
                                biljka.medicinskoUpozorenje += " TOKSIČNO"
                            }
                            //zemljisni tipovi
                            val soilTexture = plantDetail?.data?.main_species?.specifications?.growth?.soil_texture
                            if(soilTexture!=null){
                                if(soilTexture in 1..2){
                                    biljka.zemljisniTipovi = biljka.zemljisniTipovi.filter { it == Zemljiste.GLINENO }.toMutableList()
                                    if(!biljka.zemljisniTipovi.contains(Zemljiste.GLINENO)){
                                        biljka.zemljisniTipovi.toMutableList().add(Zemljiste.GLINENO)
                                    }
                                }
                                else if(soilTexture in 3..4){
                                    biljka.zemljisniTipovi = biljka.zemljisniTipovi.filter { it == Zemljiste.PJESKOVITO }.toMutableList()
                                    if(!biljka.zemljisniTipovi.contains(Zemljiste.PJESKOVITO)){
                                        biljka.zemljisniTipovi.toMutableList().add(Zemljiste.PJESKOVITO)
                                    }
                                }
                                else if(soilTexture in 5..6){
                                    biljka.zemljisniTipovi = biljka.zemljisniTipovi.filter { it == Zemljiste.ILOVACA }.toMutableList()
                                    if(!biljka.zemljisniTipovi.contains(Zemljiste.ILOVACA)){
                                        biljka.zemljisniTipovi.toMutableList().add(Zemljiste.ILOVACA)
                                    }
                                }
                                else if(soilTexture in 7..8){
                                    biljka.zemljisniTipovi = biljka.zemljisniTipovi.filter { it == Zemljiste.CRNICA }.toMutableList()
                                    if(!biljka.zemljisniTipovi.contains(Zemljiste.CRNICA)){
                                        biljka.zemljisniTipovi.toMutableList().add(Zemljiste.CRNICA)
                                    }
                                }
                                else if(soilTexture==9){
                                    biljka.zemljisniTipovi = biljka.zemljisniTipovi.filter { it == Zemljiste.SLJUNKOVITO }.toMutableList()
                                    if(!biljka.zemljisniTipovi.contains(Zemljiste.SLJUNKOVITO)){
                                        biljka.zemljisniTipovi.toMutableList().add(Zemljiste.SLJUNKOVITO)
                                    }
                                }
                                else if(soilTexture==10){
                                    biljka.zemljisniTipovi = biljka.zemljisniTipovi.filter { it == Zemljiste.KRECNJACKO }.toMutableList()
                                    if(!biljka.zemljisniTipovi.contains(Zemljiste.KRECNJACKO)){
                                        biljka.zemljisniTipovi.toMutableList().add(Zemljiste.KRECNJACKO)
                                    }
                                }
                                else{
                                    biljka.zemljisniTipovi = emptyList()
                                }
                            }
                            //klimatski tipovi
                            val light = plantDetail?.data?.main_species?.specifications?.growth?.light
                            val atmosphericHumidity = plantDetail?.data?.main_species?.specifications?.growth?.atmospheric_humidity
                            if(light!=null && atmosphericHumidity!=null){
                                if(light in 0..5 && atmosphericHumidity in 3..7){
                                    biljka.klimatskiTipovi = biljka.klimatskiTipovi.filter { it == KlimatskiTip.PLANINSKA }.toMutableList()
                                    if(!biljka.klimatskiTipovi.contains(KlimatskiTip.PLANINSKA)){
                                        biljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.PLANINSKA)
                                    }
                                }
                                else if(light in 4..7 && atmosphericHumidity in 3..7){
                                    biljka.klimatskiTipovi = biljka.klimatskiTipovi.filter { it == KlimatskiTip.UMJERENA }.toMutableList()
                                    if(!biljka.klimatskiTipovi.contains(KlimatskiTip.UMJERENA)){
                                        biljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.UMJERENA)
                                    }
                                }
                                else if(light in 6..9 && atmosphericHumidity in 1..5){
                                    biljka.klimatskiTipovi = biljka.klimatskiTipovi.filter { it == KlimatskiTip.SREDOZEMNA }.toMutableList()
                                    if(!biljka.klimatskiTipovi.contains(KlimatskiTip.SREDOZEMNA)){
                                        biljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.SREDOZEMNA)
                                    }
                                }
                                else if(light in 6..9 && atmosphericHumidity in 5..8){
                                    biljka.klimatskiTipovi = biljka.klimatskiTipovi.filter { it == KlimatskiTip.SUBTROPSKA }.toMutableList()
                                    if(!biljka.klimatskiTipovi.contains(KlimatskiTip.SUBTROPSKA)){
                                        biljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.SUBTROPSKA)
                                    }
                                }
                                else if(light in 7..9 && atmosphericHumidity in 1..2){
                                    biljka.klimatskiTipovi = biljka.klimatskiTipovi.filter { it == KlimatskiTip.SUHA }.toMutableList()
                                    if(!biljka.klimatskiTipovi.contains(KlimatskiTip.SUHA)){
                                        biljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.SUHA)
                                    }
                                }
                                else if(light in 8..10 && atmosphericHumidity in 7..10){
                                    biljka.klimatskiTipovi = biljka.klimatskiTipovi.filter { it == KlimatskiTip.TROPSKA }.toMutableList()
                                    if(!biljka.klimatskiTipovi.contains(KlimatskiTip.TROPSKA)){
                                        biljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.TROPSKA)
                                    }
                                }
                                else{
                                    biljka.klimatskiTipovi = emptyList()
                                }
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
                val response: Response<PlantSearchResponse> = ApiAdapter.retrofit.getPlantsWithFlowerColor(
                    flower_color
                ).execute()
                val responseBody = response.body()
                val plantInfo = responseBody?.data
                for(biljka in plantInfo!!){
                    val id: Int? = biljka.id
                    val responseDetail = ApiAdapter.retrofit.getPlantDetails(id).execute()
                    val plantDetail = responseDetail.body()
                    if(plantDetail?.data?.scientific_name?.contains(substr,ignoreCase = true) == true){
                        val light = plantDetail?.data?.main_species?.specifications?.growth?.light
                        val atmosphericHumidity = plantDetail?.data?.main_species?.specifications?.growth?.atmospheric_humidity
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
                        if(light!=null && atmosphericHumidity!=null){
                            if(light in 0..5 && atmosphericHumidity in 3..7){
                                filtriranaBiljka.klimatskiTipovi = filtriranaBiljka.klimatskiTipovi.filter { it == KlimatskiTip.PLANINSKA }.toMutableList()
                                if(!filtriranaBiljka.klimatskiTipovi.contains(KlimatskiTip.PLANINSKA)){
                                    filtriranaBiljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.PLANINSKA)
                                }
                            }
                            else if(light in 4..7 && atmosphericHumidity in 3..7){
                                filtriranaBiljka.klimatskiTipovi = filtriranaBiljka.klimatskiTipovi.filter { it == KlimatskiTip.UMJERENA }.toMutableList()
                                if(!filtriranaBiljka.klimatskiTipovi.contains(KlimatskiTip.UMJERENA)){
                                    filtriranaBiljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.UMJERENA)
                                }
                            }
                            else if(light in 6..9 && atmosphericHumidity in 1..5){
                                filtriranaBiljka.klimatskiTipovi = filtriranaBiljka.klimatskiTipovi.filter { it == KlimatskiTip.SREDOZEMNA }.toMutableList()
                                if(!filtriranaBiljka.klimatskiTipovi.contains(KlimatskiTip.SREDOZEMNA)){
                                    filtriranaBiljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.SREDOZEMNA)
                                }
                            }
                            else if(light in 6..9 && atmosphericHumidity in 5..8){
                                filtriranaBiljka.klimatskiTipovi = filtriranaBiljka.klimatskiTipovi.filter { it == KlimatskiTip.SUBTROPSKA }.toMutableList()
                                if(!filtriranaBiljka.klimatskiTipovi.contains(KlimatskiTip.SUBTROPSKA)){
                                    filtriranaBiljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.SUBTROPSKA)
                                }
                            }
                            else if(light in 7..9 && atmosphericHumidity in 1..2){
                                filtriranaBiljka.klimatskiTipovi = filtriranaBiljka.klimatskiTipovi.filter { it == KlimatskiTip.SUHA }.toMutableList()
                                if(!filtriranaBiljka.klimatskiTipovi.contains(KlimatskiTip.SUHA)){
                                    filtriranaBiljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.SUHA)
                                }
                            }
                            else if(light in 8..10 && atmosphericHumidity in 7..10){
                                filtriranaBiljka.klimatskiTipovi = filtriranaBiljka.klimatskiTipovi.filter { it == KlimatskiTip.TROPSKA }.toMutableList()
                                if(!filtriranaBiljka.klimatskiTipovi.contains(KlimatskiTip.TROPSKA)){
                                    filtriranaBiljka.klimatskiTipovi.toMutableList().add(KlimatskiTip.TROPSKA)
                                }
                            }
                            else{
                                filtriranaBiljka.klimatskiTipovi= emptyList()
                            }
                        }
                        val soilTexture = plantDetail?.data?.main_species?.specifications?.growth?.soil_texture
                        if(soilTexture!=null){
                            if(soilTexture in 1..2){
                                filtriranaBiljka.zemljisniTipovi = filtriranaBiljka.zemljisniTipovi.filter { it == Zemljiste.GLINENO }.toMutableList()
                                if(!filtriranaBiljka.zemljisniTipovi.contains(Zemljiste.GLINENO)){
                                    filtriranaBiljka.zemljisniTipovi.toMutableList().add(Zemljiste.GLINENO)
                                }
                            }
                            else if(soilTexture in 3..4){
                                filtriranaBiljka.zemljisniTipovi = filtriranaBiljka.zemljisniTipovi.filter { it == Zemljiste.PJESKOVITO }.toMutableList()
                                if(!filtriranaBiljka.zemljisniTipovi.contains(Zemljiste.PJESKOVITO)){
                                    filtriranaBiljka.zemljisniTipovi.toMutableList().add(Zemljiste.PJESKOVITO)
                                }
                            }
                            else if(soilTexture in 5..6){
                                filtriranaBiljka.zemljisniTipovi = filtriranaBiljka.zemljisniTipovi.filter { it == Zemljiste.ILOVACA }.toMutableList()
                                if(!filtriranaBiljka.zemljisniTipovi.contains(Zemljiste.ILOVACA)){
                                    filtriranaBiljka.zemljisniTipovi.toMutableList().add(Zemljiste.ILOVACA)
                                }
                            }
                            else if(soilTexture in 7..8){
                                filtriranaBiljka.zemljisniTipovi = filtriranaBiljka.zemljisniTipovi.filter { it == Zemljiste.CRNICA }.toMutableList()
                                if(!filtriranaBiljka.zemljisniTipovi.contains(Zemljiste.CRNICA)){
                                    filtriranaBiljka.zemljisniTipovi.toMutableList().add(Zemljiste.CRNICA)
                                }
                            }
                            else if(soilTexture==9){
                                filtriranaBiljka.zemljisniTipovi = filtriranaBiljka.zemljisniTipovi.filter { it == Zemljiste.SLJUNKOVITO }.toMutableList()
                                if(!filtriranaBiljka.zemljisniTipovi.contains(Zemljiste.SLJUNKOVITO)){
                                    filtriranaBiljka.zemljisniTipovi.toMutableList().add(Zemljiste.SLJUNKOVITO)
                                }
                            }
                            else if(soilTexture==10){
                                filtriranaBiljka.zemljisniTipovi = filtriranaBiljka.zemljisniTipovi.filter { it == Zemljiste.KRECNJACKO }.toMutableList()
                                if(!filtriranaBiljka.zemljisniTipovi.contains(Zemljiste.KRECNJACKO)){
                                    filtriranaBiljka.zemljisniTipovi.toMutableList().add(Zemljiste.KRECNJACKO)
                                }
                            }
                            else{
                                filtriranaBiljka.zemljisniTipovi = emptyList()
                            }
                        }
                        resultList.add(filtriranaBiljka)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext resultList
        }
    }

}