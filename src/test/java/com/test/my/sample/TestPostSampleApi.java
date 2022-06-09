package com.test.my.sample;

import com.test.my.ApiBase;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.*;

import static io.restassured.RestAssured.given;

public class TestPostSampleApi extends ApiBase {

    @Test
    public void postUserTest() throws JSONException {

        JSONObject request = new JSONObject();
        request.put("name", "Ash");
        request.put("job", "Manager");

        given().
            body(request.toString()).
        when().
            post("https://reqres.in/api/users").
        then().
            statusCode(201);
    }
}
