package com.example.adoptmypet.models

data class Adoption (
    var id: String? = null,
    var userAdopterId: String?,
    var telephoneNo: String?,
    var fullNameAdopter: String?,
    var userParentId: String? = null,
    var contract: Contract? = null,
    var finalized: Boolean? = null,
    var questionnaireAdopter: String?,
    var questionnaireParent: String? = null,
    var petId: String?
)