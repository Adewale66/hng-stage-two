package com.hngstagetwo;


import com.hngstagetwo.auth.AuthenticationService;
import com.hngstagetwo.dtos.LoginDto;
import com.hngstagetwo.dtos.RegisterDto;
import com.hngstagetwo.users.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.path.json.JsonPath.from;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class AuthSpecTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void UserCanRegister() {
        RegisterDto registerDto = new RegisterDto("tester", "testing", "tst@123.com", "23", "passo");

        given().contentType(ContentType.JSON)
                .body(registerDto)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201)
                .body("status", equalTo("success"))
                .body("message", equalTo("Registration successful"))
                .body("data", notNullValue());
    }

    @Test
    void validationError() {
        RegisterDto registerDto = new RegisterDto("", "testing", "tst@123.com", "23", "passo");

        given().contentType(ContentType.JSON)
                .body(registerDto)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(422)
                .body("errors", notNullValue());
    }

    @Test
    void UserDoesNotExist() {
        RegisterDto registerDto = new RegisterDto("tester", "testing", "bad@123.com", "23", "passo");

        given().contentType(ContentType.JSON)
                .body(registerDto)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);

    }

}
