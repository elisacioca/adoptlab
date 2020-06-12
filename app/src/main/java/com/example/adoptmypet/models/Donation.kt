package com.example.adoptmypet.models

data class Donation (
    var id: String?,
    var userDonator: User?,
    var userReceiver: User?,
    var productName: String?,
    var photos: List<Photo>?
)