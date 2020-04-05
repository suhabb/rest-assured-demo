package io.rest.pet.api_test;

import io.rest.pet.dto.Category;
import io.rest.pet.dto.Pet;
import io.rest.pet.util.TestUtil;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.apache.http.entity.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;


public class PetApiTest {


    private long id;

    @BeforeTest
    public void setUp() {
        id = new TestUtil().getId();
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }


    @Test(priority = 1)
    public void givePetData_Create_Pet_Return_StatusCode200_OK() {

        Category category = new Category();
        category.setId(21L);
        category.setName("German");
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName("Micky");
        pet.setStatus("available");
        pet.setCategory(category);
        pet.setPhotoUrls(Arrays.asList("http://photo-upload.micky.image.in"));

        JsonPath jsonPath = RestAssured.given()
                .when()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .with()
                .body(pet)//add pet object
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(200)
                .log()
                .body()
                .assertThat()
                .extract().body().jsonPath();

        //List<Pet> petList = jsonPath.getList("", Pet.class);
        assertEquals("Micky", jsonPath.get("name"));
        assertEquals("available", jsonPath.get("status"));

    }

    @Test(priority = 2)
    public void givenPetId_Update_Pet_StatusCode200_OK() {


        RestAssured.given()
                .when()
                .contentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                .formParam("name", "Tinker")
                .formParam("status", "available")
                .post("/pet/" + id)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200);

    }

    @Test(priority = 3)
    public void givenPetIdReturn_Pet_Data_StatusCode200_OK() {

        JsonPath jsonPath = RestAssured.given()
                .when()
                .get("/pet/" + id)
                .then()
                .assertThat()
                .statusCode(200)
                .assertThat()
                .extract().body().jsonPath();

        //List<Pet> petList = jsonPath.getList("", Pet.class);
        assertEquals("Tinker", jsonPath.get("name"));
        assertEquals("available", jsonPath.get("status"));

    }

    @Test(priority = 4)
    public void givenPetId_Delete_Pet_StatusCode200_OK() {

        RestAssured.given()
                .when()
                .header("api_key", "token")
                .delete("/pet/" + id)
                .then()
                .assertThat()
                .statusCode(200);


    }

}
