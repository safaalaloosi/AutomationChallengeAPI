package com;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class API {
    public String token;

    @BeforeClass
    public void init() {
        baseURI = "https://code-api-staging.easypayfinance.com";


    }

    @Test(priority = 1)
    public void getAuthToken() {

        Map<String, String> basicAuthCredentials = new LinkedHashMap<>();
        basicAuthCredentials.put("username", "user");
        basicAuthCredentials.put("password", "pass");

        token = given()
                .body(basicAuthCredentials)
                .contentType(ContentType.JSON)

                .when()
                .post("/api/Authentication/login")

                .then()
                .statusCode(200).extract().jsonPath().getString("token");

        System.out.println("************************************************************");
        System.out.println("Basic Auth Token = " + token);
        System.out.println("************************************************************");


    }

    @Test(priority = 2)
    public void getAllApps() {
        given()
                .header("Authorization", "Bearer " + token)

                .when()
                .get("/api/Application/all").prettyPeek()

                .then()
                .statusCode(200);
    }

    @Test(priority = 3)
    public void postAnApp() {
        Faker faker = new Faker();

        given()
                .header("Authorization", "Bearer " + token)
                .body(" {\n" +
                        "        \"applicationId\": " + faker.number().numberBetween(1000, 9999) + ",\n" +
                        "            \"name\": \"" + faker.lorem().characters(9) + "\",\n" +
                        "            \"age\": \"" + faker.number().numberBetween(18, 30) + "\",\n" +
                        "            \"amount\": " + faker.number().numberBetween(0, 999) + "\n" +
                        "    }")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/Application").prettyPeek()

                .then()
                .statusCode(200);
    }


}
