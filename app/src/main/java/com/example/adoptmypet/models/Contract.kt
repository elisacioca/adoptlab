package com.example.adoptmypet.models

data class Contract(
    var id: String?,
    var path: String?,
    var name: String?,
    var extension: String?,
    var adoption: Adoption?,
    var foster: Foster?
)