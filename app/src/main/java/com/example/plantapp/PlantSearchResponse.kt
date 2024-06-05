package com.example.plantapp

import com.google.gson.annotations.SerializedName

data class PlantSearchResponse(
    val data: List<PlantData>,
    val links: Links,
    val meta: Meta
)

data class PlantData(
    @SerializedName("id") val id: Int?,
    @SerializedName("image_url") val image_url: String?,
    @SerializedName("family") val family: String?,
    @SerializedName("scientific_name") val name : String?
)

data class Links(
    val self : String?,
    val first : String?,
    val last : String?
)

data class Meta(
    val total : Int?
)

data class PlantDetailsResponse(
    val data: PlantDetails,
    val meta: Meta
)

data class PlantDetails(
    @SerializedName("scientific_name") val scientific_name: String?,
    @SerializedName("main_species") val main_species: MainSpecies?,
    @SerializedName("family") val family: Family?
)

data class MainSpecies(
    @SerializedName("id") val id: Int?,
    @SerializedName("edible") val edible: Boolean?,
    @SerializedName("toxicity") val toxicity: Boolean?,
    @SerializedName("flower") val flower : Flower?,
    @SerializedName("growth") val growth: Growth?
    )

data class Flower(
    @SerializedName("color") val color : List<String>?
)

data class Family(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?
)

data class Growth(
    @SerializedName("soil_texture") val soil_texture: Int?,
    @SerializedName("light") val light: Int?,
    @SerializedName("atmospheric_humidity") val atmospheric_humidity: Int?
)

