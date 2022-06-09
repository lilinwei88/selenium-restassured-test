package com.test.my.utils;

import com.test.my.ApiBase;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;

public class RestAssuredRequestFilter405 implements Filter {

    private static final Logger logger = CustomLogger.getLogger(ApiBase.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        if (response.statusCode() != 405) {
            logger.error(requestSpec.getMethod() + " " + requestSpec.getURI() +  " => " +
                    response.getStatusCode() + " " + response.getStatusLine());
        }
        logger.info(requestSpec.getMethod() + " " + requestSpec.getURI() + " \n Request headers => \n" + requestSpec.getHeaders() + " \n Request Body =>" + requestSpec.getBody() + "\n Response Status => " +
                response.getStatusCode() + " " + response.getStatusLine() + " \n Response Body => " + response.getBody().prettyPrint());
        return response;
    }
}
