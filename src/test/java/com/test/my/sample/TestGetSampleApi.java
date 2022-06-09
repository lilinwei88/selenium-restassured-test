package com.test.my.sample;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsIterableContaining.hasItems;


import com.test.my.ApiBase;
import org.testng.annotations.*;

public class TestGetSampleApi extends ApiBase {

    @Test(description = "Test list of users")
    public void getUsersTest() {
        given().
            header("Contetnt-Type", "application/json").
        when().
            get("https://reqres.in/api/users?page=2").
        then().
            statusCode(200).
            statusLine("HTTP/1.1 200 OK").
            body("data.id[1]", equalTo(8)).
            body("data.first_name", hasItems("Tobias", "Michael")).
        log().all();
    }

    @Test
    public void getUserTest() {
        given().
            header("Contetnt-Type", "application/json").
        when().
            get("https://reqres.in/api/users/2").
        then().
            statusCode(200).
            body("data.id", equalTo(2)).
        log().all();
    }
}
