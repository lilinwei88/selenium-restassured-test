package com.test.my.sample;

import com.test.my.ApiBase;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.*;

import static io.restassured.RestAssured.given;

public class TestPutSampleApi extends ApiBase {

    @Test
    public void putUserTest() throws JSONException {

        JSONObject request = new JSONObject();
        request.put("name", "Ash");
        request.put("job", "Director");

        requestSpecification.
            body(request.toString()).
        when().
            put("https://reqres.in/api/users/111").
        then().
            statusCode(200);
    }
}
