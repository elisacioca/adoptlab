package com.example.adoptmypet.models

data class Adoption (
    var id: String?,
    var userAdopter: User?,
    var telephoneNo: String?,
    var fullNameAdopter: String?,
    var userParent: User?,
    var contract: Contract?,
    var finalized: Boolean?,
    var questionnaireAdopter: String?,
    var questionnaireParent: String?,
    var pet: Pet?
)