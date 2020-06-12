package com.example.adoptmypet.models

data class Question (
    var id: String?,
    var questionText: String?,
    var answers: List<Answer>?,
    var forAdoption: Boolean?
)