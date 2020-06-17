package com.example.adoptmypet.models

data class Photo (
    var id: String?,
    var path: String?,
    var extension: String?,
    var pet: Pet?,
    var donation: Donation?
)