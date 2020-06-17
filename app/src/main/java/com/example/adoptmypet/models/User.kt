package com.example.adoptmypet.models

import android.location.Location

data class User(
    var username: String,
    var password: String? = null,
    var name: String? = null,
    val token: String,
    var pets: List<Pet>? = null,
    var location: Location? = null,
    var parentAdoptions: List<Adoption>? = null,
    var adoptersAdoptions: List<Adoption>? = null,
    var parentFosters: List<Foster>? = null,
    var fosterFosters: List<Foster>? = null,
    var donatorsDonations: List<Donation>? = null,
    var receiversDonations: List<Donation>? = null
)