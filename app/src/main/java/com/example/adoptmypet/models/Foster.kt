package com.example.adoptmypet.models

import java.util.*

data class Foster (
    var id: String?,
    var userFoster: User?,
    var userParent: User?,
    var contract: Contract?,
    var pet: Pet?,
    var startDate: Date?,
    var finalDate: Date?,
    var price: Float?
)