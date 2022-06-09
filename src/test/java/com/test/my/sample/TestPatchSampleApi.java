package com.test.my.sample;

import com.test.my.ApiBase;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TestPatchSampleApi extends ApiBase {

    @Test
    public void patchUserTest() throws JSONException {
        JSONObject request = new JSONObject();
        request.put("name", "Ash");
        request.put("job", "Director");

        requestSpecification.
            body(request.toString()).
        when().
            patch("https://reqres.in/api/users/111").
        then().
            statusCode(200);
    }
}
