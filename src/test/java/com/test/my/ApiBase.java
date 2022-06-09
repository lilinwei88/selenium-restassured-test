package com.test.my;

import com.test.my.utils.RestAssuredRequestFilter;
import com.test.my.utils.PropertiesManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

import static com.test.my.utils.PropertiesManager.loadProp;

public class ApiBase {

    protected static RequestSpecification requestSpecification;
    static final String BASE_URL = PropertiesManager.loadProp("BASE_URL");

    @BeforeMethod
    public void beforeTestSetup(Method getMethod) {
        requestSpecification = RestAssured.given().filter(new RestAssuredRequestFilter());

    }

    public String generate_token()throws JSONException {
        String token_ENDPOINT = BASE_URL + "/attain-auth-broker/v1/token";
        Response response = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("client_id", "attain-kc-demo")
                .formParam("client_secret", "DNB2lbWvUy5ajGYIoFg2K2uhyICWgfAM")
                .formParam("grant_type", "client_credentials")
                .formParam("realm", "attain-poc")
                .formParam("protocol", "openid-connect")
                .formParam("scope", "openid")
                .header("x-clientrefid", "0e40dcda-a6f6-11ec-b909-0242ac120002")
                .when()
                .post(token_ENDPOINT);

        JSONObject jobj=new JSONObject(response.asString());
        return "Bearer "+jobj.getString("access_token");

    }

}
