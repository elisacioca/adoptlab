package com.example.adoptmypet.models

data class  Answer (
    var id: String?,
    var content: String?,
    var isChecked: Boolean?,
    var question: Question?,
    var affection: Int?,
    var freedom: Int?
)