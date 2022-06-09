package com.test.my.sample;

import com.test.my.ApiBase;
import org.testng.annotations.Test;


public class TestDeleteSampleApi extends ApiBase {

    @Test
    public void DeleteUserTest() {
        requestSpecification.
        when().
            delete("https://reqres.in/api/users/111").
        then().
            statusCode(204);
    }
}
