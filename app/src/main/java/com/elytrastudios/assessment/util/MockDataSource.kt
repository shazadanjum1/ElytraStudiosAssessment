package com.elytrastudios.assessment.data.mock

import com.elytrastudios.assessment.data.model.Address
import com.elytrastudios.assessment.data.model.Company
import com.elytrastudios.assessment.data.model.DogBreed
import com.elytrastudios.assessment.data.model.Geo
import com.elytrastudios.assessment.data.model.UserResponse
import com.elytrastudios.assessment.util.AppLogger

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockDataSource @Inject constructor() {

    fun getMockBreeds(): List<DogBreed> {
        AppLogger.i("MockDataSource", "Returning mock breeds")
        return listOf(
            DogBreed("bulldog", listOf("english", "french")),
            DogBreed("retriever", listOf("golden", "flatcoated")),
            DogBreed("poodle", listOf("miniature", "standard")),
            DogBreed("beagle", emptyList()),
            DogBreed("husky", emptyList()),
            DogBreed("dachshund", emptyList()),
            DogBreed("chihuahua", emptyList()),
            DogBreed("boxer", emptyList()),
            DogBreed("spaniel", listOf("cocker", "springer")),
            DogBreed("terrier", listOf("yorkshire", "australian"))
        )
    }

    /*fun getMockUsers(): List<UserResponse> {
        AppLogger.i("MockDataSource", "Returning mock users")
        return listOf(
            UserResponse(1, "Leanne Graham", "Bret", "Sincere@april.biz"),
            UserResponse(2, "Ervin Howell", "Antonette", "Shanna@melissa.tv"),
            UserResponse(3, "Clementine Bauch", "Samantha", "Nathan@yesenia.net")
        )
    }*/

    fun getMockUsers(): List<UserResponse> {
        AppLogger.i("MockDataSource", "Returning mock users")
        return listOf(
            UserResponse(
                id = 1,
                name = "Leanne Graham",
                username = "Bret",
                email = "Sincere@april.biz",
                address = Address(
                    street = "Kulas Light",
                    suite = "Apt. 556",
                    city = "Gwenborough",
                    zipcode = "92998-3874",
                    geo = Geo(lat = "-37.3159", lng = "81.1496")
                ),
                phone = "1-770-736-8031 x56442",
                website = "hildegard.org",
                company = Company(
                    name = "Romaguera-Crona",
                    catchPhrase = "Multi-layered client-server neural-net",
                    bs = "harness real-time e-markets"
                )
            ),
            UserResponse(
                id = 2,
                name = "Ervin Howell",
                username = "Antonette",
                email = "Shanna@melissa.tv",
                address = Address(
                    street = "Victor Plains",
                    suite = "Suite 879",
                    city = "Wisokyburgh",
                    zipcode = "90566-7771",
                    geo = Geo(lat = "-43.9509", lng = "-34.4618")
                ),
                phone = "010-692-6593 x09125",
                website = "anastasia.net",
                company = Company(
                    name = "Deckow-Crist",
                    catchPhrase = "Proactive didactic contingency",
                    bs = "synergize scalable supply-chains"
                )
            ),
            UserResponse(
                id = 3,
                name = "Clementine Bauch",
                username = "Samantha",
                email = "Nathan@yesenia.net",
                address = Address(
                    street = "Douglas Extension",
                    suite = "Suite 847",
                    city = "McKenziehaven",
                    zipcode = "59590-4157",
                    geo = Geo(lat = "-68.6102", lng = "-47.0653")
                ),
                phone = "1-463-123-4447",
                website = "ramiro.info",
                company = Company(
                    name = "Romaguera-Jacobson",
                    catchPhrase = "Face to face bifurcated interface",
                    bs = "e-enable strategic applications"
                )
            )
        )
    }


}
