package com.example.adoptmypet.models

import android.location.Location

data class Pet (
    var id: String?,
    var name: String?,
    var age: String?,
    var color: String?,
    var description: String?,
    var story: String?,
    var affection: Int?,
    var freedom: Int?,
    var location: Location?,
    var adoption: Adoption?,
    var fosters: List<Foster>?,
    var photos: List<Photo>?
    )