package com.example.adoptmypet.models

import okhttp3.MultipartBody

data class Photo (
    var id: String? = null,
    var path: String? = null,
    var name: String? = null,
    var photoFile: MultipartBody.Part? = null,
    var petId: String? = null,
    var donation: Donation? = null
)