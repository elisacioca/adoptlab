package com.example.adoptmypet.models

data class  Answer (
    var id: Int?,
    var content: String?,
    var question: Question?,
    var affection: Int?,
    var freedom: Int?,
    var factorRisk: Int?
)