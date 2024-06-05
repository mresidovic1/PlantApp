package com.example.plantapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("plants/search")
    fun getPlantInfo(
        @Query("q") query: String,
        @Query("token") token : String = "b4V-0hycfnbcwhxuxmtdwH7rn2BD1hOr32hX0GmdcbA"
    ): Call<PlantSearchResponse>

    @GET("plants/{id}")
    fun getPlantDetails(
        @Path("id") id: Int?,
        @Query("token") token: String ="b4V-0hycfnbcwhxuxmtdwH7rn2BD1hOr32hX0GmdcbA"
    ): Call<PlantDetailsResponse>

    @GET("plants/search")
    fun getPlantsWithFlowerColor(
        @Query("filter[flower_color]") flowerColor: String,
        @Query("q") query : String,
        @Query("token") token: String = "b4V-0hycfnbcwhxuxmtdwH7rn2BD1hOr32hX0GmdcbA",
        @Query("page") page: Int? = 1
    ): Call<PlantSearchResponse>
}
