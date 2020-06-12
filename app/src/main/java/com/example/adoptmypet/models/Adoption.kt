package com.example.adoptmypet.models

data class Adoption (
    var id: String?,
    var userAdopter: User?,
    var userParent: User?,
    var contract: Contract?,
    var finalized: Boolean?,
    var pet: Pet?
)