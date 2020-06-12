package com.example.adoptmypet.api
import android.location.Location
import com.example.adoptmypet.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*


interface Service {
    @GET("api/users")
    fun getUsers(
    ): Call<List<User>>

    @GET("api/pets")
    fun getPets(
    ): Call<List<Pet>>

    @GET("api/adoptions")
    fun getAdoptions(
    ): Call<List<Adoption>>

    @GET("api/fosters")
    fun getFosters(
    ): Call<List<Foster>>

    @GET("api/donations")
    fun getDonations(
    ): Call<List<Donation>>

    @GET("api/answers")
    fun getAnswers(
    ): Call<List<Answer>>

    @GET("api/locations")
    fun getLocations(
    ): Call<List<Location>>

    @GET("api/questions")
    fun getQuestions(
    ): Call<List<Question>>

    @GET("api/questions/{forAdoption}")
    fun getQuestionsByRole(
        @Path("forAdoption") forAdoption: Boolean
    ): Call<List<Question>>

    @GET("api/photos")
    fun getPhotos(
    ): Call<List<Photo>>

    @GET("api/contracts")
    fun getContracts(
    ): Call<List<Contract>>

    @GET("api/users/{id}")
    fun getUser(
        @Path("id") id: String
    ): Call<User>

    @GET("api/pets/{id}")
    fun getPet(
        @Path("id") id: String
    ): Call<Pet>

    @GET("api/adoptions/{id}")
    fun getAdoption(
        @Path("id") id: String
    ): Call<Adoption>

    @GET("api/fosters/{id}")
    fun getFoster(
        @Path("id") id: String
    ): Call<Foster>

    @GET("api/donations/{id}")
    fun getDonation(
        @Path("id") id: String
    ): Call<Donation>

    @GET("api/answers/{id}")
    fun getAnswer(
        @Path("id") id: String
    ): Call<Answer>

    @GET("api/locations/{id}")
    fun getLocation(
        @Path("id") id: String
    ): Call<Location>

    @GET("api/questions/{id}")
    fun getQuestion(
        @Path("id") id: String
    ): Call<Question>

    @GET("api/photos/{id}")
    fun getPhoto(
        @Path("id") id: String
    ): Call<Photo>

    @GET("api/contracts/{id}")
    fun getContract(
        @Path("id") id: String
    ): Call<Contract>

    @POST("api/users/authenticate")
    fun loginUser(
        @Body user: com.example.adoptmypet.entities.User
    ): Call<User>

    @POST("api/users")
    fun addUser(
        @Body user: User
    ): Call<User>

    @POST("api/pets")
    fun addPet(
        @Body pet: Pet
    ): Call<Pet>

    @POST("api/adoptions")
    fun addAdoption(
        @Body adoption: Adoption
    ): Call<Adoption>

    @POST("api/fosters")
    fun addFoster(
        @Body foster: Foster
    ): Call<Foster>

    @POST("api/donations")
    fun addDonation(
        @Body donation: Donation
    ): Call<Donation>

    @POST("api/locations")
    fun addUser(
        @Body location: Location
    ): Call<Location>

    @POST("api/photos")
    fun addPhoto(
        @Body photo: Photo
    ): Call<Photo>

    @POST("api/contracts")
    fun addContract(
        @Body contract: Contract
    ): Call<Contract>

    @POST("api/questions")
    fun addQuestion(
        @Body question: Question
    ): Call<Question>

    @POST("api/answers")
    fun addAnswer(
        @Body answer: Answer
    ): Call<Answer>

    @PUT("api/users/{id}")
    fun updateUser(
        @Path("id") id: String,
        @Body user: User
    ): Call<Unit>

    @PUT("api/pets/{id}")
    fun updatePet(
        @Path("id") id: String,
        @Body pet: Pet
    ): Call<Unit>

    @PUT("api/locations/{id}")
    fun updateLocation(
        @Path("id") id: String,
        @Body location: Location
    ): Call<Unit>

    @PUT("api/questions/{id}")
    fun updateQuestion(
        @Path("id") id: String,
        @Body question: Question
    ): Call<Unit>

    @PUT("api/answers/{id}")
    fun updateAnswer(
        @Path("id") id: String,
        @Body answer: Answer
    ): Call<Unit>

    @PUT("api/adoptions/{id}")
    fun updateAdoption(
        @Path("id") id: String,
        @Body adoption: Adoption
    ): Call<Unit>

    @PUT("api/fosters/{id}")
    fun updateFoster(
        @Path("id") id: String,
        @Body foster: Foster
    ): Call<Unit>

    @PUT("api/donations/{id}")
    fun updateDonation(
        @Path("id") id: String,
        @Body donation: Donation
    ): Call<Unit>

    @PUT("api/photos/{id}")
    fun updatePhoto(
        @Path("id") id: String,
        @Body photo: Photo
    ): Call<Unit>

    @DELETE("api/users/{id}")
    fun deleteUser(
        @Path("id") Id: String
    ): Call<Unit>

    @DELETE("api/pets/{id}")
    fun deletePet(
        @Path("id") id: String
    ): Call<Unit>

    @DELETE("api/adoptions/{id}")
    fun deleteAdoption(
        @Path("id") id: String
    ): Call<Unit>

    @DELETE("api/fosters/{id}")
    fun deleteFoster(
        @Path("id") id: String
    ): Call<Unit>

    @DELETE("api/donations/{id}")
    fun deleteDonation(
        @Path("id") id: String
    ): Call<Unit>

    @DELETE("api/answers/{id}")
    fun deleteAnswer(
        @Path("id") id: String
    ): Call<Unit>

    @DELETE("api/locations/{id}")
    fun deleteLocation(
        @Path("id") id: String
    ): Call<Unit>

    @DELETE("api/questions/{id}")
    fun deleteQuestion(
        @Path("id") id: String
    ): Call<Unit>

    @DELETE("api/photos/{id}")
    fun deletePhoto(
        @Path("id") id: String
    ): Call<Unit>

    @DELETE("api/contracts/{id}")
    fun deleteContract(
        @Path("id") id: String
    ): Call<Unit>
}