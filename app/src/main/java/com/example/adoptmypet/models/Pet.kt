package com.example.adoptmypet.models

import android.location.Location

data class Pet (
    var petId: String? = null,
    var animalType: Int?,
    var name: String?,
    var age: String?,
    var sex: Int?,
    var color: String?,
    var description: String? = null,
    var story: String? = null,
    var affection: Int?,
    var freedom: Int?,
    var locationId: String? = null,
    var adoptionId: String? = null,
    var fosters: List<Foster>? = null,
    var userId: String? = null
    )