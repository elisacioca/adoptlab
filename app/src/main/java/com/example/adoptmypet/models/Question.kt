package com.example.adoptmypet.models

data class Question (
    var id: Int?,
    var questionText: String?,
    var answers: List<Answer>?,
    var forAdoption: Boolean?
)